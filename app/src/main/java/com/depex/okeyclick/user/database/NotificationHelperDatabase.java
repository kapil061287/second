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

public class NotificationHelperDatabase extends SQLiteOpenHelper {

    public NotificationHelperDatabase(Context context) {
        super(context,"notificatoin.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("create table notify(_id integer primary key autoincrement, " +
                    "notification_type varchar(50)," +
                    "notify_msg text, notify_data text, notify_date varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("drop table if exists notify");
            onCreate(sqLiteDatabase);
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

    public int delete(String id){
        return getDatabase().delete("notify", "_id="+id, null);
    }

    public Cursor getNotifications(){
        return getDatabase().rawQuery("select *from notify", null);
    }
}