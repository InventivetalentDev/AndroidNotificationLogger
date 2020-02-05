package org.inventivetalent.notificationlogger.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

import org.inventivetalent.notificationlogger.database.converters.DateConverter
import org.inventivetalent.notificationlogger.database.converters.JsonConverter

@Database(entities = [Notification::class], version = 4)
@TypeConverters(DateConverter::class, JsonConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun notificationDao(): NotificationDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            // https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#6
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "notification_logger"
                )
                    .addMigrations(object : Migration(1, 2) {
                        override fun migrate(database: SupportSQLiteDatabase) {
                            database.execSQL("ALTER TABLE notifications ADD COLUMN `number` INTEGER NOT NULL DEFAULT 0")
                            database.execSQL("ALTER TABLE notifications ADD COLUMN `visibility` INTEGER NOT NULL DEFAULT 0")
                            database.execSQL("ALTER TABLE notifications ADD COLUMN `priority` INTEGER NOT NULL DEFAULT 0")
                            database.execSQL("ALTER TABLE notifications ADD COLUMN `sound` TEXT")
                            database.execSQL("ALTER TABLE notifications ADD COLUMN `vibrate` TEXT")
                            database.execSQL("ALTER TABLE notifications ADD COLUMN `channelName` TEXT")
                            database.execSQL("ALTER TABLE notifications ADD COLUMN `channelDescription` TEXT")
                        }
                    })
                    .addMigrations(object : Migration(2, 3) {
                        override fun migrate(database: SupportSQLiteDatabase) {
                            database.execSQL("ALTER TABLE notifications ADD COLUMN `category` TEXT")
                            database.execSQL("ALTER TABLE notifications ADD COLUMN `color` INTEGER NOT NULL DEFAULT 0")
                            database.execSQL("ALTER TABLE notifications ADD COLUMN `flags` INTEGER NOT NULL DEFAULT 0")
                        }
                    })
                    .addMigrations(object : Migration(3, 4) {
                        override fun migrate(database: SupportSQLiteDatabase) {
                            database.execSQL("ALTER TABLE notifications ADD COLUMN `interruptionFilter` INTEGER NOT NULL DEFAULT 0")
                            database.execSQL("ALTER TABLE notifications ADD COLUMN `removeReason` INTEGER NOT NULL DEFAULT 0")
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}
