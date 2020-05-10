package com.example.buckettest00;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Mypage extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private DatabaseHelper myHelper;
    private RecyclerView rview;
    private BucketAdapter radapter;
    private List<BucketItem> items=new ArrayList<BucketItem>();
    private Animation fab_open,fab_close;
    private boolean isFabOpen=false;
    private FloatingActionButton fab,fab1,fab2;
    private Spinner spinner;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        rview=(RecyclerView)findViewById(R.id.mypage_rview);
        myHelper=new DatabaseHelper(Mypage.this);
        radapter=new BucketAdapter(Mypage.this,items);
        items.addAll(myHelper.viewItem());

        RecyclerView.LayoutManager rLayoutManager=new LinearLayoutManager(getApplicationContext());
        rview.setLayoutManager(rLayoutManager);
        rview.setItemAnimator(new DefaultItemAnimator());
        rview.setAdapter(radapter);

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
