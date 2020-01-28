package org.inventivetalent.notificationlogger.database

import androidx.lifecycle.LiveData
import java.util.*

class NotificationRepository(private val notificationDao: NotificationDao) {

    val allNotifications: LiveData<List<Notification>> = notificationDao.getAll()

    val allRecentNotifications: LiveData<List<Notification>> =
        notificationDao.getAllSince(Date(System.currentTimeMillis() - 4.32e+8.toLong()/*5days*/))

    suspend fun insert(vararg notifications: Notification) {
        notificationDao.insert(*notifications)
    }

}