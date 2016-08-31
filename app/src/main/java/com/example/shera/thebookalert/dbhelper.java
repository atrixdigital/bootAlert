package com.example.shera.thebookalert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shera on 8/22/2016.
 */
public class dbhelper extends SQLiteOpenHelper {

    public static final  String dbname = "User";
    public static final String table = "allUsers";
    public static final String id = "ID";
    public static final String contact = "contact";



    dbhelper(Context context)
    {
        super(context, dbname, null, 1);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // db.execSQL("create table if not exists " + table + "(ID INTEGER PRIMARY KEY AUTOINCREMENT," + email + " TEXT," + contact + " TEXT);");
        db.execSQL("create table if not exists " + table + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"  + contact + " TEXT);");


    }

    public Boolean update( String con)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(contact, con);

        Cursor data;
        SQLiteDatabase dbs = this.getReadableDatabase();
        data = dbs.rawQuery("SELECT * FROM " + table, null);


        long check;
        if (String.valueOf(data.getCount())== "0" )
        {

            check = db.insert(table, null, values);

        }

        else {
            check = db.update(table, values,id + "=1",null);

        }

        if(check == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    public Cursor GetUser()
    {
        Cursor data;
        SQLiteDatabase dbs = this.getReadableDatabase();
        data = dbs.rawQuery("SELECT * FROM " + table, null);
        return data;
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
