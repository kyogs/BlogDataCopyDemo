package com.example.demofirebase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.demofirebase.database.DetailDB;
import com.example.demofirebase.model.DataModel;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<FeedListRowHolder> {

    private List<DataModel> itemList;
    private Context mContext;
    TextView txterror;

    public MyRecyclerAdapter(Context context, List<DataModel> itemList) {
        this.itemList = itemList;
        this.mContext = context;
    }

    @Override
    public FeedListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);
        FeedListRowHolder feedListRowHolder = new FeedListRowHolder(v);
        return feedListRowHolder;
    }

    @Override
    public void onBindViewHolder(FeedListRowHolder feedListRowHolder, final int i) {
        feedListRowHolder.title.setText(itemList.get(i).getDetail());
        feedListRowHolder.txt_index.setText("(" + (i + 1) + ")");


        feedListRowHolder.relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("id", itemList.get(i).getRowid());
                mContext.startActivity(intent);
            }
        });

        feedListRowHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != itemList ? itemList.size() : 0);
    }

    private void showAlert(final int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Are you sure you want to delete ?")
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DetailDB.sharedDB(mContext).deleteItem(itemList.get(index).getRowid());
                        itemList.remove(index);
                        notifyDataSetChanged();
                        if (itemList.size() == 0) {
                            txterror.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void setTextView(TextView txterror) {
        this.txterror = txterror;
    }
}


class FeedListRowHolder extends RecyclerView.ViewHolder {
    protected TextView title, txt_index;
    protected RelativeLayout relMain;
    protected ImageButton btnDelete;

    public FeedListRowHolder(View view) {
        super(view);
        this.title = (TextView) view.findViewById(R.id.title);
        this.txt_index = (TextView) view.findViewById(R.id.index);
        this.relMain = (RelativeLayout) view.findViewById(R.id.main_rel);
        this.btnDelete = (ImageButton) view.findViewById(R.id.img_delete);
    }

}