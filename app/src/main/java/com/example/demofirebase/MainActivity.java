package com.example.demofirebase;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


import com.example.demofirebase.database.DetailDB;
import com.example.demofirebase.model.DataModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter;
    private List<DataModel> itemList;
    private TextView txterror;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.list_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DetailDB.sharedDB(MainActivity.this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_list);
        txterror = (TextView) findViewById(R.id.txt_error);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<DataModel>();
        new DataAynk().execute();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        new DataAynk().execute();
    }

    public class DataAynk extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            itemList = DetailDB.sharedDB(MainActivity.this).getSaveList();
            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            setProgressBarIndeterminateVisibility(false);
            /* Download complete. Lets update UI */
            if (result == 1) {
                adapter = new MyRecyclerAdapter(MainActivity.this, itemList);
                mRecyclerView.setAdapter(adapter);
                adapter.setTextView(txterror);
                if (itemList.size() <= 0) {
                    mRecyclerView.setVisibility(View.GONE);
                    txterror.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    txterror.setVisibility(View.GONE);
                }
            } else {
                Log.e("fail", "Failed to fetch data!");
            }
        }
    }
}
