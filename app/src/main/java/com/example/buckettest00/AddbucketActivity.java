package com.example.buckettest00;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class AddbucketActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText editTitle,editDetail,editWho,editWhy; // 제목, 디테일 (1,2)
    private TextView textMessage,textGuide1,textGuide2; // 오류메세지, 가이드
    private Button btnAdd,btnHelp,btnAddCate;
    private Spinner spinner;
    private String cate="분류 안함";
    private View thislayout;

    private DatabaseHelper myHelper;

    private  List<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbucket);

        myHelper=new DatabaseHelper(AddbucketActivity.this);

        thislayout=findViewById(R.id.addbucket_layout);
        textGuide1=(TextView)findViewById(R.id.add_text_guide1);
        textGuide2=(TextView)findViewById(R.id.add_text_guide2);
        textMessage=(TextView)findViewById(R.id.add_text_message);

        editWhy=(EditText)findViewById(R.id.add_edit_detailwhy);
        editWho=(EditText) findViewById(R.id.add_edit_detailwho);
        editTitle=(EditText)findViewById(R.id.add_edit_title);
        editDetail=(EditText)findViewById(R.id.add_edit_detail);

        btnAdd=(Button)findViewById(R.id.add_btn_add);
        btnHelp=(Button)findViewById(R.id.add_btn_help);
        btnAddCate=(Button)findViewById(R.id.add_btn_cateadd);

        spinner=(Spinner)findViewById(R.id.add_spinner);
        spinner.setOnItemSelectedListener(this); // implement 해주고
        loadCate();

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnHelp.setVisibility(View.GONE);
                textGuide1.setVisibility(View.VISIBLE);
                textGuide2.setVisibility(View.VISIBLE);
                editWho.setVisibility(View.VISIBLE);
                editWhy.setVisibility(View.VISIBLE);
                showSnackbar("선택 항목입니다\n간단하게 입력하세요");
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=editTitle.getText().toString().trim();
                String detail=editDetail.getText().toString().trim();
                String who=editWho.getText().toString().trim();
                String why=editWhy.getText().toString().trim();
                if(title.equals("")){
                    showSnackbar("버킷리스트를 입력하세요");
                }
                else if(cate.equals("분류 안함")){
                    showSnackbar("분류를 선택하세요");
                }
                else {
                    BucketItem newItem = new BucketItem(title, detail, cate);
                    newItem.setHash1(who);
                    newItem.setHash2(why);
                    addData(newItem);
                }
            }
        });

        btnAddCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(AddbucketActivity.this);
                View newView= LayoutInflater.from(AddbucketActivity.this).inflate(R.layout.edit_spinner,null,false);
                builder.setView(newView);
                final Button btnAdd=(Button)newView.findViewById(R.id.editspinner_btnAdd);
                final Button btnCancel=(Button)newView.findViewById(R.id.editspinner_btnCancel);
                final EditText editCate=(EditText)newView.findViewById(R.id.editspinner_editCate);
                final AlertDialog dialog=builder.create();
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newCate=editCate.getText().toString().trim();
                        if(newCate.equals("")){
                            showToast("새 카테고리를 입력하세요");
                        }
                        else{
                            boolean result=myHelper.addCate(newCate);
                            dialog.dismiss();
                            if(result==true){
                                showToast("새 카테고리 등록 성공");
                                loadCate();
                                // 이거 하면 오류
                                // spinner.setSelection(names.size());
                            }
                            else{
                                showToast("새 카테고리 등록 실패");
                            }
                        }
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    public void loadCate(){
        names=myHelper.getAllCate();
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void addData(BucketItem item){
        Log.e("Error","Error "+item.getHash1());
        Log.e("Error","Error "+item.getHash2());
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

    public void showSnackbar(String contents){
        final Snackbar snackbar=Snackbar.make(thislayout,contents,Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("확인", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
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

    // 스피너 구현
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        cate = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
