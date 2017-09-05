package com.example.demofirebase.model;

import android.database.Cursor;

import com.example.demofirebase.database.DatabaseHelper;

/**
 * Created by yogesh.kamaliya on 27-Mar-15.
 */
public class DataModel {

    String rowid, detail, url;

    public DataModel(Cursor cursor) {
        setRowid(String.valueOf(cursor.getInt(cursor.getColumnIndex("id"))));
        setDetail(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TEXT_DETAIL)).toString().trim());
        setUrl(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TEXT_URL)).toString().trim());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getRowid() {
        return rowid;
    }

    public void setRowid(String rowid) {
        this.rowid = rowid;
    }
}
