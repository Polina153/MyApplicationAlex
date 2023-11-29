package com.example.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Note implements Parcelable {
    private final String title;
    private final String body;
    private final String date;
    private final boolean isImportant;

    public Note(String title, String body, String date, boolean isImportant) {
        this.title = title;
        this.body = body;
        this.date = date;
        this.isImportant = isImportant;
    }

    protected Note(Parcel in) {
        title = in.readString();
        body = in.readString();
        date = in.readString();
        isImportant = in.readByte() != 0;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getDate() {
        return date;
    }

    public boolean isImportant() {
        return isImportant;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(body);
        parcel.writeString(date);
        parcel.writeByte((byte) (isImportant ? 1 : 0));
    }
    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
