package uz.androdev.movies.data.db

import androidx.room.TypeConverter
import org.threeten.bp.*

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 8:12 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class Converters {
    @TypeConverter
    fun convertLocalDateTimeToMillis(localDateTime: LocalDateTime): Long {
        return localDateTime.atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    @TypeConverter
    fun convertMillisToLocalDateTime(milliseconds: Long): LocalDateTime {
        return Instant.ofEpochMilli(milliseconds)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }
}