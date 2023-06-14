package com.example.fitnesssemestralka

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnesssemestralka.data.FitnessDatabase
import com.example.fitnesssemestralka.data.FitnessViewModel
import com.example.fitnesssemestralka.data.FitnessViewModelFactory
import com.example.fitnesssemestralka.data.GymPlan
import com.example.fitnesssemestralka.databinding.FragmentExerciseListBinding
import kotlinx.coroutines.launch

/**
 * Fragment that call adapter of exercises.
 * Based on user input create workout plan of exercises chosen and name chosen
 *
 */
class ExerciseListFragment : Fragment() {
    private val viewModel: FitnessViewModel by viewModels {
        FitnessViewModelFactory(FitnessDatabase.getInstance(requireContext()).fitnessDao)
    }

    /**
     * Create the view based on adapter and check user input.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentExerciseListBinding.inflate(layoutInflater)
        val recyclerView = binding.recyclerView
        // Create an instance of the ViewModel
        // Set up the RecyclerView
        val layoutManager = LinearLayoutManager(requireContext())
        val exerciseAdapter = ExerciseAdapter(emptyList()) // Pass an empty list initially
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = exerciseAdapter

        // Observe changes in the data
        viewModel.getAllExercises().observe(viewLifecycleOwner) { exercises ->
            // Update the adapter with the new data
            exerciseAdapter.updateExercises(exercises)
        }
        binding.buttonAdd.setOnClickListener {
            val selectedExerciseIds = exerciseAdapter.getSelectedExerciseIds()

            // Create an AlertDialog to capture the plan name input
            val dialogBuilder = AlertDialog.Builder(requireContext())
            val input = EditText(requireContext())
            dialogBuilder.setView(input)
            dialogBuilder.setTitle("Enter Plan Name")
            dialogBuilder.setPositiveButton("Save") { dialog, _ ->
                val planName = input.text.toString()
                if (planName.isNotBlank()) {
                    val gymPlan = GymPlan(planName, selectedExerciseIds)

                    // Call your insertGymPlan method to insert the new plan into the database
                    lifecycleScope.launch {
                        viewModel.insertGymPlan(gymPlan)
                    }
                    val navController = findNavController()
                    navController.popBackStack(R.id.home2, false)
                    dialog.dismiss()
                } else {
                    // Display an error message if the plan name is empty
                    Toast.makeText(
                        requireContext(),
                        "Plan name cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
            dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = dialogBuilder.create()
            dialog.show()
        }

        return binding.root
    }

}

