package org.inventivetalent.notificationlogger

import android.content.Intent
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
    }


    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        Log.i(TAG, "**********  onNotificationPosted");
        if (sbn != null) {
            Log.i(
                TAG,
                "ID :" + sbn.id + "\t" + sbn.notification.tickerText + "\t" + sbn.packageName
            )

            val intent = Intent(MainActivity.BROADCAST_TAG)
            intent.putExtra("action", "post")
            writeNotificationIntoIntent(intent, sbn)

            sendBroadcast(intent)

        }
    }


    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        Log.i(TAG, "**********  onNotificationRemoved");
        if (sbn != null) {
            Log.i(
                TAG,
                "ID :" + sbn.id + "\t" + sbn.notification.tickerText + "\t" + sbn.packageName
            )

            val intent = Intent(MainActivity.BROADCAST_TAG)
            intent.putExtra("action", "remove")
            writeNotificationIntoIntent(intent, sbn)

            sendBroadcast(intent)
        }
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

}