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
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notifications = emptyList<Notification>()


    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var notificationId: Int = -1// Database ID

        val appIconView: ImageView = itemView.findViewById(R.id.appIconImageView)
        val actionIconView: ImageView = itemView.findViewById(R.id.actionIconImageView)
        val notificationTitleView: TextView = itemView.findViewById(R.id.notificationTitleTextView)
        val notificationContentView: TextView =
            itemView.findViewById(R.id.notificationContentTextView)
        val notificationDateView: TextView = itemView.findViewById(R.id.notificationDateTextView)
        val notificationTimeView: TextView = itemView.findViewById(R.id.notificationTimeTextView)


        init {
            itemView.setOnClickListener {
                if (notificationId != -1) {
                    val intent = Intent(context, NotificationViewActivity::class.java)
                    intent.putExtra("notificationId", notificationId)
                    context.startActivity(intent)
                }
            }
        }

        fun bind(position: Int, notification: Notification) {
            this.notificationId = notification.id

            appIconView.setImageDrawable(context.packageManager.getApplicationIcon(notification.packageName))
            actionIconView.setImageResource(if (notification.action == "post") R.drawable.ic_add_green_24dp else R.drawable.ic_remove_red_24dp)
            notificationTitleView.text = notification.getExtraString("android.title")
            notificationContentView.text = notification.getExtraString("android.text")
            notificationDateView.text = dateFormat.format(notification.time)
            notificationTimeView.text = timeFormat.format(notification.time)
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