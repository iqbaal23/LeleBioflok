package com.example.a455lb.lelebioflok;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class UpdateIsiKolam extends AppCompatActivity {
    DataHelper dbHelper;
    EditText text1, text2, text3, text4;
    Button btn1, btn2, btn_hapus_isi;
    protected Cursor db_isi, db_kolam;
    Spinner pakan;

    public static final String DATE_FORMAT = "dd-MM-yyyy";

    public static long getDaysBetweenDates(String start, String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        Date startDate, endDate;
        long numberOfDays = 0;
        try {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
            numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numberOfDays;
    }

    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

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
        setContentView(R.layout.activity_update_isi_kolam);

        pakan = (Spinner) findViewById(R.id.spinner1);
        String[] pakanHari = {"2X","3X"};
        ArrayAdapter<String> PHadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pakanHari);
        pakan.setAdapter(PHadapter);

        dbHelper = new DataHelper(this);
        text1 = (EditText) findViewById(R.id.editText1);
        text2 = (EditText) findViewById(R.id.editText2);
        text3 = (EditText) findViewById(R.id.editText3);
        text4 = (EditText) findViewById(R.id.editText4);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db_isi = db.rawQuery("SELECT * FROM isi_kolam WHERE kode_kolam='" +
                getIntent().getStringExtra("kode_kolam")+ "'", null);
        db_isi.moveToFirst();
        if(db_isi.getCount()>0){
            text1.setText(db_isi.getString(2).toString());
            text2.setText(db_isi.getString(3).toString());
            text3.setText(db_isi.getString(4).toString());
            text4.setText(db_isi.getString(5).toString());
            pakan.setSelection(((ArrayAdapter<String>)pakan.getAdapter()).getPosition(db_isi.getString(6).toString()));
        }

        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        btn_hapus_isi = (Button) findViewById(R.id.btn_hapus_isi);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(text1.getText())){
                    text1.setError("Jumlah Ikan Harus Diisi");
                } else if(TextUtils.isEmpty(text2.getText())){
                    text2.setError("Berat Ikan Harus Diisi");
                } else if(TextUtils.isEmpty(text3.getText())){
                    text3.setError("Ukuran Ikan Harus Diisi");
                } else {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("UPDATE isi_kolam set jumlah_ikan='" +
                            text1.getText().toString()+"', berat_rata_ikan='" +
                            text2.getText().toString()+"', ukuran_ikan='" +
                            text3.getText().toString()+"', tanggal_tebar='" +
                            text4.getText().toString()+"', frek='" +
                            pakan.getSelectedItem().toString()+"' WHERE kode_kolam='" +
                            getIntent().getStringExtra("kode_kolam")+"'");

                    SQLiteDatabase db2 = dbHelper.getReadableDatabase();
                    db_isi = db2.rawQuery("SELECT * FROM isi_kolam WHERE kode_kolam='"+getIntent().getStringExtra("kode_kolam")+"'",null);
                    db_isi.moveToFirst();

                    String [] tanggalAwal = db_isi.getString(5).split("-");
                    int hari1 = Integer.parseInt(tanggalAwal[0]);
                    int bulan1 = Integer.parseInt(tanggalAwal[1]);
                    int tahun1 = Integer.parseInt(tanggalAwal[2]);

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

                    db.execSQL("DELETE FROM todo WHERE kode_isi='"+ db_isi.getString(1)+"'");

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
                                    db_isi.getString(1).toString()+"','" +
                                    tgl +"','" +
                                    "1','" +
                                    "Siang Ini Beri Pakan "+jumlahPakan+" gr di "+getIntent().getStringExtra("nama_kolam")+"')");
                        }
                        db.execSQL("INSERT INTO todo VALUES(null, '" +
                                db_isi.getString(1).toString()+"','" +
                                tgl +"','" +
                                "0','" +
                                "Pagi Ini Beri Pakan "+jumlahPakan+" gr di "+getIntent().getStringExtra("nama_kolam")+"')");
                        db.execSQL("INSERT INTO todo VALUES(null, '" +
                                db_isi.getString(1).toString()+"','" +
                                tgl +"','" +
                                "2','" +
                                "Malam Ini Beri Pakan "+jumlahPakan+" gr di "+getIntent().getStringExtra("nama_kolam")+"')");

//                        String [] tanggalAkhir = tgl.split("-");
//                        int hari2 = Integer.parseInt(tanggalAkhir[0]);
//                        int bulan2 = Integer.parseInt(tanggalAkhir[1]);
//                        int tahun2 = Integer.parseInt(tanggalAkhir[2]);
//
//                        int tahun = tahun2-tahun1;
//                        if(tahun > 0){
//                            bulan2+=12;
//                        }
//                        int bulan = bulan2-bulan1;
//                        if(bulan > 0){
//                            hari2+=(bulan*30);
//                        }
//                        int hari = hari2-hari1;
//                        if(hari != 0 && (hari % 10) == 0){
                        long diff = getDaysBetweenDates(db_isi.getString(5),tgl);
                        if(diff != 0 && diff%10 == 0){

                            db_kolam = db2.rawQuery("SELECT * FROM kolam WHERE kode_kolam='"+ db_isi.getString(1) +"'", null);
                            db_kolam.moveToFirst();

                            double diameter = Double.parseDouble(db_kolam.getString(2))/100;
                            double tinggi = (Double.parseDouble(db_kolam.getString(3))-20)/100;
                            double volume = 3.14 * diameter * diameter / 4 * tinggi;
                            double probiotik = 5 * volume;
                            String prob = String.format("%.2f", probiotik);
                            db.execSQL("INSERT INTO todo VALUES(null, '" +
                                    db_isi.getString(1).toString()+"','" +
                                    tgl +"','" +
                                    "0', '" +
                                    "Tambahkan Probiotik "+prob+" ml di "+db_kolam.getString(1)+"')");
                        }

                        beratIkan+= 1.55;
                        ukuran+=0.18;

                    }
                    Toast.makeText(getApplicationContext(),"Berhasil Diupdate", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_hapus_isi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SQLiteDatabase db = dbHelper.getWritableDatabase();
//                SQLiteDatabase db2 = dbHelper.getReadableDatabase();
//                db_isi = db2.rawQuery("SELECT * FROM isi_kolam WHERE kode_kolam='"+getIntent().getStringExtra("kode_kolam")+"'",null);
//                db_isi.moveToFirst();
//                db.execSQL("DELETE FROM todo WHERE kode_isi='"+db_isi.getString(1)+"'");
//                db.execSQL("DELETE FROM isi_kolam WHERE kode_kolam='" +
//                        getIntent().getStringExtra("kode_kolam")+"'");
//                Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
//                finish();
                AlertDialog diaBox = AskOption();
                diaBox.show();
            }
        });


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Hapus")
                .setMessage("Apa Anda Yakin?")
//                .setIcon(R.drawable.ic_delete_white_24dp)

                .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        SQLiteDatabase db2 = dbHelper.getReadableDatabase();
                        db_isi = db2.rawQuery("SELECT * FROM isi_kolam WHERE kode_kolam='"+getIntent().getStringExtra("kode_kolam")+"'",null);
                        db_isi.moveToFirst();
                        db.execSQL("DELETE FROM todo WHERE kode_isi='"+ db_isi.getString(1)+"'");
                        db.execSQL("DELETE FROM isi_kolam WHERE kode_kolam='" +
                                getIntent().getStringExtra("kode_kolam")+"'");
                        Toast.makeText(getApplicationContext(), "Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                        finish();
                        dialog.dismiss();
                    }

                })



                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }
}
