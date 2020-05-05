package com.example.buckettest00;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mypage,sharepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mypage=(Button)findViewById(R.id.main_btn_mypage);
        sharepage=(Button)findViewById(R.id.main_btn_sharepage);

        mypage.setOnClickListener(this);
        sharepage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()){
            case R.id.main_btn_mypage:
                intent=new Intent(MainActivity.this,Mypage.class);
                break;
            case R.id.main_btn_sharepage:
                intent=new Intent(MainActivity.this,SharePage.class);
                break;
        }
        startActivity(intent);
        finish();
    }
}
