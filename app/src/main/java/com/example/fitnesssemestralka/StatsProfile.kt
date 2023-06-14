package com.example.fitnesssemestralka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnesssemestralka.data.FitnessDatabase
import com.example.fitnesssemestralka.data.FitnessViewModel
import com.example.fitnesssemestralka.data.FitnessViewModelFactory
import com.example.fitnesssemestralka.databinding.FragmentStatsProfileBinding

/**
 *  Fragment that displays the statistics for exercises in a RecyclerView.
 *
 */
class StatsProfile : Fragment() {
    private val viewModel: FitnessViewModel by viewModels {
        FitnessViewModelFactory(FitnessDatabase.getInstance(requireContext()).fitnessDao)
    }

    /**
     * Inflate the layout for the fragment, set up the RecyclerView, and observe changes in the exercise data.
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
        val binding = FragmentStatsProfileBinding.inflate(layoutInflater)
        val recyclerView = binding.statsRecyclerView
        var navController = findNavController()
        val layoutManager = LinearLayoutManager(requireContext())
        val exerciseAdapter = StatsExerciseAdapter(emptyList(),navController) // Pass an empty list initially
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = exerciseAdapter
        viewModel.getAllExercises().observe(viewLifecycleOwner) { exercises ->
            // Update the adapter with the new data
            exerciseAdapter.updateExercises(exercises)
        }
        return binding.root
    }



}
