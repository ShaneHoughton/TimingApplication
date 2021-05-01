package edu.moravian.csci299.finalproject;

import androidx.room.TypeConverter;

import java.util.UUID;

public class RunnerTypeConverter {

    /**
     * Convert a UUID to a string
     *
     * @param uuid the UUID of the event
     * @return the UUID as a string
     */
    @TypeConverter
    public String fromUUID(UUID uuid) {
        return uuid.toString();
    }

    /**
     * convert a String to a UUID
     *
     * @param id the id as a String
     * @return the id as a UUID
     */
    @TypeConverter
    public UUID toUUID(String id) {
        return UUID.fromString(id);
    }

}
