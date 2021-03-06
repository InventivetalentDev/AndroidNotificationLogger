package org.inventivetalent.notificationlogger

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.inventivetalent.notificationlogger.activities.NotificationViewActivity
import org.inventivetalent.notificationlogger.database.Notification
import java.text.DateFormat
import java.text.SimpleDateFormat

class NotificationListAdapter internal constructor(
    private val context: Context
) : RecyclerView.Adapter<NotificationListAdapter.NotificationViewHolder>() {

    companion object {
        val timeFormat: DateFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM)
        val dateFormat: DateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT)
        val dateTimeFormat: DateFormat = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.MEDIUM)
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notifications = emptyList<Notification>()


    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var notificationId: Int = -1// Database ID
        var notificationAction: String = ""

        val appIconView: ImageView = itemView.findViewById(R.id.appIconImageView)
        val actionIconView: ImageView = itemView.findViewById(R.id.actionIconImageView)
        val notificationTitleView: TextView = itemView.findViewById(R.id.notificationTitleTextView)
        val notificationContentView: TextView =
            itemView.findViewById(R.id.notificationContentTextView)
        val notificationDateView: TextView = itemView.findViewById(R.id.notificationDateTextView)
        val notificationTimeView: TextView = itemView.findViewById(R.id.notificationTimeTextView)


        init {
            itemView.setOnClickListener {
                if (notificationId != -1 && !this.notificationAction.startsWith("self_")) {
                    val intent = Intent(context, NotificationViewActivity::class.java)
                    intent.putExtra("notificationId", notificationId)
                    intent.putExtra("notificationAction", notificationAction)
                    context.startActivity(intent)
                }
            }
        }

        fun bind(position: Int, notification: Notification) {
            this.notificationId = notification.id
            this.notificationAction = notification.action!!

            actionIconView.setImageResource(if (notification.action == "post") R.drawable.ic_plus_green_shadow_hard_24dp else if (notification.action == "remove") R.drawable.ic_minus_red_shadow_hard_24dp else 0)
            notificationDateView.text = dateFormat.format(notification.time)
            notificationTimeView.text = timeFormat.format(notification.time)

            if (notification.action?.startsWith("self_")!!) {
                appIconView.setImageResource(R.mipmap.ic_launcher)
                notificationTitleView.text = context.getString(R.string.app_name)
                notificationContentView.text = notification.action
            } else {
                appIconView.setImageDrawable(context.packageManager.getApplicationIcon(notification.packageName))
                notificationTitleView.text = notification.getExtraString("android.title")
                notificationContentView.text = notification.getExtraString("android.text")
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val itemView = inflater.inflate(R.layout.notification_list_item, parent, false)
        return NotificationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val current = notifications[position]
        holder.bind(position, current)
    }

    internal fun setNotifications(notifications: List<Notification>) {
        this.notifications = notifications
        notifyDataSetChanged()
    }


    override fun getItemCount() = notifications.size

}