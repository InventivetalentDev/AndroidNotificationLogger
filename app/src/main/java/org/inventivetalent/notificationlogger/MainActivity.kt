package org.inventivetalent.notificationlogger

import android.app.NotificationManager
import android.content.*
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.service.notification.StatusBarNotification
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.inventivetalent.notificationlogger.database.Notification
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        private lateinit var notificationViewModel: NotificationViewModel

        fun insertNotification(notification: Notification) {
            notificationViewModel.insert(notification)
        }

        val BROADCAST_TAG = "org.inventivetalent.notificationlogger.NOTIFICATION_LISTENER"
    }

    private val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
    private val ACTION_NOTIFICATION_LISTENER_SETTINGS =
        "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"


    private var enableNotificationListenerAlertDialog: AlertDialog? = null
    private var notificationBroadcastReceiver: NotificationBroadcastReceiver? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NotificationListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        notificationViewModel = ViewModelProvider(this,NotificationViewModelFactory(application)).get(NotificationViewModel::class.java)
        notificationViewModel.allRecentNotifications.observe(this,androidx.lifecycle.Observer{notifications->
            println(notifications)
            notifications?.let { adapter.setNotifications(it) }
        })

        fab.setOnClickListener {
            val nManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val ncomp = NotificationCompat.Builder(this)
            ncomp.setContentTitle("My Notification")
            ncomp.setContentText("Notification Listener Service Example")
            ncomp.setTicker("Notification Listener Service Example")
            ncomp.setSmallIcon(R.drawable.ic_launcher_background)
            ncomp.setAutoCancel(true)
            nManager.notify(System.currentTimeMillis().toInt(), ncomp.build())
        }

        // If the user did not turn the notification listener service on we prompt him to do so
        if (!isNotificationServiceEnabled()) {
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog()
            enableNotificationListenerAlertDialog?.show()
        }

        notificationBroadcastReceiver = NotificationBroadcastReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(BROADCAST_TAG)
        registerReceiver(notificationBroadcastReceiver, intentFilter)

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(notificationBroadcastReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


    // https://github.com/Chagall/notification-listener-service-example/blob/master/app/src/main/java/com/github/chagall/notificationlistenerexample/MainActivity.java
    /**
     * Is Notification Service Enabled.
     * Verifies if the notification listener service is enabled.
     * Got it from: https://github.com/kpbird/NotificationListenerService-Example/blob/master/NLSExample/src/main/java/com/kpbird/nlsexample/NLService.java
     * @return True if enabled, false otherwise.
     */
    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName = packageName
        val flat = Settings.Secure.getString(
            contentResolver,
            ENABLED_NOTIFICATION_LISTENERS
        )
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    // https://github.com/Chagall/notification-listener-service-example/blob/master/app/src/main/java/com/github/chagall/notificationlistenerexample/MainActivity.java
    /**
     * Build Notification Listener Alert Dialog.
     * Builds the alert dialog that pops up if the user has not turned
     * the Notification Listener Service on yet.
     * @return An alert dialog which leads to the notification enabling screen
     */
    private fun buildNotificationServiceAlertDialog(): AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.service_name)
        alertDialogBuilder.setMessage(R.string.service_explanation)
        alertDialogBuilder.setPositiveButton(
            android.R.string.yes
        ) { _, _ -> startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS)) }
        alertDialogBuilder.setNegativeButton(
            android.R.string.no
        ) { _, _ ->
            // If you choose to not enable the notification listener
            // the app. will not work as expected
        }
        return alertDialogBuilder.create()
    }



    class NotificationBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == BROADCAST_TAG) {
                val action = intent.getStringExtra("action")
                val bundle = intent.getBundleExtra("notification")
                val notification = bundle?.getParcelable<StatusBarNotification>("notification")
                val extras = bundle?.getBundle("notificationExtras")
                println(notification)
                println(extras)
                if (notification != null) {

                    val n = Notification()
                    n.action = action
                    n.time = Date()

                    n.notificationId = notification.id
                    n.key = notification.key
                    n.tag = notification.tag
                    n.packageName = notification.packageName

                    if (notification.notification != null) {
                        n.tickerText = notification.notification.tickerText?.toString()
                        n.whenTime = Date(notification.notification.`when`)
                        n.number = notification.notification.number
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            n.visibility = notification.notification.visibility
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            n.channelId = notification.notification.channelId

                            val nManager =
                                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            val channel = nManager.getNotificationChannel(n.channelId)
                            if(channel!=null) {
                                n.channelName = channel.name?.toString()
                                n.channelDescription = channel.description
                                n.priority = channel.importance// same as priority below
                                n.vibrate = channel.vibrationPattern?.joinToString { "," }
                                n.sound = channel.sound?.toString()
                            }
                        } else {
                            n.priority = notification.notification.priority
                            n.vibrate = notification.notification.vibrate?.joinToString { "," }
                            n.sound = notification.notification.sound?.toString()
                        }
                    }

                    if (extras != null) {
                        val json = JSONObject()
                        val keys = extras.keySet()
                        for (key in keys) {
                            val v = extras.get(key)
                            if (v != null) {
                                try {
                                    json.put(key, JSONObject.wrap(v))
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                        n.extrasJson = json
                    }

                    insertNotification(n)
                }
            }
        }

    }

}
