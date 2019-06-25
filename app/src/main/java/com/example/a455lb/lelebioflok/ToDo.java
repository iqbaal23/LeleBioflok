package com.example.a455lb.lelebioflok;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ToDo extends AppCompatActivity {
    DataHelper dbHelper;
    protected Cursor todo, todoPagi, todoSiang, todoMalam;
    ListView listView1;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_kolam:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    return true;
                case R.id.navigation_todo:
//                    startActivity(new Intent(getApplicationContext(), ToDo.class));
                    return true;
                case R.id.navigation_kalender:
                    startActivity(new Intent(getApplicationContext(), Kalender.class));
                    return true;
            }
            return false;
        }
    };


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

//        Button btn = (Button) findViewById(R.id.button1);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.listView1);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        for(int i=0; i<listAdapter.getGroupCount(); i++){
            expListView.expandGroup(i);
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_todo);
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        dbHelper = new DataHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        String date = df.format(calendar.getTime());

        todo = db.rawQuery("SELECT * FROM todo", null);
        todo.moveToFirst();
        if(todo.getCount()>0){
            int child = 0;
            // Adding child data
            List<String> pagi = new ArrayList<String>();
            todoPagi = db.rawQuery("SELECT * FROM todo WHERE waktu='0' and tanggal='"+date+"'", null);
            todoPagi.moveToFirst();

            if(todoPagi.getCount() > 0 ){
                listDataHeader.add("Pagi ( 08.00 WIB )");

                for(int i=0; i<todoPagi.getCount(); i++){
                    todoPagi.moveToPosition(i);
                    pagi.add(todoPagi.getString(4));
                }
                listDataChild.put(listDataHeader.get(child), pagi);
                child++;

            }


            List<String> siang = new ArrayList<String>();
            todoSiang = db.rawQuery("SELECT * FROM todo WHERE waktu='1' and tanggal='"+date+"'", null);
            todoSiang.moveToFirst();

            if(todoSiang.getCount()>0){
                listDataHeader.add("Siang ( 13.00 WIB )");

                for(int i=0; i<todoSiang.getCount(); i++){
                    todoSiang.moveToPosition(i);
                    siang.add(todoSiang.getString(4));
                }
                listDataChild.put(listDataHeader.get(child), siang);
                child++;
            }


            List<String> malam = new ArrayList<String>();
            todoMalam = db.rawQuery("SELECT * FROM todo WHERE waktu='2' and tanggal='"+date+"'", null);
            todoMalam.moveToFirst();

            if(todoMalam.getCount()>0){
                listDataHeader.add("Malam ( 19.00 WIB )");

                for(int i=0; i<todoMalam.getCount(); i++){
                    todoMalam.moveToPosition(i);
                    malam.add(todoMalam.getString(4));
                }
                listDataChild.put(listDataHeader.get(child), malam);
                child++;
            }

        } else {
            TextView pesanKosong = (TextView) findViewById(R.id.pesanKosong);
            pesanKosong.setText("Tidak ada aktivitas yang harus dilakukan");
        }
    }

    public void deleteTask(View view){
        View parent = (View) view.getParent();
        TextView pesan = (TextView) parent.findViewById(R.id.lblListItem);
        String isiPesan = String.valueOf(pesan.getText());
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        String date = df.format(calendar.getTime());


        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM todo WHERE pesan='"+isiPesan+"' and tanggal='"+date+"'");
        Toast.makeText(getApplicationContext(), "Berhasil Dilakukan",Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}
