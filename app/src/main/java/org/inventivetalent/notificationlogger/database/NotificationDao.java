package org.inventivetalent.notificationlogger.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface NotificationDao {

    @Query("SELECT * FROM notifications ORDER BY time DESC")
    List<Notification> getAll();

    @Query("SELECT * FROM notifications WHERE time > :since ORDER BY time DESC")
    List<Notification> getAllSince(Date since);


    @Insert
    void insert(Notification... notifications);

}
