package com.example.fitnesssemestralka.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitnesssemestralka.data.FitnessDao

class FitnessViewModelFactory(private val dao: FitnessDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FitnessViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FitnessViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
