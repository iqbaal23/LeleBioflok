package com.example.a455lb.lelebioflok;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class IsiKolam extends AppCompatActivity {
    protected Cursor cursor, db_kolam;
    DataHelper dbHelper;
    EditText text1, text2, text3, text4;
    Button btn1, btn2;
    Spinner pakan;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_kolam:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    return false;
                case R.id.navigation_todo:
                    startActivity(new Intent(getApplicationContext(), ToDo.class));
                    return true;
                case R.id.navigation_kalender:
                    startActivity(new Intent(getApplicationContext(), Kalender.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isi_kolam);

        findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        pakan = (Spinner) findViewById(R.id.spinner1);
        String[] pakanHari = {"2X","3X"};
        ArrayAdapter<String> PHadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pakanHari);
        PHadapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        pakan.setAdapter(PHadapter);

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        final String date = df.format(calendar.getTime());

        dbHelper = new DataHelper(this);
        text1 = (EditText) findViewById(R.id.editText1);
        text2 = (EditText) findViewById(R.id.editText2);
        text3 = (EditText) findViewById(R.id.editText3);
        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                if(TextUtils.isEmpty(text1.getText())){
                    text1.setError("Jumlah Ikan Harus Diisi");
                } else if(TextUtils.isEmpty(text2.getText())){
                    text2.setError("Berat Ikan Harus Diisi");
                } else if(TextUtils.isEmpty(text3.getText())){
                    text3.setError("Ukuran Ikan Harus Diisi");
                } else {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("INSERT INTO isi_kolam(kode_isi, kode_kolam, jumlah_ikan, berat_rata_ikan, ukuran_ikan, tanggal_tebar, frek) " +
                            "VALUES(null, '" +
                            getIntent().getStringExtra("kode_kolam") +"','" +
                            text1.getText().toString() + "','" +
                            text2.getText().toString() + "','" +
                            text3.getText().toString() + "','" +
                            date +"','"+ //text4.getText().toString() + "','" +
                            pakan.getSelectedItem().toString() +"')");

                    SQLiteDatabase db2 = dbHelper.getReadableDatabase();
                    cursor = db2.rawQuery("SELECT * FROM isi_kolam",null);
                    cursor.moveToLast();

                    Double jumlahIkan = Double.parseDouble(text1.getText().toString());
                    Double beratIkan = Double.parseDouble(text2.getText().toString());
                    double ukuran = Double.parseDouble(text3.getText().toString());

                    int fr;
                    if(ukuran < 9){
                        fr = 9;
                    } else if(ukuran >=9 && ukuran < 10.8){
                        fr = 7;
                    } else if(ukuran >=10.8 && ukuran < 12.6){
                        fr = 5;
                    } else if(ukuran >= 12.6 && ukuran < 14.4){
                        fr = 3;
                    } else{
                        fr = 2;
                    }

                    for(int i=0; i<=90; i++){
                        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.DAY_OF_YEAR, i);
                        String tgl = df.format(cal.getTime());

                        Double totalBerat = (jumlahIkan*beratIkan);

                        Double hitungPakan = totalBerat * fr / 100 / Integer.parseInt(pakan.getSelectedItem().toString().substring(0,1));
                        String jumlahPakan = String.format("%.2f", hitungPakan);

                        if(pakan.getSelectedItem().toString() == "3X"){
                            db.execSQL("INSERT INTO todo VALUES(null, '" +
                                    cursor.getString(1).toString()+"','" +
                                    tgl +"','" +
                                    "1','" +
                                    "Siang Ini Beri Pakan "+jumlahPakan+" gr di "+getIntent().getStringExtra("nama_kolam")+"')");
                        }
                        db.execSQL("INSERT INTO todo VALUES(null, '" +
                                cursor.getString(1).toString()+"','" +
                                tgl +"','" +
                                "0','" +
                                "Pagi Ini Beri Pakan "+jumlahPakan+" gr di "+getIntent().getStringExtra("nama_kolam")+"')");
                        db.execSQL("INSERT INTO todo VALUES(null, '" +
                                cursor.getString(1).toString()+"','" +
                                tgl +"','" +
                                "2','" +
                                "Malam Ini Beri Pakan "+jumlahPakan+" gr di "+getIntent().getStringExtra("nama_kolam")+"')");

                        if(i != 0 && i%10 == 0){
                            db_kolam = db.rawQuery("SELECT * FROM kolam WHERE kode_kolam='"+ getIntent().getStringExtra("kode_kolam") +"'", null);
                            db_kolam.moveToFirst();

                            double diameter = Double.parseDouble(db_kolam.getString(2))/100;
                            double tinggi = (Double.parseDouble(db_kolam.getString(3))-20)/100;
                            double volume = 3.14 * diameter * diameter / 4 * tinggi;
                            double probiotik = 5 * volume;
                            String prob = String.format("%.2f", probiotik);
                            db.execSQL("INSERT INTO todo VALUES(null, '" +
                                    cursor.getString(1).toString()+"','" +
                                    tgl +"','" +
                                    "0', '" +
                                    "Tambahkan Probiotik "+prob+" ml di "+db_kolam.getString(1)+"')");
                        }


                        beratIkan+= 1.55;
                        ukuran+=0.18;

                    }

                    Toast.makeText(getApplicationContext(), "Berhasil Diisi",Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), ToDo.class));

                }
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
