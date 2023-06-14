package com.example.fitnesssemestralka.data

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class ExerciseInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val img: String
)