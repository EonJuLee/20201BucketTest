package com.example.buckettest00;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        myHelper=new DatabaseHelper(Mypage.this);

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

        btnSearch=(Button)findViewById(R.id.mypage_btn_search);
        btnSearch.setOnClickListener(this);

        spinner=(Spinner)findViewById(R.id.mypage_spinner);
        spinner.setOnItemSelectedListener(this);

        loadCate();
    }

    public class BucketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mypage,null,false);
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
            if(!hash.equals("")){
                ((CustomViewHolder)holder).textHash.setVisibility(View.VISIBLE);
            }
            if(!detail.equals("")){
                ((CustomViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((CustomViewHolder)holder).textDetail.setVisibility(View.VISIBLE);
                    }
                });
            }
            if(status.equals("ING")){
                ((CustomViewHolder)holder).itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
                        MenuItem mEdit=menu.add(Menu.NONE,1001,1,"삭제");
                        MenuItem mDone=menu.add(Menu.NONE,1002,2,"완료");
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
                    }
                });
            }
            if(status.equals("완료")){
                ((CustomViewHolder)holder).itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
                        MenuItem mIng=menu.add(Menu.NONE,1003,1,"진행중");
                        mIng.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                BucketItem item=items.get(i);
                                item.setStatus("ING");
                                myHelper.updateItem(items.get(i).getId(),item);
                                notifyItemChanged(i);
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

            public CustomViewHolder(View view) {
                super(view);
                textTitle=view.findViewById(R.id.item_mypage_title);
                textStatus=view.findViewById(R.id.item_mypage_status);
                textDetail=view.findViewById(R.id.item_mypage_detail);
                textHash=view.findViewById(R.id.item_mypage_hash);
            }
        }
    }

    public String getHash(String hash1,String hash2){
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
        List<String> names=myHelper.getAllCate();
        names.add("선택 안함");
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setSelection(names.size());
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
                intent=new Intent(Mypage.this,AddDiaryActivity.class);
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
        String name = adapterView.getItemAtPosition(i).toString();
        if (!name.equals("선택 안함")) {
            Toast.makeText(adapterView.getContext(), name, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
