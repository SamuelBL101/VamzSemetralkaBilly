package com.example.fitnesssemestralka.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.fitnesssemestralka.data.ExerciseInfo
import com.example.fitnesssemestralka.data.GymPlan

data class GymPlanWithExerciseInfo(
 @Embedded val gymPlan: GymPlan,
 @Relation(
     parentColumn = "name",
     entityColumn = "id",
    associateBy = Junction(GymPlanExerciseInfoCrossRef::class)
 )
    val exercises: List<ExerciseInfo>
)