package com.example.kin16.mycalendar.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB_OpenHelper {
    private static final String DATABASE_NAME = "Plans.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_Table.CreateDB._CREATE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+DB_Table.CreateDB._TABLENAME);
            onCreate(db);
        }
    }

    public DB_OpenHelper(Context context){
        this.mCtx = context;
    }

    public DB_OpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        mDB.close();
    }

    public long insertColumn(String title, String date, String year, String month, String day, String location, String memo){
        ContentValues values = new ContentValues();
        values.put(DB_Table.CreateDB.TITLE, title);
        values.put(DB_Table.CreateDB.DATE, date);
        values.put(DB_Table.CreateDB.YEAR, year);
        values.put(DB_Table.CreateDB.MONTH, month);
        values.put(DB_Table.CreateDB.DAY, day);
        values.put(DB_Table.CreateDB.LOCATION, location);
        values.put(DB_Table.CreateDB.MEMO, memo);
        return mDB.insert(DB_Table.CreateDB._TABLENAME, null, values);
    }

    public Cursor selectColumns(){
        return mDB.query(DB_Table.CreateDB._TABLENAME, null, null, null, null, null, null);
    }
}
