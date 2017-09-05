package com.example.demofirebase;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.demofirebase.database.DetailDB;


/**
 * Created by yogesh.kamaliya on 27-Mar-15.
 */
public class DiaolgActivity extends Activity implements View.OnClickListener {
    String external = "", url = "";
    private TextView txtDetail;
    private Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtDetail = (TextView) findViewById(R.id.txt_save);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();


        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                external = intent.getStringExtra(Intent.EXTRA_TEXT);
                Log.e("text", "- " + external);
                url = getUrl();
                txtDetail.setMovementMethod(new ScrollingMovementMethod());
                txtDetail.setText(external);
//                txtDetail.setText(url + "\n\n" + external);
//                Linkify.addLinks(txtDetail, Linkify.ALL);
            } else {
                finish();
            }
        } else if ("com.google.android.gm.action.AUTO_SEND".equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                external = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (external != null) {
                    Log.e("google", "- " + external);
                }
            }
        }
    }

    private String getUrl() {

        Uri BOOKMARKS_URI = Uri.parse("content://browser/bookmarks");

        String[] HISTORY_PROJECTION = new String[]{
                "_id", // 0
                "url", // 1
                "visits", // 2
                "date", // 3
                "bookmark", // 4
                "title", // 5
                "favicon", // 6
                "thumbnail", // 7
                "touch_icon", // 8
                "user_entered", // 9
        };
        int HISTORY_PROJECTION_URL_INDEX = 1;
        String mSortOrder = "DATE";
        String selection = "bookmark" + " = 0";// 0 = history, // 1 = bookmark

        String title = "";
        String url = "";

        Cursor mCur = getContentResolver().query(BOOKMARKS_URI, HISTORY_PROJECTION, selection, null, mSortOrder);

        mCur.moveToFirst();
        if (mCur.moveToFirst() && mCur.getCount() > 0) {
            mCur.moveToLast();
            url = mCur.getString(HISTORY_PROJECTION_URL_INDEX);
            Log.e("urlname", "- " + url);
//            boolean cont = true;
//            while (mCur.isAfterLast() == false && cont) {
//                url = mCur.getString(HISTORY_PROJECTION_URL_INDEX);
//                Log.e("urlname", "- " + url);
//                mCur.moveToNext();
//            }
        }
        return url;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                if (external != null) {
                    new SaveAynk().execute();
                }
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    public class SaveAynk extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            DetailDB.sharedDB(DiaolgActivity.this).insertAlbumCategoryMapping(external, url);
            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            Intent intent = new Intent(DiaolgActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }
}


//    public static final String URL = "url";
//    public static final String VISITS = "visits";
//    public static final String DATE = "date";
//    public static final String BOOKMARK = "bookmark";
//    public static final String TITLE = "title";
//    public static final String CREATED = "created";
//    public static final String FAVICON = "favicon";
//
//    public static final String THUMBNAIL = "thumbnail";
//
//    public static final String TOUCH_ICON = "touch_icon";
//
//    public static final String USER_ENTERED = "user_entered";




