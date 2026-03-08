package com.example.xiangmuke.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Memo implements Parcelable {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_REMINDER_TIME = "reminder_time";

    private int id;
    private String title;
    private String content;
    private String category;
    private String reminderTime;

    public Memo(int id, String title, String content, String category, String reminderTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.reminderTime = reminderTime;
    }

    public Memo(String title, String content, String category, String reminderTime) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.reminderTime = reminderTime;
    }

    protected Memo(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        category = in.readString();
        reminderTime = in.readString();
    }

    public static final Parcelable.Creator<Memo> CREATOR = new Parcelable.Creator<Memo>() {
        @Override
        public Memo createFromParcel(Parcel in) {
            return new Memo(in);
        }

        @Override
        public Memo[] newArray(int size) {
            return new Memo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(category);
        dest.writeString(reminderTime);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }
}