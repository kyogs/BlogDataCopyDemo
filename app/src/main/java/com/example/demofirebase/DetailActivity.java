package com.example.demofirebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.demofirebase.database.DetailDB;


/**
 * Created by yogesh.kamaliya on 27-Mar-15.
 */
public class DetailActivity extends AppCompatActivity {

    private TextView txtDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtDetail = (TextView) findViewById(R.id.txt_detail);
        txtDetail.setText(DetailDB.sharedDB(DetailActivity.this).getDetail(getIntent().getStringExtra("id")));
        txtDetail.setMovementMethod(new ScrollingMovementMethod());
        Linkify.addLinks(txtDetail, Linkify.ALL);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
