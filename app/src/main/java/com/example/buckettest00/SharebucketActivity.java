package com.example.buckettest00;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SharebucketActivity extends AppCompatActivity {

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseDatabase mDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference mRef=mDatabase.getReference();
    private Button share;
    private EditText edith1,edith2,edith3,contents;
    private TextView texttitle,textmessage;
    private String username="";
    private String title="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharebucket);

        share=(Button)findViewById(R.id.sbucket_btn_share);
        edith1=(EditText)findViewById(R.id.sbucket_edit_hash1);
        edith2=(EditText)findViewById(R.id.sbucket_edit_hash2);
        edith3=(EditText)findViewById(R.id.sbucket_edit_hash3);
        contents=(EditText)findViewById(R.id.sbucket_edit_contents);
        texttitle=(TextView)findViewById(R.id.sbucket_text_title);
        textmessage=(TextView)findViewById(R.id.sbucket_text_message);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hash1=edith1.getText().toString().trim();
                String hash2=edith2.getText().toString().trim();
                String hash3=edith3.getText().toString().trim();
                String detail=contents.getText().toString().trim();
                if(detail.equals("")){
                    showMessage("버킷리스트를 공유하려면 추천하는 이유가 있어야 합니다");
                }
                else{
                    DataItem data=new DataItem(title,hash1,hash2,hash3,detail,username);

                }
            }
        });
    }

    public void addData(DataItem data){
        mRef.child("Datas").push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    showToast("업로드 완료");
                    updateUI();
                }
                else{
                    showToast("업로드 실패");
                }
            }
        });
    }

    public void showToast(String strings){
        Toast.makeText(SharebucketActivity.this,strings,Toast.LENGTH_SHORT).show();
    }

    public void showMessage(String strings){
        textmessage.setText(strings);
    }

    public void updateUI(){
        Intent intent=new Intent(SharebucketActivity.this,SharelistPage.class);
        startActivity(intent);
        finish();
    }
}
