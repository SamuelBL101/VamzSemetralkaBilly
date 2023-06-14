package com.example.fitnesssemestralka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesssemestralka.data.FitnessDatabase
import com.example.fitnesssemestralka.data.FitnessViewModel
import com.example.fitnesssemestralka.data.FitnessViewModelFactory
import com.example.fitnesssemestralka.databinding.FragmentStartPlansBinding

/**
 *  Fragment responsible for displaying a list of gym plans and allowing the user to select a plan to start.
 *
 */
class StartPlans : Fragment() {
    private lateinit var planRecyclerView: RecyclerView
    private lateinit var planAdapter: PlanAdapter
    private lateinit var navController: NavController

    private val viewModel: FitnessViewModel by viewModels{
        FitnessViewModelFactory(FitnessDatabase.getInstance(requireContext()).fitnessDao)
    }

    /**
     * Create the view of the fragment.
     * Layout is inflated using FragmentStartPlansBinding.
     * The plans are obtained from the FitnessViewModel.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentStartPlansBinding.inflate(inflater, container, false)
        planRecyclerView = binding.planRecyclerView
        navController = findNavController()

        planAdapter = PlanAdapter(emptyList(),navController)

        planRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        planRecyclerView.adapter = planAdapter

        // Observe changes in the plan names
        viewModel.getAllPlans().observe(viewLifecycleOwner) { plans ->
            planAdapter.updateGymPlans(plans)
        }

        return binding.root
    }


}