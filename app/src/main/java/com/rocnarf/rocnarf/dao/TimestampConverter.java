package com.rocnarf.rocnarf.dao;

import androidx.room.TypeConverter;



import com.rocnarf.rocnarf.Utils.Common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampConverter {
    static DateFormat df = new SimpleDateFormat(Common.TIME_STAMP_FORMAT);


    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
