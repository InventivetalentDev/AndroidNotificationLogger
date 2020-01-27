package org.inventivetalent.notificationlogger;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONObject;

import java.util.Date;

@Entity(tableName = "notifications")
public class Notification {


    @PrimaryKey(autoGenerate = true)
    public int id;


    @ColumnInfo
    public String action;

    @ColumnInfo
    public Date time;

    @ColumnInfo
    public int notificationId;

    @ColumnInfo
    public String key;

    @ColumnInfo
    public String tag;

    @ColumnInfo
    public String packageName;

    @ColumnInfo
    public String tickerText;

    @ColumnInfo(name = "when")
    public Date whenTime;

    @ColumnInfo
    public String channelId;


    @ColumnInfo
    public JSONObject extrasJson;

}
