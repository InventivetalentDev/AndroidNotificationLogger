package org.inventivetalent.notificationlogger

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.inventivetalent.notificationlogger.database.AppDatabase
import org.inventivetalent.notificationlogger.database.Notification
import org.inventivetalent.notificationlogger.database.NotificationRepository

class NotificationViewModel(application: Application) :AndroidViewModel(application) {


    private val repository:NotificationRepository
    val allRecentNotifications:LiveData<List<Notification>>

    init {
        val notificationDao = AppDatabase.getInstance(application).notificationDao()
        repository = NotificationRepository(notificationDao)
        allRecentNotifications = repository.allRecentNotifications
    }


    fun insert(vararg notifications:Notification) = viewModelScope.launch{
        repository.insert(*notifications)
    }


    fun getById(id: Int):Deferred<Notification?>{
       return viewModelScope.async {
           return@async repository.getById(id)
       }
    }

    fun getById(id: Int, callback:(Notification?)->Unit){
        viewModelScope.launch {
            callback.invoke(repository.getById(id))
        }
    }

}