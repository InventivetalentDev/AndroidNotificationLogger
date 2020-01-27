package org.inventivetalent.notificationlogger;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotificationDao {

    @Query("SELECT * FROM notifications ORDER BY time DESC")
    List<Notification> getAll();


    @Insert
    void insert(Notification... notifications);

}
