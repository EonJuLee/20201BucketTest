package com.example.buckettest00;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SharebucketActivity extends AppCompatActivity {

    private static final int GALLERY_CODE=10;
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://hiaver2.appspot.com");
    private StorageReference sRef=storage.getReference();
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseDatabase mDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference mRef=mDatabase.getReference();
    private Button share,choose;
    private EditText edith1,edith2,edith3,contents;
    private ImageView imgView;
    private TextView texttitle,textmessage;
    private String username="";
    private String title="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharebucket);

        imgView=(ImageView)findViewById(R.id.sbucket_img_img);
        choose=(Button)findViewById(R.id.sbucket_btn_choose);
        share=(Button)findViewById(R.id.sbucket_btn_share);
        edith1=(EditText)findViewById(R.id.sbucket_edit_hash1);
        edith2=(EditText)findViewById(R.id.sbucket_edit_hash2);
        edith3=(EditText)findViewById(R.id.sbucket_edit_hash3);
        contents=(EditText)findViewById(R.id.sbucket_edit_contents);
        texttitle=(TextView)findViewById(R.id.sbucket_text_title);
        textmessage=(TextView)findViewById(R.id.sbucket_text_message);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }

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

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,GALLERY_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        if(requestCode==GALLERY_CODE){
            String path=getPath(data.getData());
            Uri file=Uri.fromFile(new File(path));
            try{
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                imgView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            sRef.child("images/"+file.getLastPathSegment()).putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    showToast("사진을 추가했습니다");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showToast("사진을 추가하지 못했습니다");
                }
            });

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getPath(Uri uri){
        String [] proj={MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);

        Cursor cursor= cursorLoader.loadInBackground();
        int index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();
        return cursor.getString(index);
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
