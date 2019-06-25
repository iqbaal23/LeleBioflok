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
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Kalender extends AppCompatActivity {
    protected Cursor cursor;
    DataHelper dbHelper;
    EditText text1, text2, text3;
    Button btn1, btn2;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_kolam:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    return true;
                case R.id.navigation_todo:
                    startActivity(new Intent(getApplicationContext(), ToDo.class));
                    return true;
                case R.id.navigation_kalender:
//                    startActivity(new Intent(getApplicationContext(), Kalender.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalender);

        CalendarView calendarView = (CalendarView) findViewById(R.id.kalender);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String tanggal = ""+dayOfMonth;
                if(tanggal.length() == 1){
                    tanggal = "0"+tanggal;
                }
                String bulan = ""+(month+1);
                if(bulan.length() == 1){
                    bulan = "0"+bulan;
                }
                Intent i = new Intent(getApplicationContext(), DetailKalender.class);
                i.putExtra("tanggal", tanggal+"-"+bulan+"-"+year);
                startActivity(i);
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_kalender);
    }

}
