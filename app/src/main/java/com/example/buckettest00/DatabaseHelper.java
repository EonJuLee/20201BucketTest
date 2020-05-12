package com.example.buckettest00;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="MEMO";
    private static final String TABLE_BUCKET="BUCKETLIST";
    private static final String TABLE_CATEGORY="CATEGORYLIST";
    private static final String TABLE_DIARY="DIARYLIST";
    private static final String DIARY_COL1="ID";
    private static final String DIARY_COL2="TITLE";
    private static final String DIARY_COL3="PICTURE";
    private static final String DIARY_COL4="DETAIL";
    private static final String BUCKET_COL_1="ID";
    private static final String BUCKET_COL_2="TITLE";
    private static final String BUCKET_COL_3="STATUS";
    private static final String BUCKET_COL_4="DETAIL";
    private static final String BUCKET_COL_5="CAT";
    private static final String BUCKET_COL_6="HASH1";
    private static final String BUCKET_COL_7="HASH2";
    private static final String CATEGORY_COL_1="ID";
    private static final String CATEGORY_COL_2="CAT";
    private static final int VERSION=1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 일기 테이블도 필요해보임
        db.execSQL("CREATE TABLE "+TABLE_BUCKET+"(ID INTEGER PRIMARY KEY,TITLE TEXT,STATUS TEXT,DETAIL TEXT,CAT TEXT,HASH1 TEXT,HASH2 TEXT)");
        db.execSQL("CREATE TABLE "+TABLE_CATEGORY+"(ID INTEGER PRIMARY KEY,CAT TEXT)");
        db.execSQL("CREATE TABLE "+TABLE_DIARY+"(ID INTEGER PRIMARY KEY,TITLE TEXT,PICTURE TEXT,DETAIL TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_BUCKET);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_DIARY);
        onCreate(db);
    }

    public boolean addEntry(DiaryItem item){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DIARY_COL2,item.getTitle());
        values.put(DIARY_COL4,item.getDetail());
        values.put(DIARY_COL3,item.getImage());
        long result=db.insert(TABLE_DIARY,null,values);
        db.close();
        if(result==-1)return false;
        else return true;
    }

    public boolean addItem(BucketItem item){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(BUCKET_COL_2,item.getTitle());
        values.put(BUCKET_COL_3,"진행중");
        values.put(BUCKET_COL_4,item.getDetail());
        values.put(BUCKET_COL_5,item.getCgory());
        values.put(BUCKET_COL_6,item.getHash1());
        values.put(BUCKET_COL_7,item.getHash2());
        long result=db.insert(TABLE_BUCKET,null,values);
        db.close();
        if(result==-1)return false;
        else return true;
    }

    public List<DiaryItem> viewEntry(){
        List<DiaryItem> items=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();

        String query="SELECT * FROM "+TABLE_DIARY;
        Cursor cursor=db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                DiaryItem item=new DiaryItem();
                item.setId(cursor.getString(0));
                item.setTitle(cursor.getString(1));
                item.setDetail(cursor.getString(3));
                item.setImage(cursor.getString(2));
                items.add(item);
            }while(cursor.moveToNext());
        }
        db.close();
        return items;
    }

    public List<BucketItem> viewItem(){
        List<BucketItem> items=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();

        String query="SELECT * FROM "+TABLE_BUCKET;
        Cursor cursor=db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                BucketItem item=new BucketItem();
                item.setId(cursor.getString(0));
                item.setTitle(cursor.getString(1));
                item.setStatus(cursor.getString(2));
                item.setDetail(cursor.getString(3));
                item.setCgory(cursor.getString(4));
                item.setHash1(cursor.getString(5));
                item.setHash2(cursor.getString(6));
                items.add(item);
            }while(cursor.moveToNext());
        }
        db.close();
        return items;
    }

    public List<BucketItem> selectItem(String name){
        List<BucketItem> items=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();

        String query="SELECT * FROM "+TABLE_BUCKET+" WHERE CAT=?";
        Log.e("Error","Error "+query);
        Cursor cursor=db.rawQuery(query,new String[]{name});

        if(cursor.moveToFirst()){
            do{
                BucketItem item=new BucketItem();
                item.setId(cursor.getString(0));
                item.setTitle(cursor.getString(1));
                item.setStatus(cursor.getString(2));
                item.setDetail(cursor.getString(3));
                item.setCgory(cursor.getString(4));
                item.setHash1(cursor.getString(5));
                item.setHash2(cursor.getString(6));
                items.add(item);
            }while(cursor.moveToNext());
        }
        db.close();
        return items;
    }

    public boolean updateItem(String id,BucketItem item){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(BUCKET_COL_2,item.getTitle());
        values.put(BUCKET_COL_3,item.getStatus());
        values.put(BUCKET_COL_4,item.getDetail());
        values.put(BUCKET_COL_5,item.getCgory());
        values.put(BUCKET_COL_6,item.getHash1());
        values.put(BUCKET_COL_7,item.getHash2());
        long result=db.update(TABLE_BUCKET,values,"id=? ",new String[]{id});
        db.close();
        if(result==-1)return false;
        else return true;
    }

    public boolean deleteItem(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        int result=db.delete(TABLE_BUCKET,"id=?",new String[]{id});
        if(result==-1)return false;
        else return true;
    }

    public boolean addCate(String cate){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(CATEGORY_COL_2,cate);

        long result=db.insert(TABLE_CATEGORY,null,values);
        db.close();
        if(result==-1)return false;
        return true;
    }

    public List<String> getAllCate(){
        List<String> list=new ArrayList<String>();

        String selectQuery="select * from "+TABLE_CATEGORY;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                list.add(cursor.getString(1));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

}
