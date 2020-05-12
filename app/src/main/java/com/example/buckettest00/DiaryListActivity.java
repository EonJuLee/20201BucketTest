package com.example.buckettest00;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DiaryListActivity extends AppCompatActivity {

    private DatabaseHelper myHelper;
    private RecyclerView rview;
    private DiaryAdapter dadapter;
    private List<DiaryItem> items=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_list);

        myHelper=new DatabaseHelper(this);
        items.addAll(myHelper.viewEntry());
        Log.e("Error","Error"+items.size());

        rview=(RecyclerView)findViewById(R.id.diarylist_rview);
        rview.setLayoutManager(new GridLayoutManager(this,3));

        dadapter=new DiaryAdapter();
        rview.setAdapter(dadapter);
        dadapter.notifyDataSetChanged();
    }

    private class DiaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diary,parent,false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
            DiaryItem item=items.get(i);
            ((CustomViewHolder)holder).textTitle.setText(item.getTitle());
            ((CustomViewHolder)holder).textDetail.setText(item.getTitle());
            // ((CustomViewHolder)holder).textDetail.setVisibility(View.VISIBLE);
            File imgFile=new File(item.getImage());
            if(imgFile.exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                //Matrix matrix1 = new Matrix() ;
                //matrix1.postScale(2.0f, 2.0f);
                ((CustomViewHolder)holder).img.setImageBitmap(bitmap);
                //((CustomViewHolder)holder).img.setImageMatrix(matrix1);
            }
            else{
                ((CustomViewHolder)holder).img.setImageDrawable(getResources().getDrawable(R.drawable.fbimg));
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            private TextView textTitle;
            private TextView textDetail;
            private ImageView img;

            public CustomViewHolder(View view) {
                super(view);
                textDetail=(TextView)view.findViewById(R.id.item_diary_detail);
                textTitle=(TextView)view.findViewById(R.id.item_diary_title);
                img=(ImageView)view.findViewById(R.id.item_diary_img);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateUI();
    }

    public void updateUI(){
        Intent intent=new Intent(DiaryListActivity.this,Mypage.class);
        startActivity(intent);
        finish();
    }
}
