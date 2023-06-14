package com.example.fitnesssemestralka.data.relations

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["name","id"])
data class GymPlanExerciseInfoCrossRef(
  val name: String,
  @ColumnInfo(index = true)
  val id: Int
)