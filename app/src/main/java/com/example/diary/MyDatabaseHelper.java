package com.example.diary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_Diary = "create table diary ("
            + "id integer primary key autoincrement, title text, "
            + "content text, time text, author text)";
    private Context mContext;
    //创建数据库，参数name为数据库名
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }
    //创建数据库时为数据库建立表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_Diary);
        //Toast.makeText(mContext,"数据库创建成功",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
