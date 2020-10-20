package com.example.whoismillionaire.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.whoismillionaire.model.Question;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseQuestion extends SQLiteAssetHelper {
    private static final String database_Name = "questions.db";
    private static final int database_Version = 1;

    public DatabaseQuestion(@Nullable Context context) {
        super(context, database_Name, null, database_Version);
    }

    public List<Question> getData(int userLevel){
        List<Question> list = new ArrayList<>();
        try {
            String sqlQuery;
            if(userLevel == 1) sqlQuery = "select * from table_question where level = 1 order by random() limit 5";
            else if (userLevel == 2) sqlQuery = "select * from table_question where level = 2 order by random() limit 5";
            else sqlQuery = "select * from table_question where level = 3 order by random() limit 5";
            SQLiteDatabase database = getReadableDatabase();
            Cursor cursor = database.rawQuery(sqlQuery, null);
            while (cursor.moveToNext()) {
                String question = cursor.getString(cursor.getColumnIndex("questions"));
                String answerA = cursor.getString(cursor.getColumnIndex("answerA"));
                String answerB = cursor.getString(cursor.getColumnIndex("answerB"));
                String answerC = cursor.getString(cursor.getColumnIndex("answerC"));
                String answerD = cursor.getString(cursor.getColumnIndex("answerD"));
                List<String> answerList = new ArrayList<>();
                answerList.add(answerA);
                answerList.add(answerB);
                answerList.add(answerC);
                answerList.add(answerD);
                int answer = cursor.getInt(cursor.getColumnIndex("answer"));
                int level = cursor.getInt(cursor.getColumnIndex("level"));
                list.add(new Question(question, answerList, answer, level));
            }
        }catch (Exception e){
            Log.e("Error", e.toString());
        }
        return list;
    }
}
