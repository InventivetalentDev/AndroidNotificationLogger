package org.inventivetalent.notificationlogger;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {
		Notification.class
},
		version = 2)
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
                            .addMigrations(new Migration(1,2) {
                                @Override
                                public void migrate(@NonNull SupportSQLiteDatabase database) {
                                    database.execSQL("ALTER TABLE notifications ADD COLUMN `number` INTEGER NOT NULL DEFAULT 0");
                                    database.execSQL("ALTER TABLE notifications ADD COLUMN `visibility` INTEGER NOT NULL DEFAULT 0");
                                    database.execSQL("ALTER TABLE notifications ADD COLUMN `priority` INTEGER NOT NULL DEFAULT 0");
                                    database.execSQL("ALTER TABLE notifications ADD COLUMN `sound` TEXT");
                                    database.execSQL("ALTER TABLE notifications ADD COLUMN `vibrate` TEXT");
                                    database.execSQL("ALTER TABLE notifications ADD COLUMN `channelName` TEXT");
                                    database.execSQL("ALTER TABLE notifications ADD COLUMN `channelDescription` TEXT");
                                }
                            })
							.fallbackToDestructiveMigration()
							.build();
				}
			}
		}
		return INSTANCE;
	}

}
