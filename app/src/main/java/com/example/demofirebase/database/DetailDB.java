package com.example.demofirebase.database;

import java.io.IOException;

import android.content.Context;

public class DetailDB extends DatabaseHelper {

    private static DetailDB mInstance;

    private DetailDB(Context context) {
        super(context);
    }

    public static DetailDB sharedDB(Context context) {
        if (mInstance == null)
            if (context != null)
                mInstance = new DetailDB(context);
        if (!mInstance.isOpen()) {
            try {
                mInstance.createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mInstance.openDataBase();
        }
        return mInstance;
    }

    @Override
    public synchronized void close() {
        super.close();
        mInstance = null;
    }
}