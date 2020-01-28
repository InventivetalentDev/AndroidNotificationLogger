package org.inventivetalent.notificationlogger.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.*

@Dao
interface NotificationDao {

    @Query("SELECT * FROM notifications ORDER BY time DESC")
    fun getAll(): List<Notification>

    @Query("SELECT * FROM notifications WHERE time > :since ORDER BY time DESC")
    fun getAllSince(since: Date): List<Notification>


    @Insert
    fun insert(vararg notifications: Notification)

}
