package com.example.fitnesssemestralka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fitnesssemestralka.data.FitnessDatabase
import com.example.fitnesssemestralka.data.FitnessViewModel
import com.example.fitnesssemestralka.data.FitnessViewModelFactory
import com.example.fitnesssemestralka.data.User
import com.example.fitnesssemestralka.databinding.FragmentProfileSettingsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Fragment responsible for allowing the user to update their profile information, specifically weight and height.
 *
 */
class ProfileSettings : Fragment() {

    private val viewModel: FitnessViewModel by viewModels{
        FitnessViewModelFactory(FitnessDatabase.getInstance(requireContext()).fitnessDao)
    }

    /**
     * Creating view and sett up the onClickListener.
     * The user information is added to the database in the background IO thread.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentProfileSettingsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        binding.addButton.setOnClickListener {
            val weightText = binding.editWeight.text.toString()
            val heightText = binding.editHeight.text.toString()

            if (weightText.isEmpty() || heightText.isEmpty()) {
                Toast.makeText(requireContext(), "Weight or height is empty", Toast.LENGTH_SHORT).show()
            } else {
                val weight = weightText.toInt()
                val height = heightText.toInt()
                val user = User(weight, height, Date())

                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        viewModel.addUserInfo(user)
                    }
                    findNavController().navigate(R.id.action_profileSettings_to_profile)
                }
            }
        }
        return binding.root
    }
}