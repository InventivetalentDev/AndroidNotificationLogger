package org.inventivetalent.notificationlogger

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.inventivetalent.notificationlogger.database.Notification

class NotificationListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<NotificationListAdapter.NotificationViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notifications = emptyList<Notification>()


    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appIconView: ImageView = itemView.findViewById(R.id.appIconImageView)
        val notificationTitleView: TextView = itemView.findViewById(R.id.notificationTitleTextView)
        val notificationContentView: TextView =
            itemView.findViewById(R.id.notificationContentTextView)
        val notificationDateView: TextView = itemView.findViewById(R.id.notificationDateTextView)
        val notificationTimeView: TextView = itemView.findViewById(R.id.notificationTimeTextView)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val itemView = inflater.inflate(R.layout.notification_list_item, parent, false)
        return NotificationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val current = notifications[position]
        //TODO
        holder.notificationTitleView.text = current.tickerText
        //TODO
    }

    internal fun setNotifications(notifications: List<Notification>) {
        this.notifications = notifications
        notifyDataSetChanged()
    }


    override fun getItemCount() = notifications.size

}