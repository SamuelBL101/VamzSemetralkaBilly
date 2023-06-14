package com.example.fitnesssemestralka

import NotificationUtils
import android.content.pm.PackageManager
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.fitnesssemestralka.data.ExerciseInfo
import com.example.fitnesssemestralka.data.FitnessDao
import com.example.fitnesssemestralka.data.FitnessDatabase
import com.example.fitnesssemestralka.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Main activity in application.
 * Its entry point where all automatic functions is provided.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout

    /**
     * Create a view of the entry frame of application
     * Call functions.
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dao: FitnessDao = FitnessDatabase.getInstance(this).fitnessDao

        lifecycleScope.launch {
            withContext(Dispatchers.IO){
                if (dao.getExerciseCount() == 0){
                addexercises(dao) }
            }
        }
        drawerLayout = binding.drawerLayout

        NotificationUtils.createNotificationChannel(this)
        NotificationUtils.scheduleNotification(this)
        
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)
        supportActionBar?.setDisplayShowTitleEnabled(false)

    }

    /**
     * Schedule the notification if permmision is granted.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                NotificationUtils.scheduleNotification(this)
            } else {
            }
        }
    }

    /**
     * Change the fragment when Back button is pressed to Home2.
     *
     * @return
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        val currentDestination = navController.currentDestination?.id
        if (currentDestination == R.id.home2) {
            return false
        }
        navController.navigate(R.id.home2)
        return true
    }
    /**
     * Insert into database all exercises.
     *
     * @param dao
     */
    private suspend fun addexercises(dao: FitnessDao){
        val exerciseList = listOf(
            ExerciseInfo(0, "TraditionalPushUp", "traditionalpushups"),
            ExerciseInfo(0, "Incline bench press", "inclinebench"),
            ExerciseInfo(0, "Cable chest flys", "cablechestflys"),
            ExerciseInfo(0, "Narrow Grip Chest Press", "narrowgripchestpress"),
            ExerciseInfo(0, "Dips", "dips"),
            ExerciseInfo(0, "Machine Chest Press", "machinechestpress"),
            ExerciseInfo(0, "Machine Fly", "machinefly"),
            //back
            ExerciseInfo(0, "Back Extension", "backextension"),
            ExerciseInfo(0, "Lat Pulldown", "latpulldown"),
            ExerciseInfo(0, "Dumbbell Bend Over", "dumbbellrow"),
            ExerciseInfo(0, "Single arm dumbbell row", "singelarmrow"),
            ExerciseInfo(0, "Landmine T-bar Row", "landminetbarrow"),
            ExerciseInfo(0, "Seated Row", "seatedcablerow"),
             //legs
            ExerciseInfo(0, "Lunges", "lunges"),
            ExerciseInfo(0, "Dead-lifts", "deadlift"),
            ExerciseInfo(0, "Step-Ups", "stepups"),
            ExerciseInfo(0, "Bulgarian Split Squats", "bulgariansplit"),
            ExerciseInfo(0, "Squats", "squats"),
            ExerciseInfo(0, "Calf Raises", "calf"),
            ExerciseInfo(0, "Leg extension", "legextension"),
            //biceps
            ExerciseInfo(0, "Barbell Curls", "barbellcurls"),
            ExerciseInfo(0, "Incline Dumbbell Curls", "inclinecurls"),
            ExerciseInfo(0, "Biceps Cable Curl", "cablecurls"),
            ExerciseInfo(0, "EZ Bar Curls", "ezbarcurl"),
            //triceps
            ExerciseInfo(0, "Dumbbell Triceps Extension", "dumbbelltriceps"),
            ExerciseInfo(0, "Close grip bench press", "closegrip"),
            ExerciseInfo(0, "Skull Crushers", "skull"),
            ExerciseInfo(0, "Tricep Push Down", "triceppush")
            )
        exerciseList.forEach { exercise ->
            dao.insertExerciseInfo(exercise)
        }
    }
    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
    }
}