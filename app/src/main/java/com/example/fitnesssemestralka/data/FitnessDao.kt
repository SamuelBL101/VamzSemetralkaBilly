package com.example.fitnesssemestralka.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fitnesssemestralka.data.relations.ExerciseInfoWithExerciseStats
import com.example.fitnesssemestralka.data.relations.ExerciseInfoWithGymPlan
import com.example.fitnesssemestralka.data.relations.GymPlanExerciseInfoCrossRef
import com.example.fitnesssemestralka.data.relations.GymPlanWithExerciseInfo

@Dao
interface FitnessDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUserInfo(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseStat(exerciseStat: ExerciseStat)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGymPlan(gymPlan: GymPlan)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGymPlanExerciseInfoCrossRef(gymPlanExerciseInfoCrossRef: GymPlanExerciseInfoCrossRef)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExerciseInfo(exerciseInfo: ExerciseInfo)

    @Query("SELECT * FROM user ORDER BY date ASC")
    fun readAllData(): LiveData<List<User>>

    @Query("SELECT * FROM user ORDER BY date DESC LIMIT 1")
    fun readLatestUser(): LiveData<User>

    @Query("SELECT name FROM GymPlan ORDER BY name ASC")
    fun getPlanNames():LiveData<List<String>>

    @Query("DELETE FROM GymPlan WHERE name = :planName")
    suspend fun deleteGymPlan(planName: String)

    @Transaction
    @Query("SELECT * FROM exerciseInfo WHERE id =:id")
    suspend fun getExerciseWithStats(id: Int): List<ExerciseInfoWithExerciseStats>

    @Query("SELECT * FROM exerciseInfo ORDER BY id")
    fun getAllExercises():LiveData<List<ExerciseInfo>>

    @Query("SELECT COUNT(*) FROM exerciseInfo")
    fun getExerciseCount(): Int

    @Query("SELECT * FROM GymPlan ORDER BY name ASC")
    fun getAllPlans():LiveData<List<GymPlan>>

    @Query("SELECT * FROM ExerciseInfo WHERE id = :id")
    fun getExercise(id: Int):LiveData<ExerciseInfo?>

    @Transaction
    @Query("SELECT * FROM gymPlan WHERE name =:name")
    suspend fun getExercisesOfPlan(name:String): List<GymPlanWithExerciseInfo>

    @Transaction
    @Query("SELECT * FROM exerciseInfo WHERE id =:id")
    suspend fun getPlanOfExercises(id: Int): List<ExerciseInfoWithGymPlan>

    @Query("SELECT * FROM GymPlan WHERE name = :planName")
    fun getGymPlanByPlanName(planName: String?): GymPlan?

    @Query("SELECT * FROM ExerciseStat WHERE id = :id")
    fun getAllExerciseStats(id :Int): List<ExerciseStat>

    @Query("SELECT id From ExerciseInfo WHERE name = :name")
    fun getIdOfExercise(name: String): Int

}