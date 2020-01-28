package org.inventivetalent.notificationlogger.database;

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
    public int number;

    @ColumnInfo
    public int visibility;

    @ColumnInfo
    public int priority;

    @ColumnInfo
    public String sound;

    @ColumnInfo
    public String vibrate;// Long array pattern stored as comma separated string

    @ColumnInfo
    public String channelId;

    @ColumnInfo
    public String channelName;

    @ColumnInfo
    public String channelDescription;

    @ColumnInfo
    public JSONObject extrasJson;

}
