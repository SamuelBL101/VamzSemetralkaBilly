package com.example.fitnesssemestralka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.fitnesssemestralka.data.FitnessDatabase
import com.example.fitnesssemestralka.data.FitnessViewModel
import com.example.fitnesssemestralka.data.FitnessViewModelFactory
import com.example.fitnesssemestralka.databinding.FragmentProfileBinding
import java.text.DecimalFormat

/**
 * Fragment responsible for displaying the user's profile information, such as weight, height, and BMI (Body Mass Index).
 *
 */
class Profile : Fragment(R.layout.activity_main) {

    private val viewModel: FitnessViewModel by viewModels {
        FitnessViewModelFactory(FitnessDatabase.getInstance(requireContext()).fitnessDao)
    }

    /**
     * Assign click listeners to navigate to other destinations using the NavController.
     * The user's weight and height are displayed, and the BMI is calculated using the formula weight / (height^2).
     * If the user information is not available, the UI shows "N/A" for weight and height, and hides the conditionalEditText view.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =FragmentProfileBinding.inflate(layoutInflater)
        binding.profileWeight.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_profileSettings)
        }
        binding.profileHeight.setOnClickListener{
            findNavController().navigate(R.id.action_profile_to_profileSettings)
        }
        binding.myButton.setOnClickListener{
            findNavController().navigate(R.id.statsProfile)
        }
        viewModel.readLatestUser().observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.profileWeight.text = "Weight: ${user.weight} kg"
                binding.profileHeight.text = "Height: ${user.height} cm"
                val heightInM:Double = user.height.toDouble() /100
                val bmi:Double = user.weight/(heightInM* heightInM)
                binding.conditionalEditText.visibility = View.VISIBLE
                val decimalFormat = DecimalFormat("#.00")
                val bmiFormatted = decimalFormat.format(bmi)
                binding.conditionalEditText.text = "BMI: $bmiFormatted"
                if (bmi < 18.5){
                    binding.conditionalEditText.setBackgroundResource(R.color.blue)}
                else if (bmi >= 18.5 && bmi < 25){
                    binding.conditionalEditText.setBackgroundResource(R.color.green)}
                else if (bmi >= 25 && bmi < 30){
                    binding.conditionalEditText.setBackgroundResource(R.color.yellow)}
                else if (bmi >= 30 && bmi < 35){
                    binding.conditionalEditText.setBackgroundResource(R.color.orange)}
                else {
                    binding.conditionalEditText.setBackgroundResource(R.color.purple)}
            } else {
                binding.profileWeight.text = "Weight: N/A"
                binding.profileHeight.text = "Height: N/A"
                binding.conditionalEditText.visibility = View.GONE
            }
        }
        return binding.root
    }


}