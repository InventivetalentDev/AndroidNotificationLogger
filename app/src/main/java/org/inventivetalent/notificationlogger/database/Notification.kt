package org.inventivetalent.notificationlogger.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONObject
import java.util.*

@Entity(tableName = "notifications")
class Notification {


    @PrimaryKey(autoGenerate = true)
    var id: Int = 0


    @ColumnInfo
    var action: String? = null

    @ColumnInfo
    var time: Date? = null

    @ColumnInfo
    var notificationId: Int = 0

    @ColumnInfo
    var key: String? = null

    @ColumnInfo
    var tag: String? = null

    @ColumnInfo
    var packageName: String? = null

    @ColumnInfo
    var tickerText: String? = null

    @ColumnInfo(name = "when")
    var whenTime: Date? = null

    @ColumnInfo
    var number: Int = 0

    @ColumnInfo
    var visibility: Int = 0

    @ColumnInfo
    var priority: Int = 0

    @ColumnInfo
    var interruptionFilter: Int = 0

    @ColumnInfo
    var removeReason: Int = 0

    @ColumnInfo
    var sound: String? = null

    @ColumnInfo
    var vibrate: String? = null// Long array pattern stored as comma separated string

    @ColumnInfo
    var category: String? = null

    @ColumnInfo
    var color: Int = 0

    @ColumnInfo
    var flags: Int = 0

    @ColumnInfo
    var channelId: String? = null

    @ColumnInfo
    var channelName: String? = null

    @ColumnInfo
    var channelDescription: String? = null

    @ColumnInfo
    var extrasJson: JSONObject? = null

    fun hasExtra(key:String):Boolean{
        return extrasJson != null && (extrasJson as JSONObject).has(key)
    }

    fun getExtraString(key:String, def:String?=null):String?{
        if (hasExtra(key)) {
            return extrasJson?.getString(key)
        }
        return def
    }

    fun getExtraInt(key:String, def:Int=0):Int?{
        if (hasExtra(key)) {
            return extrasJson?.getInt(key)
        }
        return def
    }

}
