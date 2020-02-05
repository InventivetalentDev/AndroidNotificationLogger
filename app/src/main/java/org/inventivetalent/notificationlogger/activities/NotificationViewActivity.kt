package org.inventivetalent.notificationlogger.activities

import android.annotation.SuppressLint
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

        appIconView = findViewById(R.id.appIconImageView)
        actionIconView = findViewById(R.id.actionIconImageView)
        notificationTitleView = findViewById(R.id.notificationTitleTextView)
        notificationContentView = findViewById(R.id.notificationContentTextView)
        notificationDateView = findViewById(R.id.notificationDateTextView)
        notificationTimeView = findViewById(R.id.notificationTimeTextView)


        val notificationId = intent?.getIntExtra("notificationId", -1)
        if (notificationId != null && notificationId != -1) {
            val viewmodelProvider =
                ViewModelProvider(
                    this,
                    NotificationViewModelFactory(
                        application
                    )
                ).get(
                    NotificationViewModel::class.java
                )

            viewmodelProvider.getById(notificationId) { notification ->
                println(notification)
                if (notification != null) {
                    onNotificationLoaded(notification)
                } else {
                    finish()
                }
            }

        } else {
            finish()
        }
    }


    @SuppressLint("SetTextI18n")
    @UiThread
    fun onNotificationLoaded(notification: Notification) {
        val applicationInfo = packageManager.getApplicationInfo(notification.packageName, 0)

        // Same stuff as the list view holder
        appIconView.setImageDrawable(packageManager.getApplicationIcon(applicationInfo))
        actionIconView.setImageResource(if (notification.action == "post") R.drawable.ic_add_green_24dp else R.drawable.ic_remove_red_24dp)
        notificationTitleView.text = notification.getExtraString("android.title")
        notificationContentView.text = notification.getExtraString("android.text")
        notificationDateView.text = NotificationListAdapter.dateFormat.format(notification.time)
        notificationTimeView.text = NotificationListAdapter.timeFormat.format(notification.time)

        // More specific details
        supportActionBar?.title = packageManager.getApplicationLabel(applicationInfo)


        idTextView.text = notification.id.toString()
        keyTextView.text = notification.key
        tagTextView.text = notification.tag
        packageTextView.text = notification.packageName
        tickerTextView.text = notification.tickerText
        whenTextView.text =
            if (notification.whenTime != null) NotificationListAdapter.dateTimeFormat.format(
                notification.whenTime!!
            ) else "?"
        numberTextView.text = notification.number.toString()

        visibilityTextView.text = when (notification.visibility) {
            1 -> "PUBLIC"
            0 -> "PRIVATE"
            -1 -> "SECRET"
            else -> "UNKNOWN"
        } + " (" + notification.visibility.toString() + ")"

        priorityTextView.text = when (notification.priority) {
            0 -> "NONE"
            1 -> "MIN"
            2 -> "LOW"
            3 -> "DEFAULT"
            4 -> "HIGH"
            5 -> "MAX"
            else -> "UNKNOWN"
        } + " (" + notification.priority.toString() + ")"

        interruptionFilterTextView.text = when (notification.interruptionFilter) {
            1 -> "ALL"
            2 -> "PRIORITY"
            3 -> "NONE"
            4 -> "ALARMS"
            else -> "UNKNOWN"
        } + " (" + notification.interruptionFilter.toString() + ")"

        reasonTextView.text = when (notification.removeReason) {
            1 -> "CLICK"
            2 -> "CANCEL"
            3 -> "CANCEL_ALL"
            4 -> "ERROR"
            5 -> "PACKAGE_CHANGED"
            6 -> "USER_STOPPED"
            7 -> "PACKAGE_BANNED"
            8 -> "APP_CANCEL"
            9 -> "APP_CANCEL_ALL"
            10 -> "LISTENER_CANCEL"
            11 -> "LISTENER_CANCEL_ALL"
            12 -> "GROUP_SUMMARY_CANCELED"
            13 -> "GROUP_OPTIMIZATION"
            14 -> "PACKAGE_SUSPENDED"
            15 -> "PROFILE_TURNED_OFF"
            16 -> "UNAUTOBUNDLED"
            17 -> "CHANNEL_BANNED"
            18 -> "SNOOZED"
            19 -> "TIMEOUT"
            else -> "UNKNOWN"
        } + " (" + notification.removeReason.toString() + ")"

        colorTextView.text = notification.color.toString()
        colorTextView.setTextColor(notification.color)

        flagsTextView.text = notification.flags.toString()

        extrasJsonTextView.text = notification.extrasJson?.toString(2)


    }

}
