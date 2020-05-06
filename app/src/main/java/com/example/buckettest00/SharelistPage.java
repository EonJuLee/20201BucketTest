package com.example.buckettest00;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

public class SharelistPage extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences auto; // 로그아웃 시 정보 초기화
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private FirebaseDatabase mDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference mRef=mDatabase.getReference();
    private Button logout,mylist;
    private TextView textuser;
    private RecyclerView rview;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharelist_page);

        FirebaseUser user=mAuth.getCurrentUser();

        auto=getSharedPreferences("autologin", Activity.MODE_PRIVATE);
        userName = auto.getString("userName", null);

        // 사용자 이름 가져오기
        if(userName==null){
            findName(user.getEmail());
            SharedPreferences.Editor autoLogin = auto.edit();
            autoLogin.putString("userName", userName);
            autoLogin.commit();
        }

        textuser=(TextView)findViewById(R.id.sharelist_text_user);

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

    private void findName(final String email){
        final ProgressDialog pdialog=new ProgressDialog(this);
        pdialog.setTitle("정보를 불러오는 중입니다");
        pdialog.show();
        mRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    UserData userData = snapshot.getValue(UserData.class);
                    if (userData.getUseremail().equals(email)) {
                        userName =userData.getUsername();
                    }
                }
                textuser.setText(userName+"님 환영합니다");
                pdialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void signOut(){
        // 자동 로그인 해제
        SharedPreferences.Editor editor = auto.edit();
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
