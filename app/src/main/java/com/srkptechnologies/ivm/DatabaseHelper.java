package com.srkptechnologies.ivm;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String dbName = "ivm.db";
    public static  final int version = 1;

    public DatabaseHelper(Context context) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE ivm_products (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL, inStock INTEGER)";
        String sql1 = "CREATE TABLE ivm_orders (_id INTEGER PRIMARY KEY AUTOINCREMENT, orderList BLOB)";
        String sql2 = "CREATE TABLE ivm_history (_id INTEGER PRIMARY KEY AUTOINCREMENT, orderList BLOB, date TEXT)";
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(sql1);
        sqLiteDatabase.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
