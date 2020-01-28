package org.inventivetalent.notificationlogger.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_notification_view.*
import org.inventivetalent.notificationlogger.NotificationListAdapter
import org.inventivetalent.notificationlogger.R
import org.inventivetalent.notificationlogger.database.Notification
import org.inventivetalent.notificationlogger.model.NotificationViewModel
import org.inventivetalent.notificationlogger.model.NotificationViewModelFactory

class NotificationViewActivity : AppCompatActivity() {

    lateinit var appIconView: ImageView
    lateinit var notificationTitleView: TextView
    lateinit var notificationContentView: TextView
    lateinit var notificationDateView: TextView
    lateinit var notificationTimeView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_view)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        appIconView =findViewById(R.id.appIconImageView)
        notificationTitleView=findViewById(R.id.notificationTitleTextView)
        notificationContentView = findViewById(R.id.notificationContentTextView)
        notificationDateView = findViewById(R.id.notificationDateTextView)
        notificationTimeView = findViewById(R.id.notificationTimeTextView)


        val notificationId = intent?.getIntExtra("notificationId", -1)
        if (notificationId != null && notificationId != -1) {
            val viewmodelProvider =
                ViewModelProvider(this,
                    NotificationViewModelFactory(
                        application
                    )
                ).get(
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
        appIconView.setImageDrawable(packageManager.getApplicationIcon(notification.packageName))
        notificationTitleView.text = notification.getExtraString("android.title")
        notificationContentView.text = notification.getExtraString("android.text")
        notificationDateView.text = NotificationListAdapter.dateFormat.format(notification.time)
        notificationTimeView.text = NotificationListAdapter.timeFormat.format(notification.time)


        debugTextView.text = notification.extrasJson?.toString()
    }

}
