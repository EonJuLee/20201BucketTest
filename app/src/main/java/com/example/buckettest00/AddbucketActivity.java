package com.example.buckettest00;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class AddbucketActivity extends AppCompatActivity {

    private EditText editTitle,editDetail,editWho,editWhy;
    private TextView textMessage,textGuide1,textGuide2;
    private Button btnAdd,btnHelp;
    private DatabaseHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbucket);

        myHelper=new DatabaseHelper(AddbucketActivity.this);

        textGuide1=(TextView)findViewById(R.id.add_text_guide1);
        textGuide2=(TextView)findViewById(R.id.add_text_guide2);
        textMessage=(TextView)findViewById(R.id.add_text_message);

        editWhy=(EditText)findViewById(R.id.add_edit_detailwhy);
        editWho=(EditText) findViewById(R.id.add_edit_detailwho);
        editTitle=(EditText)findViewById(R.id.add_edit_title);
        editDetail=(EditText)findViewById(R.id.add_edit_detail);

        btnAdd=(Button)findViewById(R.id.add_btn_add);
        btnHelp=(Button)findViewById(R.id.add_btn_help);

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnHelp.setVisibility(View.GONE);
                textGuide1.setVisibility(View.VISIBLE);
                textGuide2.setVisibility(View.VISIBLE);
                editWho.setVisibility(View.VISIBLE);
                editWhy.setVisibility(View.VISIBLE);
                showToast("꼭 작성해야 하는 건 아니야");
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contents="";
                String title=editTitle.getText().toString().trim();
                String detail=editDetail.getText().toString().trim();
                String who=editWho.getText().toString().trim();
                String why=editWhy.getText().toString().trim();
                if(title.equals("")){
                    showMessage("버킷리스트를 입력하세요");
                }
                else{
                    contents="☆"+editWho+"\n"+"☆"+editWhy+"\n"+detail;
                    addData(title,contents);
                }
            }
        });
    }

    public void addData(String title,String detail){
        BucketItem item=new BucketItem(title,detail);
        boolean isAdded=myHelper.addItem(item);
        if(isAdded){
            showToast("버킷리스트 등록 성공");
            updateUI();
        }
        else{
            showToast("버킷리스트 등록 실패");
        }
    }

    public void showMessage(String contents){
        textMessage.setText(contents);
    }

    public void showToast(String contents){
        Toast.makeText(AddbucketActivity.this,contents,Toast.LENGTH_SHORT).show();
    }

    public void updateUI(){
        Intent intent = new Intent(AddbucketActivity.this,Mypage.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateUI();
    }
}
