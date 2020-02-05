package org.inventivetalent.notificationlogger

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import org.inventivetalent.notificationlogger.activities.MainActivity
import org.json.JSONException
import org.json.JSONObject

class NotificationListener : NotificationListenerService() {

    private val TAG = "NotificationListener"


    override fun onCreate() {
        super.onCreate()


    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    override fun onListenerConnected() {
        Log.i(TAG, "onListenerConnected")

        val intent = Intent(MainActivity.BROADCAST_TAG)
        intent.putExtra("action", "self_connected")
        writeStatesIntoIntent(intent)
        sendBroadcast(intent)
    }

    override fun onListenerDisconnected() {
        Log.i(TAG, "onListenerDisconnected")

        val intent = Intent(MainActivity.BROADCAST_TAG)
        intent.putExtra("action", "self_disconnected")
        writeStatesIntoIntent(intent)
        sendBroadcast(intent)
    }


    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        Log.i(TAG, "**********  onNotificationPosted")
        if (sbn != null) {
            Log.i(
                TAG,
                "ID :" + sbn.id + "\t" + sbn.notification.tickerText + "\t" + sbn.packageName
            )

            val intent = Intent(MainActivity.BROADCAST_TAG)
            intent.putExtra("action", "post")
            writeNotificationIntoIntent(intent, sbn)
            writeStatesIntoIntent(intent)

            sendBroadcast(intent)

        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        // Hoping this can be empty, even if it was abstract in API 20
    }


    override fun onNotificationRemoved(
        sbn: StatusBarNotification?,
        rankingMap: RankingMap?,
        reason: Int
    ) {
        Log.i(TAG, "**********  onNotificationRemoved")
        if (sbn != null) {
            Log.i(
                TAG,
                "ID :" + sbn.id + "\t" + sbn.notification.tickerText + "\t" + sbn.packageName + "\t" + reason
            )

            val intent = Intent(MainActivity.BROADCAST_TAG)
            intent.putExtra("action", "remove")
            intent.putExtra("reason", reason)
            writeNotificationIntoIntent(intent, sbn)
            writeStatesIntoIntent(intent)

            sendBroadcast(intent)
        }
    }


    override fun onInterruptionFilterChanged(interruptionFilter: Int) {
        Log.i(TAG, "onInterruptionFilterChanged $interruptionFilter")
//        val intent = Intent(MainActivity.BROADCAST_TAG)
//        intent.putExtra("action", "self_filter_changed")
//        writeStatesIntoIntent(intent)
//
//        sendBroadcast(intent)
    }


    private fun writeNotificationIntoIntent(intent: Intent, sbn: StatusBarNotification) {
        val extras = sbn.notification.extras

        var extrasJson = ""
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
            extrasJson = json.toString()
        }

        // https://stackoverflow.com/a/23683704/6257838
        sbn.notification.extras = null

        val bundle = Bundle()
        bundle.putParcelable("notification", sbn)
//        bundle.putBundle("notificationExtras", extras)
        bundle.putString("notificationExtrasJson", extrasJson)
        intent.putExtra("notification", bundle)
    }


    private fun writeStatesIntoIntent(intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.putExtra("interruptionFilter", currentInterruptionFilter)
        }
    }

}