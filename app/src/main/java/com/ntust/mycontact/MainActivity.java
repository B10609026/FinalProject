package com.ntust.mycontact;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import com.ntust.mycontact.Location;
import com.ntust.mycontact.ToDoListDBAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity{

    private ToDoListDBAdapter toDoListDBAdapter;
    private List<Location> locations;
    private SQLiteDatabase mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ToDoListDBAdapter.ToDoListDBHelper dbHelper = new ToDoListDBAdapter.ToDoListDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        RecyclerView recyclerView = findViewById(R.id.todoListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toDoListDBAdapter = new ToDoListDBAdapter(this, getAllitems());
        recyclerView.setAdapter(toDoListDBAdapter);

    }


    private Cursor getAllitems(){
        return mDatabase.query(ToDoListDBAdapter.TABLE_LOCATION,null,null,null,null,null,null);
    }
}