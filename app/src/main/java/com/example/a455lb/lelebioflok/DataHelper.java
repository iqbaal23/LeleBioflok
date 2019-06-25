package com.example.a455lb.lelebioflok;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bioflok.db";
    private static final int DATABASE_VERSION = 1;

    public DataHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        // table kolam
        String sql = "CREATE TABLE kolam(kode_kolam INTEGER PRIMARY KEY AUTOINCREMENT, nama_kolam TEXT NOT NULL, diameter_kolam INTEGER NOT NULL, tinggi_kolam INTEGER NOT NULL);";
        db.execSQL(sql);
        sql = "INSERT INTO kolam (kode_kolam, nama_kolam, diameter_kolam, tinggi_kolam) VALUES (null, 'Kolam 1','120','120');";
        db.execSQL(sql);
        sql = "INSERT INTO kolam (kode_kolam, nama_kolam, diameter_kolam, tinggi_kolam) VALUES (null, 'Kolam 2','150','130');";
        db.execSQL(sql);

        //table isi
        sql = "CREATE TABLE isi_kolam(kode_isi INTEGER PRIMARY KEY AUTOINCREMENT, kode_kolam INTEGER NOT NULL, jumlah_ikan INTEGER NOT NULL, berat_rata_ikan INTEGER NOT NULL, ukuran_ikan INTEGER NOT NULL, tanggal_tebar TEXT NOT NULL, frek TEXT NOT NULL);";
        db.execSQL(sql);
        db.execSQL("INSERT INTO isi_kolam VALUES(null, 1, 1000, 5, 5, '13-01-2019', '3X')");
        db.execSQL("INSERT INTO isi_kolam VALUES(null, 2, 1500, 6, 5, '13-01-2019', '3X')");

        //table todo
        sql = "CREATE TABLE todo(kode_todo INTEGER PRIMARY KEY AUTOINCREMENT, kode_isi INTEGER, tanggal TEXT NOT NULL, waktu INTEGER NOT NULL, pesan TEXT NOT NULL);";
        db.execSQL(sql);

        sql = "CREATE TABLE tanggal (kode_tanggal INTEGER PRIMARY KEY AUTOINCREMENT, tanggal TEXT NOT NULL);";
        db.execSQL(sql);

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//        String date = df.format(Calendar.getInstance().getTime());
//        db.execSQL("INSERT INTO tanggal VALUES(null,'"+date+"')");
        db.execSQL("INSERT INTO tanggal VALUES(null,'22-01-2019')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2){

    }
}
