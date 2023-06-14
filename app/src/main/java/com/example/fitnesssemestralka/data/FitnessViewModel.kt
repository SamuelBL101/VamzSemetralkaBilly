package com.example.fitnesssemestralka.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.example.fitnesssemestralka.data.*
import com.example.fitnesssemestralka.data.relations.ExerciseInfoWithExerciseStats
import com.example.fitnesssemestralka.data.relations.ExerciseInfoWithGymPlan
import com.example.fitnesssemestralka.data.relations.GymPlanExerciseInfoCrossRef
import com.example.fitnesssemestralka.data.relations.GymPlanWithExerciseInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FitnessViewModel(private val dao: FitnessDao): ViewModel() {
    fun addUserInfo(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.addUserInfo(user)
        }
    }

    suspend fun insertExerciseStat(exerciseStat: ExerciseStat) {
        dao.insertExerciseStat(exerciseStat)
    }

    suspend fun insertGymPlan(gymPlan: GymPlan) {
        dao.insertGymPlan(gymPlan)
    }

    suspend fun insertGymPlanExerciseInfoCrossRef(gymPlanExerciseInfoCrossRef: GymPlanExerciseInfoCrossRef) {
        dao.insertGymPlanExerciseInfoCrossRef(gymPlanExerciseInfoCrossRef)
    }

    suspend fun insertExerciseInfo(exerciseInfo: ExerciseInfo) {
        dao.insertExerciseInfo(exerciseInfo)
    }

    fun readAllData(): LiveData<List<User>> {
           return dao.readAllData()
    }

    fun readLatestUser(): LiveData<User> {
        return dao.readLatestUser()
    }

    fun getPlanNames():LiveData<List<String>>{
        return dao.getPlanNames()
    }

    suspend fun getExerciseWithStats(id: Int): List<ExerciseInfoWithExerciseStats> {
        return dao.getExerciseWithStats(id)
    }

    suspend fun getExercisesOfPlan(name: String): List<GymPlanWithExerciseInfo> {
        return dao.getExercisesOfPlan(name)
    }

    suspend fun getPlanOfExercises(id: Int): List<ExerciseInfoWithGymPlan> {
        return dao.getPlanOfExercises(id)
    }
    suspend fun deleteGymPlan(planName: String) {
        dao.deleteGymPlan(planName)
    }
    fun getAllExercises(): LiveData<List<ExerciseInfo>> {
        return dao.getAllExercises()
    }
    fun getExercisesCount():Int{
        return dao.getExerciseCount()
    }
    fun getAllPlans(): LiveData<List<GymPlan>>{
        return dao.getAllPlans()
    }
    fun getExerciseIdsByPlanName(planName: String): List<Int?> {
        val gymPlan = dao.getGymPlanByPlanName(planName)
        return gymPlan?.exerciseIds ?: emptyList()
    }
    fun getExercise(id: Int):LiveData<ExerciseInfo?>{
        return dao.getExercise(id)
    }

    fun getAllExerciseStats(id :Int): List<ExerciseStat>{
        return dao.getAllExerciseStats(id)
        /*var exerciseStats: List<ExerciseStat> = emptyList()
        viewModelScope.launch(Dispatchers.IO) {
            exerciseStats = dao.getAllExerciseStats(id)
        }
        return exerciseStats*/
    }
    fun getIdOfExercise(name: String): Int{
        return dao.getIdOfExercise(name)

    }



}
