package com.example.a455lb.lelebioflok;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TestService extends Service {
    DataHelper dbHelper;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub

//        Toast.makeText(getApplicationContext(), "Service Created", Toast.LENGTH_LONG).show();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
//        Toast.makeText(getApplicationContext(), "Service Destroy", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        Cursor db_tanggal, db_isi, db_todo, db_kolam;

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final String date = df.format(Calendar.getInstance().getTime());

        dbHelper = new DataHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteDatabase db2 = dbHelper.getWritableDatabase();

        //cek tanggal
        db_tanggal = db.rawQuery("SELECT * FROM tanggal",null);
        db_tanggal.moveToFirst();
        if(!db_tanggal.getString(1).equals(date)){

            db_isi = db.rawQuery("SELECT * FROM isi_kolam",null);
            db_isi.moveToFirst();
            for(int i=0; i<db_isi.getCount(); i++){
                db_isi.moveToPosition(i);
                double berat_rata = Double.parseDouble(db_isi.getString(3));
                double ukuran = Double.parseDouble(db_isi.getString(4));

                berat_rata+=1.55;
                ukuran+=0.18;

                db2.execSQL("UPDATE isi_kolam set berat_rata_ikan='" +
                        berat_rata+"', ukuran_ikan='" +
                        ukuran+"' WHERE kode_isi='" +
                        db_isi.getString(0)+"'");

            }
            // update tanggal
            db.execSQL("UPDATE tanggal set tanggal='"+date+"'");

        }

//        Toast.makeText(getApplicationContext(), "Service Running ", Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }


}