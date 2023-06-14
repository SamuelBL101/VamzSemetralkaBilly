package com.example.fitnesssemestralka

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fitnesssemestralka.data.FitnessDatabase
import com.example.fitnesssemestralka.data.FitnessViewModel
import com.example.fitnesssemestralka.data.FitnessViewModelFactory
import com.example.fitnesssemestralka.data.GymPlan
import com.example.fitnesssemestralka.databinding.FragmentPlansShowBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Fragment responsible for displaying a list of gym plan names and providing options to delete plans.
 *
 */
class PlansShow : Fragment() {
    private val viewModel: FitnessViewModel by viewModels {
        FitnessViewModelFactory(FitnessDatabase.getInstance(requireContext()).fitnessDao)
    }
    private lateinit var nameListContainer:LinearLayout

    /**
     * Create the view of the fragment.
     * Two buttons, buttonDelete and buttonAdd, are set up with click listeners.
     * ButtonDelete invokes the showPlanSelectionDialog method to show a dialog for plan selection.
     * ButtonAdd navigates to another destination using the NavController.
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
        val binding = FragmentPlansShowBinding.inflate(layoutInflater)
         nameListContainer = binding.nameListContainer

        // Retrieve the list of plan names from the database
        val planNames = viewModel.getPlanNames()

        // Iterate through the plan names and create TextViews to display them
        planNames.observe(viewLifecycleOwner) { names ->
            names?.let { planNamesList ->
                for (name in planNamesList) {
                    val textView = TextView(requireContext())
                    textView.text = name
                    textView.textSize = 40F
                    nameListContainer.addView(textView)
                }
            }
        }
        val buttonDelete = binding.buttonDeletePlan
        buttonDelete.setOnClickListener {
            showPlanSelectionDialog()
        }
        val buttonAdd = binding.buttonAddPlan
        buttonAdd.setOnClickListener {
            findNavController().navigate(R.id.action_plansShow_to_exerciseListFragment)
        }

        return binding.root
    }

    /**
     *  Method creates an AlertDialog to display a list of plan names retrieved from the database.
     *
     */
    private fun showPlanSelectionDialog() {
        Log.d("PlansShow", "showPlanSelectionDialog() called") // Log statement to verify function call

        viewModel.getPlanNames().observe(viewLifecycleOwner) { names ->
            names?.let { planNamesList ->
                val planArray = planNamesList.toTypedArray()
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Select a plan to delete")

                builder.setItems(planArray) { _, which ->
                    val selectedPlanName = planArray[which]
                    lifecycleScope.launch {
                        deletePlan(selectedPlanName)
                    }
                }
                builder.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                builder.setPositiveButton("Delete") { dialog, _ ->
                }

                val dialog = builder.create() // Create the dialog here

                dialog.show() // Show the dialog
            }
        }
    }

    /**
     * Method deletes the selected plan by calling viewModel.deleteGymPlan(planName).
     *
     * @param planName
     */
    private suspend fun deletePlan(planName: String) {
        // Call your delete plan function in the ViewModel to delete the selected plan
            withContext(Dispatchers.IO) {
                viewModel.deleteGymPlan(planName)
            }
        // Remove the observer for the plan names list
        viewModel.getPlanNames().removeObservers(viewLifecycleOwner)

        val updatedPlanNames = viewModel.getPlanNames()

        // Observe the updated plan names list to update the UI
        updatedPlanNames.observe(viewLifecycleOwner) { names ->
            names?.let { planNamesList ->
                // Clear the existing views
                nameListContainer.removeAllViews()

                // Add the updated plan names to the layout
                for (name in planNamesList) {
                    val textView = TextView(requireContext())
                    textView.text = name
                    textView.textSize = 40F
                    nameListContainer.addView(textView)
                }
            }
        }
    }
}