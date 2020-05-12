package com.example.buckettest00;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AddDiaryActivity extends AppCompatActivity {

    private DatabaseHelper myHelper;
    private static final int GALLERY_CODE=10;
    private boolean pictureRead=false;

    private Button btnAdd,btnGallery;
    private EditText editText;
    private TextView textView;
    private ImageView img;
    private View mylayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        myHelper=new DatabaseHelper(AddDiaryActivity.this);

        btnAdd=(Button)findViewById(R.id.diary_btn_add);
        btnGallery=(Button)findViewById(R.id.diary_btn_gallery);
        editText=(EditText)findViewById(R.id.diary_edit_text);
        img=(ImageView)findViewById(R.id.diary_img_img);
        textView=(TextView)findViewById(R.id.diary_text_text);
        mylayout=findViewById(R.id.diary_linearlayout);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkP()){
                    requestP();
                }
                else{
                    loadGallery();
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 추가하고, 그 데이터 status 변화하고
                updateUI();
            }
        });
    }

    public void loadGallery(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==GALLERY_CODE){
            Uri uri=data.getData();
            Uri file=Uri.fromFile(new File(getPath(uri)));
            try{
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                img.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public boolean checkP(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            pictureRead=true;
        }
        return pictureRead;
    }

    public void requestP(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==GALLERY_CODE&&grantResults.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                pictureRead=true;
            }
        }
        if(pictureRead==true){
            loadGallery();
        }
        else{
            showSnackbar("권한 허용 후 사진 업로드가 가능합니다");
        }
    }

    public void showSnackbar(String contents){
        final Snackbar snackbar=Snackbar.make(mylayout,contents,Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("확인", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateUI();
    }

    public void updateUI(){
        Intent intent = new Intent(AddDiaryActivity.this,Mypage.class);
        startActivity(intent);
        finish();
    }
}
