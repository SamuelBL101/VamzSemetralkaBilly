package com.example.fitnesssemestralka.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.fitnesssemestralka.data.ExerciseInfo
import com.example.fitnesssemestralka.data.GymPlan

data class ExerciseInfoWithGymPlan(
    @Embedded val exerciseInfo: ExerciseInfo,
    @Relation(
        parentColumn = "id",
        entityColumn = "name",
        associateBy = Junction(GymPlanExerciseInfoCrossRef::class)
    )
    val gymPlans: List<GymPlan>
)