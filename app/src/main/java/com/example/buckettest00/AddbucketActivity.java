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

    private EditText editTitle,editDetail;
    private TextView textMessage;
    private Button btnAdd;
    private DatabaseHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbucket);

        myHelper=new DatabaseHelper(AddbucketActivity.this);

        textMessage=(TextView)findViewById(R.id.add_text_message);
        editTitle=(EditText)findViewById(R.id.add_edit_title);
        editDetail=(EditText)findViewById(R.id.add_edit_detail);
        btnAdd=(Button)findViewById(R.id.add_btn_add);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=editTitle.getText().toString().trim();
                String detail=editDetail.getText().toString().trim();
                if(title.equals("")){
                    showMessage("버킷리스트를 입력하세요");
                }
                else{
                    addData(title,detail);
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
