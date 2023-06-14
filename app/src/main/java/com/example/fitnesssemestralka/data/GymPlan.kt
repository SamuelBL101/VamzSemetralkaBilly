package com.example.fitnesssemestralka.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters


@Entity(primaryKeys = ["name", "exerciseIds"])
@TypeConverters(ListConverter::class) // Add the TypeConverters annotation
data class GymPlan(
    val name:String,
    val exerciseIds: List<Int>

)