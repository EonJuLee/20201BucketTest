package com.example.buckettest00;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SharemylistActivity extends AppCompatActivity {

    private DatabaseHelper myHelper;
    private RecyclerView rview;
    private AllListAdapter radapter;
    private List<BucketItem> items=new ArrayList<BucketItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharemylist);

        myHelper=new DatabaseHelper(this);
        items.addAll(myHelper.viewItem());

        rview=(RecyclerView)findViewById(R.id.sharemylist_rview);
        rview.setLayoutManager(new LinearLayoutManager(this));
        radapter=new AllListAdapter();

        rview.setAdapter(radapter);
    }

    class AllListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mypage,parent,false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            final BucketItem bucketItem = items.get(position);
            ((CustomViewHolder)holder).bucket_title.setText(bucketItem.getTitle());
            ((CustomViewHolder)holder).bucket_status.setVisibility(View.GONE);
            ((CustomViewHolder)holder).bucket_detail.setVisibility(View.GONE);
            ((CustomViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(SharemylistActivity.this,SharebucketActivity.class);
                    intent.putExtra("titleInformation",bucketItem.getTitle());
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            private TextView bucket_title;
            private TextView bucket_status;
            private TextView bucket_detail;

            public CustomViewHolder(View view) {
                super(view);
                bucket_title=(TextView)view.findViewById(R.id.item_mypage_title);
                bucket_status=(TextView)view.findViewById(R.id.item_mypage_status);
                bucket_detail=(TextView)view.findViewById(R.id.item_mypage_detail);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,SharelistPage.class);
        startActivity(intent);
        finish();
    }
}
