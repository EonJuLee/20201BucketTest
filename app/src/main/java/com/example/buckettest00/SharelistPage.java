package com.example.buckettest00;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class SharelistPage extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences auto;
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private Button logout,mylist;
    private TextView textuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharelist_page);

        FirebaseUser user=mAuth.getCurrentUser();
        textuser=(TextView)findViewById(R.id.sharelist_text_user);
        textuser.setText(user.getDisplayName());

        mylist=(Button)findViewById(R.id.sharelist_btn_mypage);
        logout=(Button)findViewById(R.id.sharelist_btn_logout);
        logout.setOnClickListener(this);

        mylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SharelistPage.this,SharemylistActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateUI(){
        Intent intent=new Intent(SharelistPage.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void signOut(){
        auto =getSharedPreferences("autologin", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=auto.edit();
        editor.clear();
        editor.commit();
        mAuth.signOut();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.sharelist_btn_logout:
                signOut();
                updateUI();
                break;
        }
    }
}
