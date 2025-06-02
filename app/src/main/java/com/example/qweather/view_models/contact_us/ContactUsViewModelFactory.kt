package com.example.qweather.view_models.contact_us

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.qweather.repository.ContactUsRepository

class ContactUsViewModelFactory(
    private val repository: ContactUsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContactUsViewModel(repository) as T
    }
}

