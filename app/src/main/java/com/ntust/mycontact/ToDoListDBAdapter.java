package com.ntust.mycontact;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.ntust.mycontact.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by anildeshpande on 3/23/17.
 */

public class ToDoListDBAdapter extends RecyclerView.Adapter<ToDoListDBAdapter.ToDoListViewHolder> {
    private Context context;
    private Cursor mCursor;

    public ToDoListDBAdapter(Context context, @Nullable Cursor cursor){
        this.context = context;
        mCursor = cursor;
        sqLliteDatabase=new ToDoListDBHelper(this.context).getWritableDatabase();
    }

    private static final String TAG=ToDoListDBAdapter.class.getSimpleName();

    public static final String DB_NAME="location.db";
    public static final int DB_VERSION=2;

    public static final String TABLE_LOCATION="location";
    public static final String COLUMN_ID="id";
    public static final String COLUMN_LONGITUDE="longitude";
    public static final String COLUMN_LATITUDE="latitude";
    public static final String COLUMN_NAME="name";

    //create table table_todo(task_id integer primary key, todo text not null);

    public static String CREATE_TABLE_LOCATION="CREATE TABLE "+TABLE_LOCATION+"("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_LONGITUDE+" REAL NOT NULL, "+
            COLUMN_LATITUDE+ " REAL NOT NULL, "+COLUMN_NAME+"TEXT)";


    private SQLiteDatabase  sqLliteDatabase;
    private static ToDoListDBAdapter toDoListDBAdapterInstance;

    public class ToDoListViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView longtitude;
        public TextView latitude;

        public ToDoListViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textview_name);
            longtitude = itemView.findViewById(R.id.textview_longitude);
            latitude = itemView.findViewById(R.id.textview_latitude);

        }
    }

    @NonNull
    @Override
    public ToDoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.todolist_item,parent,false);
        return new ToDoListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ToDoListDBAdapter.ToDoListViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)){
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(COLUMN_NAME));
        String longitute = mCursor.getString(mCursor.getColumnIndex(COLUMN_LONGITUDE));
        String latitude = mCursor.getString(mCursor.getColumnIndex(COLUMN_LATITUDE));

        holder.name.setText(name);
        holder.longtitude.setText(longitute);
        holder.latitude.setText(latitude);
    }

    @Override
    public int getItemCount() {


        return mCursor.getCount();
    }



    public static ToDoListDBAdapter getToDoListDBAdapterInstance(Context context,Cursor cursor){
        if(toDoListDBAdapterInstance==null){
            toDoListDBAdapterInstance=new ToDoListDBAdapter(context,cursor);
        }
        return toDoListDBAdapterInstance;
    }

    //Will be used in the content provider
    public Cursor getCursorsForAllToDos(){
        Cursor cursor=sqLliteDatabase.query(TABLE_LOCATION,new String[]{COLUMN_ID,COLUMN_NAME, COLUMN_LONGITUDE,COLUMN_LATITUDE},null,null,null,null,null,null);
        return cursor;
    }

    public Cursor getCursorForSpecificPlace(String longitute, String latitude){
        //SELECT task_id,todo FROM table_todo WHERE place LIKE '%desk%'
        Cursor cursor=sqLliteDatabase.query(TABLE_LOCATION,new String[]{COLUMN_ID,COLUMN_NAME},COLUMN_LONGITUDE +" LIKE '%"+longitute+"%' AND "+COLUMN_LATITUDE+" LIKE '%"+latitude+"%'",null,null,null,null,null);
        return cursor;
    }

    public Cursor getCount(){
        Cursor cursor=sqLliteDatabase.rawQuery("SELECT COUNT(*) FROM "+TABLE_LOCATION,null);
        return cursor;
    }

    //insert,delete,modify,query methods

    public boolean insert(String name){
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_NAME,name);

        return sqLliteDatabase.insert(TABLE_LOCATION,null,contentValues)>0;
    }

    public boolean insert(String name, String longitude, String latitude){
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_NAME,name);
        contentValues.put(COLUMN_LONGITUDE,longitude);
        contentValues.put(COLUMN_LATITUDE,latitude);

        return sqLliteDatabase.insert(TABLE_LOCATION,null,contentValues)>0;
    }

    //Will be used in the content provider
    public long insert(ContentValues contentValues){
        return sqLliteDatabase.insert(TABLE_LOCATION,null,contentValues);
    }

    public boolean delete(int locationId){
        return sqLliteDatabase.delete(TABLE_LOCATION, COLUMN_ID+" = "+locationId,null)>0;
    }

    //Will be used by the provider
    public int delete(String whereClause, String [] whereValues){
        return sqLliteDatabase.delete(TABLE_LOCATION,whereClause,whereValues);
    }

    public boolean modify(int locationId, String newToDoItem){
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_NAME,newToDoItem);

        return sqLliteDatabase.update(TABLE_LOCATION,contentValues, COLUMN_ID+" = "+locationId,null)>0;
    }

    //Will be used in the content provider
    public int update(ContentValues contentValues, String s, String [] strings){
        return sqLliteDatabase.update(TABLE_LOCATION,contentValues, s,strings);
    }

    public List<Location> getAllToDos(){
        List<Location> toDoList=new ArrayList<Location>();

        Cursor cursor=sqLliteDatabase.query(TABLE_LOCATION,new String[]{COLUMN_ID,COLUMN_NAME, COLUMN_LONGITUDE,COLUMN_LATITUDE},null,null,null,null,null,null);

        if(cursor!=null &cursor.getCount()>0){
            while(cursor.moveToNext()){
                Location location=new Location(cursor.getLong(0),cursor.getString(1), cursor.getString(2), cursor.getString(3));
                toDoList.add(location);

            }
        }
        cursor.close();
        return toDoList;
    }



    public static class ToDoListDBHelper extends SQLiteOpenHelper{

        public ToDoListDBHelper(Context context){
            super(context,DB_NAME,null,DB_VERSION);
        }

        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.setForeignKeyConstraintsEnabled(true);
            Log.i(TAG,"Inside onConfigure");
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE_LOCATION);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase,
                              int oldVersion, int newVersion) {
            //Not implemented now

            switch (oldVersion){
                case 1: sqLiteDatabase.execSQL("ALTER TABLE "+TABLE_LOCATION+ " ADD COLUMN "+COLUMN_LONGITUDE+" TEXT");break;
                default: break;
            }
            Log.i(TAG,"Inside onUpgrade");
        }
    }

}