package com.example.demofirebase.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.demofirebase.model.DataModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static final String DATABASE_PATH = "/data/data/com.example.demofirebase/databases/";
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "mydata.sqlite";
    private static String TABLE_NAME = "detail_list";
    public static String TEXT_DETAIL = "text_detail";
    public static String TEXT_URL = "url";
    private static String ID = "id";
    private SQLiteDatabase myDataBase;

    private Context mContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * This method will create database in application package /databases
     * directory when first time application launched
     */
    public void createDataBase() throws IOException {
        boolean mDataBaseExist = checkDataBase();
        if (!mDataBaseExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException mIOException) {
                mIOException.printStackTrace();
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * This method checks whether database is exists or not *
     */
    private boolean checkDataBase() {
        try {
            final String mPath = DATABASE_PATH + DATABASE_NAME;
            final File file = new File(mPath);
            if (file.exists())
                return true;
            else
                return false;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method will copy database from /assets directory to application
     * package /databases directory
     */
    private void copyDataBase() throws IOException {
        try {
            InputStream mInputStream = mContext.getAssets().open(DATABASE_NAME);
            String outFileName = DATABASE_PATH + DATABASE_NAME;
            OutputStream mOutputStream = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = mInputStream.read(buffer)) > 0) {
                mOutputStream.write(buffer, 0, length);
            }
            mOutputStream.flush();
            mOutputStream.close();
            mInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method open database for operations *
     */
    public boolean openDataBase() throws SQLException {
        String mPath = DATABASE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READWRITE);
        return myDataBase.isOpen();
    }

    /**
     * This method close database connection and released occupied memory *
     */
    @Override
    public void close() {
        if (myDataBase != null)
            myDataBase.close();
        SQLiteDatabase.releaseMemory();
        super.close();
    }

    public boolean isOpen() {
        if (myDataBase != null)
            return myDataBase.isOpen();
        return false;
    }

    public synchronized void execNonQuery(String sql) {
        try {
            myDataBase.execSQL(sql);
        } catch (Exception e) {
            Log.e("Err", e.getMessage());
        } finally {
            // closeDb();
        }
    }

    public void insertAlbumCategoryMapping(String saveText, String url) {
        synchronized (this) {
            try {
                myDataBase.beginTransaction();
                ContentValues values = new ContentValues();
                values.put(TEXT_DETAIL, saveText);
                values.put(TEXT_URL, url);
                myDataBase.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                myDataBase.setTransactionSuccessful();
            } catch (SQLException e) {
            } finally {
                myDataBase.endTransaction();
            }
        }
    }


    public ArrayList<DataModel> getSaveList() {
        myDataBase = this.getReadableDatabase();
        Cursor cursor = null;

        ArrayList<DataModel> arrayList = new ArrayList<DataModel>();
        try {
            String query;
            query = "SELECT * FROM detail_list";

            cursor = myDataBase.rawQuery(query, null);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    DataModel albumModel = new DataModel(cursor);
                    arrayList.add(albumModel);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return arrayList;
    }

    public String getDetail(String id) {
        String strDetail = "";
        myDataBase = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String query;
            query = "SELECT * FROM detail_list where id=" + id;

            cursor = myDataBase.rawQuery(query, null);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    strDetail = cursor.getString(cursor.getColumnIndex(TEXT_URL)).toString().trim();
                    strDetail = strDetail + "\n\n" + cursor.getString(cursor.getColumnIndex(TEXT_DETAIL)).toString().trim();
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return strDetail;
    }


    public void deleteItem(String id) {
        myDataBase = this.getReadableDatabase();
        try {
            myDataBase.delete(TABLE_NAME, "id=" + id, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}