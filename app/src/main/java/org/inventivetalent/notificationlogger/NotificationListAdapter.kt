package org.inventivetalent.notificationlogger

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.inventivetalent.notificationlogger.database.Notification
import java.text.DateFormat
import java.text.SimpleDateFormat

class NotificationListAdapter internal constructor(
    private val context: Context
) : RecyclerView.Adapter<NotificationListAdapter.NotificationViewHolder>() {

    val timeFormat: DateFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
    val dateFormat: DateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT)

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
        holder.appIconView.setImageDrawable(context.packageManager.getApplicationIcon(current.packageName))
        holder.notificationTitleView.text = current.getExtraString("android.title")
        holder.notificationContentView.text = current.getExtraString("android.text")
        holder.notificationDateView.text = dateFormat.format(current.time)
        holder.notificationTimeView.text = timeFormat.format(current.time)
    }

    internal fun setNotifications(notifications: List<Notification>) {
        this.notifications = notifications
        notifyDataSetChanged()
    }


    override fun getItemCount() = notifications.size

}