package com.cnpm.vnews.room_db
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.*

@Entity
data class DatabaseModel(
    @PrimaryKey val ID: String,
    @ColumnInfo(name = "Image") val Image: String,
    @ColumnInfo(name = "Title") val Title: String,
    @ColumnInfo(name = "Category") val Category: String,
    @ColumnInfo(name = "Slug") val Slug: String,
    @ColumnInfo(name = "ContentType") val ContentType: String? = null,
    @ColumnInfo(name = "CreateTime") val CreateTime: Date
)

class DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}