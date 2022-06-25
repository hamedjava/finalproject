package com.example.notebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDatabase extends SQLiteOpenHelper {

    private Context context;
    private static final String DB_NAME = "notebook";
    private static final int DB_VERSION = 1;
    private static final String TBL_NAME = "note";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_DESC = "description";
    private static final String COL_REMEMBER = "remember";
    private static final String COL_DATE = "date";
    private static final String COL_TIME = "time";


    private static final String QUERY = "CREATE TABLE IF NOT EXISTS " +TBL_NAME+"( " +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
            COL_TITLE +" TEXT,"+
            COL_DESC +" TEXT,"+
            COL_REMEMBER +" INTEGER ,"+
            COL_DATE+" TEXT," +
            COL_TIME + " TEXT );";

    public NoteDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(QUERY);
        }catch (SQLiteException e){
            e.printStackTrace();
        }



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long addInfo(String title,String desc,int remember,String date,String time){
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_TITLE,title);
        contentValues.put(COL_DESC,desc);
        contentValues.put(COL_REMEMBER,remember);
        contentValues.put(COL_DATE,date);
        contentValues.put(COL_TIME,time);
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        long id=sqLiteDatabase.insert(TBL_NAME,null,contentValues);
        return id;
    }

    public Cursor getInfos(){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+TBL_NAME,null);
        return cursor;
    }

    public Cursor getSomeInfo(int remember){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM "+TBL_NAME+" WHERE "+ COL_REMEMBER+" =?",new String[]{String.valueOf(remember)});
    }
    public Cursor searchInfo(int id){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TBL_NAME + " WHERE " + COL_ID + " =? " , new String[]{String.valueOf(id)});
    }
    public void deleteRow(int id){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.delete(TBL_NAME,COL_ID+" = ?",new String[]{String.valueOf(id)});
    }

    public void updateRow(int id,String title,String desc){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_DESC, desc);

        sqLiteDatabase.update(TBL_NAME,contentValues,COL_ID+" =? ",new String[]{String.valueOf(id)});
    }
}
