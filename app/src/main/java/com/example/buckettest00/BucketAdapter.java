package com.example.buckettest00;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BucketAdapter extends RecyclerView.Adapter<BucketAdapter.MyViewHolder>{

    private Context context;
    private List<BucketItem> items;
    private DatabaseHelper myHelper;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView bucket_title;
        public TextView bucket_status;
        public TextView bucket_detail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bucket_title=itemView.findViewById(R.id.item_mypage_title);
            bucket_status=itemView.findViewById(R.id.item_mypage_status);
            bucket_detail=itemView.findViewById(R.id.item_mypage_detail);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem mEdit=menu.add(Menu.NONE,1001,1,"편집");
            MenuItem mDelete=menu.add(Menu.NONE,1002,2,"삭제");
            MenuItem mDone=menu.add(Menu.NONE,1003,3,"완료");

            mEdit.setOnMenuItemClickListener(onEditMenu);
            mDelete.setOnMenuItemClickListener(onEditMenu);
            mDone.setOnMenuItemClickListener(onEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu=new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1001:
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        View view = LayoutInflater.from(context).inflate(R.layout.edit_box, null, false);
                        builder.setView(view);
                        final Button btnSubmit = (Button) view.findViewById(R.id.editbox_btn);
                        final EditText editTitle = (EditText) view.findViewById(R.id.editbox_title);
                        final EditText editDetail = (EditText) view.findViewById(R.id.editbox_detail);
                        final EditText editHash1 = (EditText) view.findViewById(R.id.editbox_hash1);
                        final EditText editHash2 = (EditText) view.findViewById(R.id.editbox_hash2);

                        editTitle.setText(items.get(getAdapterPosition()).getTitle());
                        editDetail.setText(items.get(getAdapterPosition()).getDetail());
                        // editHash1.setText(items.get(getAdapterPosition()).get);

                        final AlertDialog dialog = builder.create();
                        btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String title = editTitle.getText().toString().trim();
                                String detail = editDetail.getText().toString().trim();
                                BucketItem newItem = new BucketItem(title, detail);
                                items.set(getAdapterPosition(), newItem);
                                notifyItemChanged(getAdapterPosition());
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        break;

                    case 1002:
                        myHelper.deleteItem(items.get(getAdapterPosition()).getId());
                        items.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(),items.size());
                        break;

                    case 1003:
                        items.get(getAdapterPosition()).setStatus("완료");
                        notifyItemChanged(getAdapterPosition());
                        break;
                }
                return true;
            }
        };
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
        /*
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
        */
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
