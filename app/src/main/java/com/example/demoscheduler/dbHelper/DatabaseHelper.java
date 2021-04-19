package com.example.demoscheduler.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.demoscheduler.setGet.SetGetJobDetails;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "JOBDB";
    private static final String TABLE_NAME = "JOBDB_DETAILS";


    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = "create table "+TABLE_NAME+"(id INTEGER PRIMARY KEY,subject varchar(100),saveDateTime VARCHAR(100),description VARCHAR(300))";
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTs "+TABLE_NAME);
    }

    public boolean addContent(String subject,String desc,String dateTime){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("subject",subject);
        contentValues.put("description",desc);
        contentValues.put("saveDateTime",dateTime);
        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        return true;
    }

    public ArrayList<SetGetJobDetails> getContent(){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ArrayList<SetGetJobDetails> arrayList=new ArrayList<>();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from "+TABLE_NAME,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            SetGetJobDetails setGetJobDetails=new SetGetJobDetails();
            setGetJobDetails.setSubject(cursor.getString(cursor.getColumnIndex("subject")));
            setGetJobDetails.setDesc(cursor.getString(cursor.getColumnIndex("description")));
            setGetJobDetails.setEntDateTime(cursor.getString(cursor.getColumnIndex("saveDateTime")));
            setGetJobDetails.setId(cursor.getInt(cursor.getColumnIndex("id")));
            arrayList.add(setGetJobDetails);
            cursor.moveToNext();
        }
        return arrayList;
    }

    public boolean editContent(int id,String toEdit){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("id",id);
        contentValues.put("description",toEdit);
        long success=-1;
        success=sqLiteDatabase.update(TABLE_NAME,contentValues,"id=?",new String[] {String.valueOf(id)});
        if(success>-1){
            return true;
        }else{
            return false;
        }
    }

    public boolean removeContent(int id){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        long success=-1;
        success=sqLiteDatabase.delete(TABLE_NAME,"id=?",new String[]{String.valueOf(id)});
        if(success>-1){
            return true;
        }else{
            return false;
        }
    }

}
