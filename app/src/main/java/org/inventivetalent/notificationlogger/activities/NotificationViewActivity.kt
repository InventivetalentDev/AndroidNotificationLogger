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
    lateinit var actionIconView: ImageView
    lateinit var notificationTitleView: TextView
    lateinit var notificationContentView: TextView
    lateinit var notificationDateView: TextView
    lateinit var notificationTimeView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_view)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        appIconView =findViewById(R.id.appIconImageView)
        actionIconView = findViewById(R.id.actionIconImageView)
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
        val applicationInfo = packageManager.getApplicationInfo(notification.packageName,0)

        // Same stuff as the list view holder
        appIconView.setImageDrawable(packageManager.getApplicationIcon(applicationInfo))
        actionIconView.setImageResource(if (notification.action == "post") R.drawable.ic_add_green_24dp else R.drawable.ic_remove_red_24dp)
        notificationTitleView.text = notification.getExtraString("android.title")
        notificationContentView.text = notification.getExtraString("android.text")
        notificationDateView.text = NotificationListAdapter.dateFormat.format(notification.time)
        notificationTimeView.text = NotificationListAdapter.timeFormat.format(notification.time)

        // More specific details
        supportActionBar?.title = packageManager.getApplicationLabel(applicationInfo)


        debugTextView.text = notification.extrasJson?.toString()
    }

}
