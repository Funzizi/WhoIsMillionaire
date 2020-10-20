package com.example.whoismillionaire.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.whoismillionaire.model.History;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHistory extends SQLiteAssetHelper {
    private final static String database_Name = "history.db";
    public final static String table_Name = "history";
    private static final int database_Version = 1;

    public DatabaseHistory(Context context) {
        super(context, database_Name, null, database_Version);
    }

    // Set History
    public void setHistory(String table_Name, String[] columns, String[] dataColumns){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        for(int i = 0; i < columns.length; i++){
            values.put(columns[i], dataColumns[i]);
        }
        long record = database.insert(table_Name, null, values);
    }

    // Get History
    public List<History> getHistory(){
        List<History> list = new ArrayList<>();
        try {
            String sqlQuery = "select * from history order by number_question desc, times asc limit 5";
            SQLiteDatabase database = getReadableDatabase();
            Cursor cursor = database.rawQuery(sqlQuery, null);
            while (cursor.moveToNext()) {
                int numberQuestion = cursor.getInt(cursor.getColumnIndex("number_question"));
                String times = cursor.getString(cursor.getColumnIndex("times"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                list.add(new History(numberQuestion, times, date));
            }
        }catch (Exception e){
            Log.e("Error", e.toString());
        }
        return list;
    }
}