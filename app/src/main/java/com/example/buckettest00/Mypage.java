package com.example.buckettest00;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.media.MediaActionSound;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mypage extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnCreateContextMenuListener {

    private DatabaseHelper myHelper;

    private RecyclerView rview;
    private BucketAdapter radapter;
    private List<BucketItem> items=new ArrayList<BucketItem>();

    private boolean isFabOpen=false;
    private Animation fab_open,fab_close;
    private FloatingActionButton fab,fab1,fab2;

    private Spinner spinner;
    private Button btnSearch;
    private ImageView btnShare;
    private View mylayout;

    private String selected="모두";
    private List<String> names=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        myHelper=new DatabaseHelper(Mypage.this);
        mylayout=findViewById(R.id.mypage_framelayout);

        rview=(RecyclerView)findViewById(R.id.mypage_rview);
        rview.setLayoutManager(new LinearLayoutManager(Mypage.this));
        rview.setItemAnimator(new DefaultItemAnimator());

        radapter=new BucketAdapter();
        rview.setAdapter(radapter);
        radapter.notifyDataSetChanged();

        items.addAll(myHelper.viewItem());

        fab_open= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fab_close=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);

        fab=(FloatingActionButton)findViewById(R.id.mypage_fab);
        fab1 = (FloatingActionButton) findViewById(R.id.mypage_fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.mypage_fab2);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

        spinner=(Spinner)findViewById(R.id.mypage_spinner);

        loadCate();
        spinner.setSelection(names.size()-1,false);
        spinner.setOnItemSelectedListener(this);


        btnShare=(ImageView)findViewById(R.id.mypage_btn_sharepage);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Mypage.this,SharePage.class);
                startActivity(intent);
                finish();
            }
        });

        btnSearch=(Button)findViewById(R.id.mypage_btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!selected.equals("모두")) {
                    items.clear();
                    items.addAll(myHelper.selectItem(selected));
                    radapter.notifyDataSetChanged();
                }
                else{
                    items.clear();
                    items.addAll(myHelper.viewItem());
                    radapter.notifyDataSetChanged();
                }
            }
        });
    }

    public class BucketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mypage,parent,false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {
            String title=items.get(i).getTitle();
            final String detail=items.get(i).getDetail();
            String hash1=items.get(i).getHash1();
            String hash2=items.get(i).getHash2();
            String status=items.get(i).getStatus();
            String cgory=items.get(i).getCgory(); // 쓰이지 않는 부분
            String hash=getHash(hash1,hash2);
            ((CustomViewHolder)holder).textTitle.setText(title);
            ((CustomViewHolder)holder).textStatus.setText(status);
            ((CustomViewHolder)holder).textDetail.setText(detail);
            ((CustomViewHolder)holder).textHash.setText(hash);
            ((CustomViewHolder)holder).textNotshow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((CustomViewHolder)holder).textDetail.setVisibility(View.GONE);
                    ((CustomViewHolder)holder).textNotshow.setVisibility(View.GONE);
                }
            });
            if(!hash.equals("")){
                ((CustomViewHolder)holder).textHash.setVisibility(View.VISIBLE);
            }
            if(!detail.equals("")){
                ((CustomViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((CustomViewHolder)holder).textDetail.setVisibility(View.VISIBLE);
                        ((CustomViewHolder)holder).textNotshow.setVisibility(View.VISIBLE);
                    }
                });
            }
            if(status.equals("인증")){
                ((CustomViewHolder)holder).imgDone.setVisibility(View.VISIBLE);
                ((CustomViewHolder)holder).itemView.setBackground(ContextCompat.getDrawable(Mypage.this,R.drawable.round_color_done_rectangle));
            }
            if(status.equals("진행중")){
                ((CustomViewHolder)holder).imgDone.setVisibility(View.GONE);
                ((CustomViewHolder)holder).itemView.setBackground(ContextCompat.getDrawable(Mypage.this,R.drawable.round_rectangle));
                ((CustomViewHolder)holder).itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
                        MenuItem mEdit=menu.add(Menu.NONE,1001,1,"삭제");
                        MenuItem mDone=menu.add(Menu.NONE,1002,2,"완료");
                        MenuItem mVerify=menu.add(Menu.NONE,1000,3,"인증");
                        mEdit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                myHelper.deleteItem(items.get(i).getId());
                                items.remove(i);
                                notifyItemRemoved(i);
                                notifyItemRangeChanged(i,items.size());
                                return true;
                            }
                        });
                        mDone.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                BucketItem item=items.get(i);
                                item.setStatus("완료");
                                myHelper.updateItem(items.get(i).getId(),item);
                                notifyItemChanged(i);
                                return true;
                            }
                        });
                        mVerify.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                Intent intent = new Intent(Mypage.this,AddDiaryActivity.class);
                                intent.putExtra("BucketInfo",items.get(i));
                                startActivity(intent);
                                finish();
                                return true;
                            }
                        });
                    }
                });
            }
            if(status.equals("완료")){
                ((CustomViewHolder)holder).imgDone.setVisibility(View.GONE);
                ((CustomViewHolder)holder).itemView.setBackground(ContextCompat.getDrawable(Mypage.this,R.drawable.round_color_done_rectangle));
                ((CustomViewHolder)holder).itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
                        MenuItem mIng=menu.add(Menu.NONE,1003,1,"인증");
                        MenuItem mEdit=menu.add(Menu.NONE,1004,1,"삭제");
                        mIng.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                Intent intent = new Intent(Mypage.this,AddDiaryActivity.class);
                                intent.putExtra("BucketInfo",items.get(i));
                                startActivity(intent);
                                finish();
                                return true;
                            }
                        });
                        mEdit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                myHelper.deleteItem(items.get(i).getId());
                                items.remove(i);
                                notifyItemRemoved(i);
                                notifyItemRangeChanged(i,items.size());
                                return true;
                            }
                        });
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            private TextView textTitle;
            private TextView textHash;
            private TextView textStatus;
            private TextView textDetail;
            private TextView textNotshow;
            private ImageView imgDone;

            public CustomViewHolder(View view) {
                super(view);
                imgDone=view.findViewById(R.id.item_mypage_img);
                textTitle=view.findViewById(R.id.item_mypage_title);
                textStatus=view.findViewById(R.id.item_mypage_status);
                textDetail=view.findViewById(R.id.item_mypage_detail);
                textHash=view.findViewById(R.id.item_mypage_hash);
                textNotshow=view.findViewById(R.id.item_mypage_notshow);
            }
        }
    }

    public String getHash(String hash1, String hash2){
        String hash="";
        if(!hash1.equals("")){
            hash+="#"+hash1+" ";
        }
        if(!hash2.equals("")){
            hash+="#"+hash2;
        }
        return hash;
    }


    public void loadCate(){
        names=myHelper.getAllCate();
        if(!names.contains("모두")) {
            names.add("모두");
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.item_spinner,names);
        adapter.setDropDownViewResource(R.layout.item_dropdown);
        spinner.setAdapter(adapter);
    }


    @Override
    public void onClick(View view) {
        Intent intent=null;
        switch(view.getId()){
            case R.id.mypage_btn_search:
                break;
            case R.id.mypage_fab:
                anim();
                break;
            case R.id.mypage_fab1:
                anim();
                intent=new Intent(Mypage.this,DiaryListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.mypage_fab2:
                anim();
                intent=new Intent(Mypage.this,AddbucketActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void anim(){
        if(isFabOpen){
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen=false;
        }
        else{
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen=true;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.e("Error","Error "+"itemselected안");
        selected= adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.e("Error","Error "+"nothingselected안");
    }

    public void showSnackbar(String contents){
        final Snackbar snackbar=Snackbar.make(mylayout,contents,Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("확인", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}