package com.example.buckettest00;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BucketAdapter extends RecyclerView.Adapter<BucketAdapter.MyViewHolder> {

    private Context context;
    private List<BucketItem> items;
    private DatabaseHelper myHelper;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView bucket_title;
        public TextView bucket_status;
        public TextView bucket_detail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bucket_title=itemView.findViewById(R.id.item_mypage_title);
            bucket_status=itemView.findViewById(R.id.item_mypage_status);
            bucket_detail=itemView.findViewById(R.id.item_mypage_detail);
        }
    }

    public BucketAdapter(Context context, List<BucketItem> items) {
        this.context = context;
        this.items = items;
        myHelper=new DatabaseHelper(context);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View pview= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mypage,parent,false);
        return new MyViewHolder(pview);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final BucketItem bucketItem = items.get(position);
        holder.bucket_title.setText(bucketItem.getTitle());
        holder.bucket_status.setText(bucketItem.getStatus());
        holder.bucket_detail.setText(bucketItem.getDetail());
        holder.bucket_detail.setVisibility(View.GONE);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(myHelper.deleteItem(bucketItem.getId())){
                    items.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context,"삭제 성공",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context,"삭제 실패",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.bucket_detail.setVisibility(View.VISIBLE);
            }
        });
        holder.bucket_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.bucket_detail.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
