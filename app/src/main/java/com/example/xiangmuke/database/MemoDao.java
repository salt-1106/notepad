package com.example.xiangmuke.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.xiangmuke.models.Memo;

import java.util.ArrayList;
import java.util.List;

public class MemoDao {
    private DatabaseHelper databaseHelper;

    public MemoDao(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    // 插入备忘录记录
    public long insertMemo(Memo memo) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Memo.COLUMN_TITLE, memo.getTitle());
        values.put(Memo.COLUMN_CONTENT, memo.getContent());
        values.put(Memo.COLUMN_CATEGORY, memo.getCategory());
        values.put(Memo.COLUMN_REMINDER_TIME, memo.getReminderTime());
        return db.insert(DatabaseHelper.TABLE_MEMOS, null, values);
    }

    // 根据ID查询备忘录记录
    public Memo getMemoById(int id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columns = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_TITLE,
                DatabaseHelper.COLUMN_CONTENT,
                DatabaseHelper.COLUMN_CATEGORY,
                DatabaseHelper.COLUMN_REMINDER_TIME
        };
        String selection = DatabaseHelper.COLUMN_ID + " =?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(DatabaseHelper.TABLE_MEMOS, columns, selection, selectionArgs, null, null, null);
        Memo memo = null;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
            int titleIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE);
            int contentIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTENT);
            int categoryIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY);
            int reminderTimeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_REMINDER_TIME);

            if (idIndex >= 0 && titleIndex >= 0 && contentIndex >= 0 && categoryIndex >= 0 && reminderTimeIndex >= 0) {
                memo = new Memo(
                        cursor.getInt(idIndex),
                        cursor.getString(titleIndex),
                        cursor.getString(contentIndex),
                        cursor.getString(categoryIndex),
                        cursor.getString(reminderTimeIndex)
                );
            } else {
                Log.e("MemoDao", "Column index is invalid, please check table structure or column names.");
                throw new RuntimeException("Column index is invalid, please check table structure or column names.");
            }
        }
        cursor.close();
        return memo;
    }

    // 查询所有备忘录记录
    public List<Memo> getAllMemos() {
        List<Memo> memoList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columns = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_TITLE,
                DatabaseHelper.COLUMN_CONTENT,
                DatabaseHelper.COLUMN_CATEGORY,
                DatabaseHelper.COLUMN_REMINDER_TIME
        };
        Cursor cursor = db.query(DatabaseHelper.TABLE_MEMOS, columns, null, null, null, null, null);
        int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
        int titleIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE);
        int contentIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTENT);
        int categoryIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY);
        int reminderTimeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_REMINDER_TIME);

        if (idIndex >= 0 && titleIndex >= 0 && contentIndex >= 0 && categoryIndex >= 0 && reminderTimeIndex >= 0) {
            while (cursor.moveToNext()) {
                Memo memo = new Memo(
                        cursor.getInt(idIndex),
                        cursor.getString(titleIndex),
                        cursor.getString(contentIndex),
                        cursor.getString(categoryIndex),
                        cursor.getString(reminderTimeIndex)
                );
                memoList.add(memo);
            }
        } else {
            Log.e("MemoDao", "getColumnIndex returned -1 for one or more columns. Please check the table structure and column names.");
        }
        cursor.close();
        return memoList;
    }

    // 根据ID删除备忘录记录
    public int deleteMemoById(int id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String selection = DatabaseHelper.COLUMN_ID + " =?";
        String[] selectionArgs = {String.valueOf(id)};
        int result = db.delete(DatabaseHelper.TABLE_MEMOS, selection, selectionArgs);
        if (result > 0) {
            Log.d("MemoDao", "Successfully deleted memo with id: " + id);
        } else {
            Log.e("MemoDao", "Failed to delete memo with id: " + id);
        }
        return result;
    }

    // 更新备忘录记录
    public int updateMemo(Memo memo) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Memo.COLUMN_TITLE, memo.getTitle());
        values.put(Memo.COLUMN_CONTENT, memo.getContent());
        values.put(Memo.COLUMN_CATEGORY, memo.getCategory());
        values.put(Memo.COLUMN_REMINDER_TIME, memo.getReminderTime());

        if (memo.getId() == 0) {
            Log.e("MemoDao", "Attempt to update memo with invalid ID");
            return 0;
        }

        String selection = Memo.COLUMN_ID + " =?";
        String[] selectionArgs = {String.valueOf(memo.getId())};
        int result = db.update(DatabaseHelper.TABLE_MEMOS, values, selection, selectionArgs);

        if (result > 0) {
            Log.d("MemoDao", "Successfully updated memo with id: " + memo.getId());
        } else {
            Log.e("MemoDao", "Failed to update memo with id: " + memo.getId());
        }
        return result;
    }

    // 根据标题模糊查询备忘录记录
    public List<Memo> searchMemosByTitle(String titleKeyword) {
        List<Memo> memoList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columns = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_TITLE,
                DatabaseHelper.COLUMN_CONTENT,
                DatabaseHelper.COLUMN_CATEGORY,
                DatabaseHelper.COLUMN_REMINDER_TIME
        };
        String selection = DatabaseHelper.COLUMN_TITLE + " LIKE?";
        String[] selectionArgs = {"%" + titleKeyword + "%"};
        Cursor cursor = db.query(DatabaseHelper.TABLE_MEMOS, columns, selection, selectionArgs, null, null, null);

        int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
        int titleIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE);
        int contentIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTENT);
        int categoryIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY);
        int reminderTimeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_REMINDER_TIME);

        if (idIndex >= 0 && titleIndex >= 0 && contentIndex >= 0 && categoryIndex >= 0 && reminderTimeIndex >= 0) {
            while (cursor.moveToNext()) {
                Memo memo = new Memo(
                        cursor.getInt(idIndex),
                        cursor.getString(titleIndex),
                        cursor.getString(contentIndex),
                        cursor.getString(categoryIndex),
                        cursor.getString(reminderTimeIndex)
                );
                memoList.add(memo);
            }
        } else {
            Log.e("MemoDao", "getColumnIndex returned -1 for one or more columns in searchMemosByTitle method.");
        }
        cursor.close();
        return memoList;
    }

    // 统计备忘录总数
    public int countAllMemos() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columns = {"COUNT(*)"};
        Cursor cursor = db.query(DatabaseHelper.TABLE_MEMOS, columns, null, null, null, null, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}
