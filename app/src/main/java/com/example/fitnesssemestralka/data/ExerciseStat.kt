package com.example.fitnesssemestralka.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity(primaryKeys = ["id", "date"])
@TypeConverters(DateTypeConverter::class)
data class ExerciseStat(
    val id: Int,
    val weight: Double?,
    val reps: Int,
    val date: Date
)