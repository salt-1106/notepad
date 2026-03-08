package com.example.xiangmuke.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "memo.db";
    private static final int DATABASE_VERSION = 2;  // 增加数据库版本号（如需要迁移功能时）

    // 表名和列名常量
    public static final String TABLE_MEMOS = "memos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_REMINDER_TIME = "reminder_time";

    // 创建表SQL语句
    private static final String CREATE_TABLE_MEMOS = "CREATE TABLE " + TABLE_MEMOS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_CONTENT + " TEXT,"
            + COLUMN_CATEGORY + " TEXT,"
            + COLUMN_REMINDER_TIME + " TEXT"
            + ")";

    // 删除表SQL语句
    private static final String DROP_TABLE_MEMOS = "DROP TABLE IF EXISTS " + TABLE_MEMOS;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建备忘录表
        db.execSQL(CREATE_TABLE_MEMOS);
        Log.d("DatabaseHelper", "Table created: " + TABLE_MEMOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabaseHelper", "Upgrading database from version " + oldVersion + " to " + newVersion);

        if (oldVersion < 2) {

            // 删除旧表并重建
            db.execSQL(DROP_TABLE_MEMOS);
            onCreate(db);
        }
    }
}
