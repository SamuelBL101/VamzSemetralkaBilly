package com.example.fitnesssemestralka

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fitnesssemestralka.databinding.FragmentHomeBinding

/**
 * Fragment showed when app start or called to home.
 *
 */
class Home : Fragment() {
    /**
     * Set up the view.
     * Add OnClickListeners to move to other fragments.
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
        val homeBinding = FragmentHomeBinding.inflate(layoutInflater)
        homeBinding.profilebutton.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_profile)
        }
        homeBinding.planButton.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_plansShow)
        }
        homeBinding.playButton.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_startPlans)
        }

        return homeBinding.root
    }

}