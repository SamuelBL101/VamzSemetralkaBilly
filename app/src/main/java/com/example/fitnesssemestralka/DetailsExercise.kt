package com.example.fitnesssemestralka

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.fitnesssemestralka.data.FitnessDatabase
import com.example.fitnesssemestralka.data.FitnessViewModel
import com.example.fitnesssemestralka.data.FitnessViewModelFactory
import com.example.fitnesssemestralka.databinding.FragmentDetailsExerciseBinding
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Fragment that show chart of the exercise progress based on name he receive.
 *
 */
class DetailsExercise : Fragment() {
    private val viewModel: FitnessViewModel by viewModels {
        FitnessViewModelFactory(FitnessDatabase.getInstance(requireContext()).fitnessDao)
    }

    /**
     * Show the view with Chart.
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
        val binding = FragmentDetailsExerciseBinding.inflate(layoutInflater)
        val chartView = binding.chart

        val description = Description()
        description.text = "Weight Chart"
        chartView.description = description

        chartView.setTouchEnabled(true)
        chartView.setPinchZoom(true)

        val nameOfExercise = arguments?.getString("Name") ?: 0
        Log.d("NameofExercise", nameOfExercise.toString())

        lifecycleScope.launch {
            val idOfExercise = withContext(Dispatchers.IO) {
                viewModel.getIdOfExercise(nameOfExercise.toString())
            }
            val exerciseDataList = withContext(Dispatchers.IO) {
                viewModel.getAllExerciseStats(idOfExercise)
            }
            val entries = mutableListOf<Entry>()
            var previousDate: Date? = null
            var maxWeight = 0f
            var index = -1

            for (exerciseStat in exerciseDataList) {
                val currentDate = exerciseStat.date
                if (currentDate != previousDate) {
                    if (index != -1) {
                        entries.add(Entry(index.toFloat(), maxWeight))
                    }
                    previousDate = currentDate
                    maxWeight = exerciseStat.weight?.toFloat() ?: 0f
                    index++
                } else {
                    // Check if the current weight is higher than the previous max weight
                    exerciseStat.weight?.toFloat()?.let { weight ->
                        if (weight > maxWeight) {
                            maxWeight = weight
                        }
                    }
                }
            }
            if (index != -1) {
                entries.add(Entry(index.toFloat(), maxWeight))
            }

            val dataSet = LineDataSet(entries, "Max Weight")
            dataSet.color = ColorTemplate.rgb("#FF0000")
            dataSet.setDrawValues(false)

            val lineData = LineData(dataSet)
            chartView.data = lineData
            chartView.invalidate()
        }

        return binding.root
    }
}
