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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateKolam extends AppCompatActivity {
    protected Cursor cursor, cursor2;
    DataHelper dbHelper;
    Button btn1, btn2, btn_isi, btn_hapus_kolam;
    EditText text1, text2, text3;
    String isi[];
    ListView lv_isi;

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
        setContentView(R.layout.activity_update_kolam);

        dbHelper = new DataHelper(this);
        text1 = (EditText) findViewById(R.id.editText1);
        text2 = (EditText) findViewById(R.id.editText2);
        text3 = (EditText) findViewById(R.id.editText3);
        TextView keterangan = (TextView) findViewById(R.id.keterangan);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM kolam WHERE kode_kolam = '" +
                getIntent().getStringExtra("kode_kolam") + "'",null);
        cursor.moveToFirst();
        if(cursor.getCount()>0){
            cursor.moveToPosition(0);
            text1.setText(cursor.getString(1).toString());
            text2.setText(cursor.getString(2).toString());
            text3.setText(cursor.getString(3).toString());
        }

        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        btn_isi = (Button) findViewById(R.id.btn_isikolam);
        btn_hapus_kolam = (Button) findViewById(R.id.btn_hapus_kolam);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(text1.getText())){
                    text1.setError("Nama Kolam Harus Diisi!");
                } else if(TextUtils.isEmpty(text2.getText())){
                    text2.setError("Diameter Kolam Harus Diisi");
                } else if(TextUtils.isEmpty(text3.getText())){
                    text3.setError("Tinggi Kolam Harus Diisi!");
                } else {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("UPDATE kolam set nama_kolam='" +
                            text1.getText().toString() +"', diameter_kolam='" +
                            text2.getText().toString() + "', tinggi_kolam='" +
                            text3.getText().toString() + "' WHERE kode_kolam='" +
                            getIntent().getStringExtra("kode_kolam") + "'");
                    Toast.makeText(getApplicationContext(), "Berhasil Diupdate", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(back);
            }
        });

        cursor2 = db.rawQuery("SELECT * FROM isi_kolam WHERE kode_kolam = '" +
                getIntent().getStringExtra("kode_kolam") + "'",null);
        cursor2.moveToFirst();
        if(cursor2.getCount()>0){
            btn_isi.setText("Edit Data Ikan");
        } else{
            btn_isi.setText("Masukkan Ikan");

            String nama_kolam = cursor.getString(1).toString();
            double diameter = Double.parseDouble(cursor.getString(2).toString());
            int tinggi = (Integer.parseInt(cursor.getString(3).toString()))-20;
            double d = diameter/100;
            double t = ((double) tinggi )/100;
            double volume = 3.14*d*d/4*t;
            double probiotik = 5*volume;
            double prebiotik = 250*volume;
            double dolomite = 150*volume;
            int ikan = (int) (1000*volume);

            keterangan.setText("Isi kolam dengan air setinggi "+tinggi+" cm\n" +
                    "Masukkan probiotik sebanyak "+String.format("%.2f",probiotik)+" ml\n" +
                    "Masukkan prebiotik sebanyak "+String.format("%.2f", prebiotik)+" ml\n" +
                    "Masukkan dolomite sebanyak "+String.format("%.2f", dolomite)+ " gram\n" +
                    "Diamkan air selama 7 hari, lalu masukkan ikan\n" +
                    "Idealnya kolam ini dapat menampung hingga "+ikan+" ikan lele");
        }

        btn_isi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i;
                if(cursor2.getCount()>0){
                    i = new Intent(getApplicationContext(), UpdateIsiKolam.class);
                } else{
                    i = new Intent(getApplicationContext(), IsiKolam.class);
                }
                i.putExtra("kode_kolam", getIntent().getStringExtra("kode_kolam"));
                i.putExtra("nama_kolam", cursor.getString(1));
                startActivity(i);
            }


        });

        btn_hapus_kolam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SQLiteDatabase db = dbHelper.getWritableDatabase();
//                SQLiteDatabase db2 = dbHelper.getReadableDatabase();
//                db_isi = db2.rawQuery("SELECT * FROM isi_kolam WHERE kode_kolam='"+getIntent().getStringExtra("kode_kolam")+"'",null);
//                db_isi.moveToFirst();
//                if(db_isi.getCount()>0){
//                    db.execSQL("DELETE FROM todo WHERE kode_isi='"+db_isi.getString(1)+"'");
//                    db.execSQL("DELETE FROM isi_kolam WHERE kode_kolam='" +
//                            getIntent().getStringExtra("kode_kolam")+"'");
//                }
//                db.execSQL("DELETE FROM kolam WHERE kode_kolam='" +
//                        getIntent().getStringExtra("kode_kolam")+"'");
//                Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
//                finish();
//                Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(i);
                AlertDialog diaBox = AskOption();
                diaBox.show();
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onRestart(){
        super.onRestart();
        startActivity(getIntent());
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
                        cursor = db2.rawQuery("SELECT * FROM isi_kolam WHERE kode_kolam='"+getIntent().getStringExtra("kode_kolam")+"'",null);
                        cursor.moveToFirst();
                        if(cursor.getCount()>0){
                            db.execSQL("DELETE FROM todo WHERE kode_isi='"+cursor.getString(1)+"'");
                            db.execSQL("DELETE FROM isi_kolam WHERE kode_kolam='" +
                                    getIntent().getStringExtra("kode_kolam")+"'");
                        }
                        db.execSQL("DELETE FROM kolam WHERE kode_kolam='" +
                                getIntent().getStringExtra("kode_kolam")+"'");
                        Toast.makeText(getApplicationContext(), "Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
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
