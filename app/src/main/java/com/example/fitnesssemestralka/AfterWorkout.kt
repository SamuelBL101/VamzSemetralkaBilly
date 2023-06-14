package com.example.fitnesssemestralka

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fitnesssemestralka.databinding.FragmentAfterWorkouBinding
import com.example.fitnesssemestralka.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment that show after completed workout total time and set Button to return to home.
 *
 */
class AfterWorkout : Fragment() {
    //private var elapsedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    /**
     * Create the view of Fragment and show information based on parameters he receive.
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
        val binding = FragmentAfterWorkouBinding.inflate(layoutInflater)
        val args = arguments
        val elapsedTime = args?.getLong("elapsedTime") ?: 0
        Log.d("ElapsedTime", elapsedTime.toString()) // Log statement to verify function call

        val formattedElapsedTime = formatElapsedTime(elapsedTime)
        binding.userInputEditText.text =
            Editable.Factory.getInstance().newEditable(formattedElapsedTime)
        // Inflate the layout for this fragment
        binding.submitButton.setOnClickListener {
            findNavController().navigate(R.id.home2)
        }
        return binding.root
    }

    /**
     * Format time from Long to HH:mm:ss time format
     *
     * @param elapsedTimeInMillis
     * @return
     */
    private fun formatElapsedTime(elapsedTimeInMillis: Long): String {
        val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return formatter.format(Date(elapsedTimeInMillis))
    }


}