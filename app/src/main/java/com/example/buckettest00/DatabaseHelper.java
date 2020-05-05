package com.example.buckettest00;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="MEMO";
    private static final String TABLE_BUCKET="BUCKETLIST";
    private static final String TABLE_CATEGORY="CATEGORYLIST";
    private static final String BUCKET_COL_1="ID";
    private static final String BUCKET_COL_2="TITLE";
    private static final String BUCKET_COL_3="STATUS";
    private static final String BUCKET_COL_4="DETAIL";
    private static final String CATEGORY_COL_1="ID";
    private static final String CATEGORY_COL_2="CAT";
    private static final int VERSION=1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 일기 테이블도 필요해보임
        db.execSQL("CREATE TABLE "+TABLE_BUCKET+"(ID INTEGER PRIMARY KEY,TITLE TEXT,STATUS TEXT,DETAIL TEXT)");
        db.execSQL("CREATE TABLE "+TABLE_CATEGORY+"(ID INTEGER PRIMARY KEY,CAT TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_BUCKET);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CATEGORY);
        onCreate(db);
    }

    public boolean addItem(BucketItem item){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(BUCKET_COL_2,item.getTitle());
        values.put(BUCKET_COL_3,"도전 중");
        values.put(BUCKET_COL_4,item.getDetail());

        long result=db.insert(TABLE_BUCKET,null,values);
        db.close();
        if(result==-1)return false;
        else return true;
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
                items.add(item);
            }while(cursor.moveToNext());
        }
        db.close();
        return items;
    }

    public boolean deleteItem(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        int result=db.delete(TABLE_BUCKET,"id=?",new String[]{id});
        if(result==-1)return false;
        else return true;
    }

}
