package org.inventivetalent.notificationlogger.database.converters;

import androidx.room.TypeConverter;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonConverter {


    @TypeConverter
    public static JSONObject jsonFromString(String string) {
        if (string == null) return null;
        try {
            return new JSONObject(string);
        } catch (JSONException e) {
            return null;
        }
    }

    @TypeConverter
    public static String jsonToString(JSONObject json) {
        if (json == null) return "{}";
        return json.toString();
    }

}
