package org.inventivetalent.notificationlogger.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.*

@Dao
interface NotificationDao {

    @Query("SELECT * FROM notifications ORDER BY time DESC")
    fun getAll(): LiveData<List<Notification>>

    @Query("SELECT * FROM notifications WHERE time > :since ORDER BY time DESC")
    fun getAllSince(since: Date): LiveData<List<Notification>>


    @Insert
    suspend fun insert(vararg notifications: Notification)

}
