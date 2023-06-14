package com.example.fitnesssemestralka.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fitnesssemestralka.data.relations.GymPlanExerciseInfoCrossRef

@Database(
    entities = [
        User::class,
        GymPlan::class,
        ExerciseInfo::class,
        ExerciseStat::class,
        GymPlanExerciseInfoCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FitnessDatabase: RoomDatabase() {

    abstract val fitnessDao: FitnessDao

    companion object{
        private var INSTANCE: FitnessDatabase? = null

        fun getInstance(context: Context): FitnessDatabase{
            synchronized(this){
                return INSTANCE?: Room.databaseBuilder(
                    context.applicationContext,
                    FitnessDatabase::class.java,
                    "fitness_db"
                )
                    .build().also {
                    INSTANCE = it
                }
            }
        }
    }
}