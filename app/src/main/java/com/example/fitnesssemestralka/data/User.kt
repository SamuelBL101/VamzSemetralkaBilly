package com.example.fitnesssemestralka.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDateTime
import java.util.Date


@Entity
@TypeConverters(DateTypeConverter::class)
data class User(
    val weight: Int,
    val height: Int,
    @PrimaryKey(autoGenerate = false)
    val date: Date
    )