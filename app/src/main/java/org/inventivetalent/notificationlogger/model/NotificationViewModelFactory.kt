package org.inventivetalent.notificationlogger.model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NotificationViewModelFactory(private val application: Application):ViewModelProvider.Factory {



    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NotificationViewModel(application) as T
    }

}