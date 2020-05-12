package com.example.buckettest00;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SharelistPage extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences auto; // 로그아웃 시 정보 초기화
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private FirebaseDatabase mDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference mRef=mDatabase.getReference();
    private FirebaseStorage mStorage=FirebaseStorage.getInstance();
    private List<DataItem> items=new ArrayList<DataItem>();
    private Button logout,mylist,mypage;
    private TextView textuser;
    private RecyclerView rview;
    private String userName;
    private AllDataAdapter radapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharelist_page);

        FirebaseUser user=mAuth.getCurrentUser();
        textuser=(TextView)findViewById(R.id.sharelist_text_user);

        auto=getSharedPreferences("autologin", Activity.MODE_PRIVATE);
        userName = auto.getString("userName", null);

        // 사용자 이름 가져오기
        if(userName==null){
            findName(user.getEmail());
        }
        else{
            textuser.setText(userName+"님 환영합니다");
        }

        mylist=(Button)findViewById(R.id.sharelist_btn_mypage);
        mypage=(Button)findViewById(R.id.sharelist_btn_tomypage);
        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SharelistPage.this,Mypage.class);
                startActivity(intent);
                finish();
            }
        });

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

        rview=(RecyclerView)findViewById(R.id.sharelist_rview);
        rview.setLayoutManager(new LinearLayoutManager(this));
        radapter=new AllDataAdapter();
        rview.setAdapter(radapter);

        mRef.child("Datas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    DataItem dataItem=snapshot.getValue(DataItem.class);
                    items.add(dataItem);
                    Log.e("Error","Error"+dataItem.toString());
                }
                radapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                SharedPreferences.Editor autoLogin = auto.edit();
                autoLogin.putString("userName", userName);
                autoLogin.commit();
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

    class AllDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sharepage,parent,false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            final DataItem dataItem=items.get(position);
            final String hashString="#"+dataItem.getHash1()+" #"+dataItem.getHash2()+" #"+dataItem.getHash3();
            ((CustomViewHolder)holder).share_title.setText(dataItem.getTitle());
            ((CustomViewHolder)holder).share_hash.setText(hashString);
            Glide.with(SharelistPage.this).load(dataItem.getPicture()).into(((CustomViewHolder) holder).share_img);
            ((CustomViewHolder)holder).share_img.setVisibility(View.VISIBLE);
            Log.e("Error","Error"+dataItem.toString());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            private TextView share_title;
            private TextView share_hash;
            private ImageView share_img;

            public CustomViewHolder(View view) {
                super(view);
                share_title=(TextView)view.findViewById(R.id.item_sharepage_title);
                share_hash=(TextView)view.findViewById(R.id.item_sharepage_hash);
                share_img=(ImageView)view.findViewById(R.id.item_sharepage_img);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SharelistPage.this,Mypage.class);
        startActivity(intent);
        finish();
    }
}
