package com.example.fitnesssemestralka.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.fitnesssemestralka.data.ExerciseInfo
import com.example.fitnesssemestralka.data.ExerciseStat

data class ExerciseInfoWithExerciseStats (
    @Embedded val exerciseInfo: ExerciseInfo,
        @Relation(
                parentColumn = "id",
                entityColumn = "id"
        )
    val ExerciseStats: List<ExerciseStat>
)