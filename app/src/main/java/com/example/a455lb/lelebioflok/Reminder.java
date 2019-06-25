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
import android.graphics.Color;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Reminder extends Service {
    DataHelper dbHelper;
    protected Cursor db_todo;

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

        dbHelper = new DataHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat cf = new SimpleDateFormat("HH");
        Calendar cal = Calendar.getInstance();
        String tgl = df.format(cal.getTime());
        int jam = Integer.parseInt(cf.format(cal.getTime()));

        db_todo = db.rawQuery("SELECT * FROM todo WHERE tanggal='"+ tgl +"'", null);
        db_todo.moveToFirst();

        for(int i=0; i<db_todo.getCount(); i++){
            db_todo.moveToPosition(i);
            if(jam >= 7 && jam < 12 ){
                if(db_todo.getString(3).equals("0")){
                    notif(db_todo.getString(4), i);
                }
//                Toast.makeText(getApplicationContext(), "Pagi", Toast.LENGTH_SHORT).show();
            } else if(jam >=12 && jam < 18){
                if(db_todo.getString(3).equals("1")){
                    notif(db_todo.getString(4), i);
                }
//                Toast.makeText(getApplicationContext(), "Siang", Toast.LENGTH_SHORT).show();
            } else if(jam >=18 && jam < 22){
                if(db_todo.getString(3).equals("2")){
                    notif(db_todo.getString(4), i);
                }
//                Toast.makeText(getApplicationContext(), "Sore", Toast.LENGTH_SHORT).show();
            } else{
//                Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_LONG).show();
            }
        }
//        if(db_todo.getCount()==0){
//            Toast.makeText(getApplicationContext(), "Todo Kosong", Toast.LENGTH_LONG).show();
//        }

        return super.onStartCommand(intent, flags, startId);
    }

    protected  void notif(String pesan, int notifyID){
        Intent notificationIntent = new Intent(this, Activity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification noti = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("REMINDER")
                .setContentText(pesan)
                .setVibrate(new long[] { 500, 500 })
                .setLights(Color.RED, 3000, 3000)
                .setContentIntent(contentIntent)
                .setAutoCancel(true).build();
        notificationManager.notify(notifyID, noti);
    }
}