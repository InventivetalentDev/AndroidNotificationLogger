package org.inventivetalent.notificationlogger;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {
		Notification.class
},
		version = 1)
@TypeConverters({
		DateConverter.class,
		JsonConverter.class
})
public abstract class AppDatabase extends RoomDatabase {

	private static AppDatabase INSTANCE;

	public abstract NotificationDao notificationDao();

	public static AppDatabase getInstance(final Context context) {
		if (INSTANCE == null) {
			synchronized (AppDatabase.class) {
				if (INSTANCE == null) {
					INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "notification_logger")
							.fallbackToDestructiveMigration()
							.build();
				}
			}
		}
		return INSTANCE;
	}

}
