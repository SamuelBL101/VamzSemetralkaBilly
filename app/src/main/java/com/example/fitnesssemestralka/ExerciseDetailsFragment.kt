package com.example.fitnesssemestralka

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fitnesssemestralka.data.*
import com.example.fitnesssemestralka.databinding.FragmentExerciseDetailsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Class that provide functionality of writing exercises to database.
 *
 */
class ExerciseDetailsFragment : Fragment() {
    private val viewModel: FitnessViewModel by viewModels {
        FitnessViewModelFactory(FitnessDatabase.getInstance(requireContext()).fitnessDao)
    }

    private var exerciseIds: List<Int?> = emptyList()
    private var currentExerciseIndex = 0
    private var planStartTime: Long = 0
    private var repsCount = 0
    private var weightCount: Double = 0.00
    private var repsIncrementCount = 0
    private var repsDecrementCount = 0
    private var weightIncrementCount = 0
    private var weightDecrementCount = 0

    /**
     * Create the view of fragment.
     * Set onClickListeners to adding weight and reps by buttons.
     * Writing the stat of exercise to database based on user input values.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        planStartTime = System.currentTimeMillis()

        val args = arguments
        val planName = args?.getString("Key")
        var currentExerciseId: Int = 0 // Declare the currentExerciseId variable

        val binding = FragmentExerciseDetailsBinding.inflate(layoutInflater)
        Log.d("T", planName.toString())
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                exerciseIds = viewModel.getExerciseIdsByPlanName(planName.toString())
            }
            val exerciseImageView: ImageView = binding.exerciseImageView
            val nextButton: Button = binding.nextButton

            val exerciseId = exerciseIds[currentExerciseIndex]
            if (exerciseId != null) {
                val exerciseInfoLiveData = viewModel.getExercise(exerciseId)
                exerciseInfoLiveData.observe(viewLifecycleOwner) { exerciseInfo ->
                    if (exerciseInfo != null) {
                        loadExerciseImage(exerciseImageView, exerciseInfo)
                        currentExerciseId = exerciseId
                    } else {
                        // Handle the case when exerciseInfo is null (exercise not found)
                        // You can show a default image or handle the situation in a way that fits your app's logic
                        Log.d("T", "Nieje taky cvik")
                    }
                }
            }

            nextButton.setOnClickListener {
                currentExerciseIndex++
                if (currentExerciseIndex < exerciseIds.size) {
                    // If there are more exercises, load the next exercise image
                    val nextExerciseId = exerciseIds[currentExerciseIndex]
                    if (nextExerciseId != null) {
                        val nextExerciseInfoLiveData = viewModel.getExercise(nextExerciseId)
                        nextExerciseInfoLiveData.observe(viewLifecycleOwner) { nextExerciseInfo ->
                            if (nextExerciseInfo != null) {
                                loadExerciseImage(exerciseImageView, nextExerciseInfo)
                            } else {
                                // Handle the case when nextExerciseInfo is null (exercise not found)
                                // You can show a default image or handle the situation in a way that fits your app's logic
                                Log.d("T", "Nieje taky cvik")
                            }
                        }
                    }
                } else {
                    // If there are no more exercises, navigate to the next fragment
                    val totalTimeElapsed = calculateTotalTimeElapsed()
                    navigateToNextFragment(totalTimeElapsed)
                }
            }
        }

        // Inflate the layout for this fragment
        val repsEditText: EditText = binding.repsEditText
        val weightEditText: EditText = binding.weightEditText
        val confirmButton: Button = binding.confirmButton
        val plusRepButton: Button = binding.repsIncrementButton
        val minusRepButton: Button = binding.repsDecrementButton
        val plusWeightButton: Button = binding.weightIncrementButton
        val minusWeightButton: Button = binding.weightDecrementButton

        plusRepButton.setOnClickListener {
            val reps = repsEditText.text.toString().toIntOrNull()
            if (reps != null) {
                repsCount = reps + 1
                repsEditText.setText(repsCount.toString())
            }
        }

        minusRepButton.setOnClickListener {
            if (repsCount > 0) {
                repsCount--
                repsEditText.setText(repsCount.toString())
            }
        }
        plusWeightButton.setOnClickListener {
            val weight = weightEditText.text.toString().toDoubleOrNull()
            if (weight != null) {
                weightCount = weight + 2.5
                weightEditText.setText(weightCount.toString())
            }
        }
        minusWeightButton.setOnClickListener {
            val weight = weightEditText.text.toString().toDoubleOrNull()
            if (weight != null && weight > 0) {
                weightCount = weight - 2.5
                weightEditText.setText(weightCount.toString())
            }
        }

        confirmButton.setOnClickListener {
            val reps = repsEditText.text.toString().toIntOrNull()
            val weight = weightEditText.text.toString().toDoubleOrNull()
            if (reps != null && weight != null) {
                val finalReps = reps + repsIncrementCount - repsDecrementCount
                val finalWeight = weight + (weightIncrementCount - weightDecrementCount) * 2.5

                // Do something with the final values as needed
                Log.d("Final Values", "Reps: $finalReps, Weight: $finalWeight")

                // Example: Save the final reps and weight to a database
                val stat = ExerciseStat(currentExerciseId, finalWeight, finalReps, Date())
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        viewModel.insertExerciseStat(stat)
                    }
                }

                // Reset the increment and decrement counts
                repsIncrementCount = 0
                repsDecrementCount = 0
                weightIncrementCount = 0
                weightDecrementCount = 0

                // Clear the edit text values
                repsEditText.text.clear()
                weightEditText.text.clear()
            } else {
                // Handle the case when the entered values are not valid
                Log.d("Error", "Invalid input")
            }
        }

        return binding.root
    }

    /**
     * Calculate total time of workout.
     *
     * @return
     */
    private fun calculateTotalTimeElapsed(): Long {
        val currentTime = System.currentTimeMillis()
        return currentTime - planStartTime
    }

    /**
     * Navigate to next fragment and send the time user spend on training.
     *
     * @param totalTime
     */
    private fun navigateToNextFragment(totalTime: Long) {
        val bundle = Bundle()
        bundle.putLong("elapsedTime", totalTime)
        Log.d("T", totalTime.toString())

        val navController = findNavController()
        navController.navigate(R.id.afterWorkou, bundle)
    }

    /**
     * Load image of current exercise user is training.
     *
     * @param imageView
     * @param exerciseInfo
     */
    private fun loadExerciseImage(imageView: ImageView, exerciseInfo: ExerciseInfo) {
        val resourceId = imageView.context.resources.getIdentifier(
            exerciseInfo.img,
            "drawable",
            imageView.context.packageName
        )
        Glide.with(imageView)
            .load(resourceId)
            .into(imageView)
    }
}
