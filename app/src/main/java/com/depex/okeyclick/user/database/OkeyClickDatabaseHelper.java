package com.depex.okeyclick.user.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by we on 2/23/2018.
 */

public class OkeyClickDatabaseHelper extends SQLiteOpenHelper {

    public OkeyClickDatabaseHelper(Context context) {
        super(context,"okeyclick.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("create table notify(_id integer primary key autoincrement, " +
                    "notification_type varchar(50)," +
                    "notify_msg text, notify_data text, notify_date varchar(20))");
        sqLiteDatabase.execSQL("create table taskTable(_id integer primary key autoincrement, " +
                "taskKey varchar(10), status varchar(20), task_id varchar(10) not null unique , sp_id varchar(10), createdBy varchar(10))");
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("drop table if exists notify");
            sqLiteDatabase.execSQL("drop table if exists taskTable");
            onCreate(sqLiteDatabase);
    }


    public long  taskInsert(String taskStatus, String taskID, String taskKey, String sp_id , String createdBy  /*userid is created by*/ ){
        ContentValues values=new ContentValues();
        values.put("taskKey", taskKey);
        values.put("status", taskStatus);
        values.put("task_id", taskID);
        values.put("sp_id", sp_id);
        values.put("createdBy", createdBy);
        return getDatabase().insert("taskTable", null, values);
    }

    public int taskUpdate(String task_id , String status, String sp_id, String taskKey){
        ContentValues values=new ContentValues();
        if(status!=null)
            values.put("status", status);
        if(sp_id!=null)
            values.put("sp_id", sp_id);
        if(taskKey!=null)
            values.put("taskKey" , taskKey);
        return getDatabase().update("taskTable", values, "task_id="+task_id, null);
    }

    public int deleteTask(String task_id){
        return getDatabase().delete("taskTable", "task_id="+task_id, null);
    }

    public int deleteAllTask(){
        return getDatabase().delete("taskTable", null, null);
    }


   public  SQLiteDatabase getDatabase(){
        return getWritableDatabase();
    }

    public long insert(String notification_type, String notify_msg, String notify_data, Date notify_date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String notifyDate=simpleDateFormat.format(notify_date);

        ContentValues contentValues=new ContentValues();
        contentValues.put("notification_type", notification_type);
        contentValues.put("notify_msg", notify_msg);
        contentValues.put("notify_data", notify_data);
        contentValues.put("notify_date", notifyDate);

        return getDatabase().insert("notify", null,contentValues);
    }

    public int deleteNotification(String id){
        return getDatabase().delete("notify", "_id="+id, null);
    }
    public int deleteAllNotification(){
        return getDatabase().delete("notify", null, null);
    }

    public Cursor getNotifications(){
        return getDatabase().rawQuery("select *from notify", null);
    }
}