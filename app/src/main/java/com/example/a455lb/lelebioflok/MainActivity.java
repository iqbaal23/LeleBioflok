package com.example.a455lb.lelebioflok;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.support.design.widget.NavigationView;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    String [] daftar;
    String [] kode_kolam;
    DataHelper dbcenter;
    protected Cursor cursor;
    ListView ListView01;
    public static MainActivity ma;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_kolam:
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    return true;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10);

        Intent intent = new Intent(this, TestService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),1000*60*60*1, pintent);
        startService(new Intent(getBaseContext(), TestService.class));

        Intent intent2 = new Intent(this, Reminder.class);
        PendingIntent pintent2 = PendingIntent.getService(this, 0, intent2, 0);
        AlarmManager alarm2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm2.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),1000*60*60*1, pintent2);
        startService(new Intent(getBaseContext(), Reminder.class));

        Button btn = (Button)findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent inte = new Intent(MainActivity.this, TambahKolam.class);
                startActivity(inte);
            }
        });

//        btn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), ToDo.class);
//                startActivity(i);
//            }
//        });

        ma = this;
        dbcenter = new DataHelper(this);
        RefreshList();


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void RefreshList(){
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM kolam", null);
        daftar = new String[cursor.getCount()];
        kode_kolam = new String[cursor.getCount()];
        cursor.moveToFirst();

        for (int i=0; i<cursor.getCount(); i++){
            cursor.moveToPosition(i);
            daftar[i] = cursor.getString(1).toString();
            kode_kolam[i] = cursor.getString(0).toString();
        }
        ListView01 = (ListView)findViewById(R.id.listView1);
        ListView01.setAdapter(new ArrayAdapter(this,android.R.layout.simple_list_item_1,daftar));
        ListView01.setSelected(true);
        ListView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String selection = kode_kolam[position];
                Intent i = new Intent(getApplicationContext(), UpdateKolam.class);
                i.putExtra("kode_kolam", selection);
                startActivity(i);
            }
        });
        ((ArrayAdapter)ListView01.getAdapter()).notifyDataSetInvalidated();

        TextView pesanKosong = (TextView) findViewById(R.id.pesanKosong);
        if(cursor.getCount() == 0){
            pesanKosong.setText("Klik tombol + untuk menambah kolam");
        }

    }

    @Override
    public void onRestart(){
        super.onRestart();
        startActivity(getIntent());
    }

}
