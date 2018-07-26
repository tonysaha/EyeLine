package com.eyeline.diu.eyeline;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="user.db";
    public static final String TABLE_NAME="liveroute_table";
    public static final String COL_1="ID";
    public static final String COL_2="INSTRUCTION";
    public static final String COL_3="LATITUDE";
    public static final String COL_4="LONGITUDE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

       // SQLiteDatabase db=this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(" CREATE TABLE " + TABLE_NAME + " (" +
                COL_1 + " INTEGER PRIMARY KEY, " +
                COL_2 + " TEXT , " +
                COL_3 + " TEXT , " +
                COL_4 + " TEXT );"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
onCreate(db);
    }

    public boolean insertData(int sno,String instruction ,String latitude ,String longitude){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COL_1,sno);
        contentValues.put(COL_2,instruction);
        contentValues.put(COL_3,latitude);
        contentValues.put(COL_4,longitude);
        long result=db.insert(TABLE_NAME,null,contentValues);

        if (result==-1){
            return false;
        }else {
            return true;
        }

    }

    public Cursor getDatalive(){
    SQLiteDatabase db=this.getWritableDatabase();
    Cursor result=db.rawQuery("SELECT * FROM "+TABLE_NAME,null);

    return result;


    }

    public void deleteall(){

        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.close();
    }


    public void deleteRow(String id){
        SQLiteDatabase db=this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_NAME+ " WHERE "+COL_1+"='"+id+"'");
        db.close();
    }
}
