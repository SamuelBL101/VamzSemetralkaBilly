package com.example.fitnesssemestralka.data

import androidx.room.TypeConverter

class ListConverter {
    @TypeConverter
    fun fromList(list: List<Int>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toList(data: String?): List<Int> {
        if (data.isNullOrEmpty()) {
            return emptyList()
        } else {
            return data.split(",").map { it.toInt() }
        }
    }
}
