package org.inventivetalent.notificationlogger

import android.os.Bundle
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import org.inventivetalent.notificationlogger.database.Notification

class NotificationViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_view)

        val notificationId = intent?.getIntExtra("notificationId", -1)
        if (notificationId != null && notificationId != -1) {
            val viewmodelProvider =
                ViewModelProvider(this, NotificationViewModelFactory(application)).get(
                    NotificationViewModel::class.java
                )

            viewmodelProvider.getById(notificationId) { notification ->
                println(notification)
                if (notification != null){
                    onNotificationLoaded(notification)
                }else{
                    finish()
                }
            }

        } else {
            finish()
        }
    }


    @UiThread
    fun onNotificationLoaded(notification: Notification) {
        //TODO
    }

}
