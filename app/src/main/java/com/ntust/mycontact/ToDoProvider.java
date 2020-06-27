package com.ntust.mycontact;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.ntust.mycontact.ToDoListDBAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



public class ToDoProvider extends ContentProvider {

    public static final String AUTHORITY="com.ntust.mycontact";

    public static final String PATH_LOCATIONS ="LOCATIONS";
    public static final String PATH_NEARBY ="NEARBY";

    public static final Uri CONTENT_URI_1=Uri.parse("content://"+AUTHORITY+"/"+ PATH_LOCATIONS);
    public static final Uri CONTENT_URI_2=Uri.parse("content://"+AUTHORITY+"/"+ PATH_NEARBY);

    public static final int LOCATIONS =1;
    public static final int NEARBY=2;

    private static final UriMatcher MATCHER=new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, PATH_LOCATIONS, LOCATIONS);
        MATCHER.addURI(AUTHORITY, PATH_NEARBY,NEARBY);
    }

    public static final String MIME_TYPE_1 = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+ "vnd.com.ntust.locations";
    public static final String MIME_TYPE_2 = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+ "vnd.com.ntust.nearby";

    private ToDoListDBAdapter toDoListDBAdapter;

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)){
            case LOCATIONS: return MIME_TYPE_1;
            case NEARBY: return MIME_TYPE_2;
        }
        return null;
    }



    @Override
    public boolean onCreate() {
        toDoListDBAdapter = ToDoListDBAdapter.getToDoListDBAdapterInstance(getContext(),null);
//        toDoListDBAdapter=ToDoListDBAdapter.getToDoListDBAdapterInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        Cursor cursor=null;
        switch (MATCHER.match(uri)){
            case LOCATIONS: cursor=toDoListDBAdapter.getCursorsForAllToDos();break;
//            case NEARBY: cursor=toDoListDBAdapter.getCursorForSpecificPlace(strings1[0]);break;
            default:cursor=null; break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) throws UnsupportedOperationException{
        Uri returnUri = null;
        switch (MATCHER.match(uri)){
            case LOCATIONS: returnUri= insertToDo(uri,contentValues);break;
            default: new UnsupportedOperationException("insert operation not supported"); break;
        }

        return returnUri ;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) throws UnsupportedOperationException{
        int updateCount=-1;
        switch (MATCHER.match(uri)){
            case LOCATIONS: updateCount=update(contentValues,s,strings);break;
            default:new UnsupportedOperationException("insert operation not supported"); break;
        }
        return updateCount;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) throws UnsupportedOperationException{
        int deleteCount=-1;
        switch (MATCHER.match(uri)){
            case LOCATIONS: deleteCount= delete(s,strings);break;
            default:new UnsupportedOperationException("delete operation not supported"); break;
        }
        return deleteCount;
    }

    private int update(ContentValues contentValues, String whereCluase, String [] strings){
        return toDoListDBAdapter.update(contentValues,whereCluase,strings);
    }


    private Uri insertToDo(Uri uri, ContentValues contentValues){
        long id = toDoListDBAdapter.insert(contentValues);
        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.parse("content://"+AUTHORITY+"/"+ PATH_LOCATIONS +"/"+id);
    }

    private int delete(String whereClause, String [] whereValues){
        return toDoListDBAdapter.delete(whereClause,whereValues);
    }

}
