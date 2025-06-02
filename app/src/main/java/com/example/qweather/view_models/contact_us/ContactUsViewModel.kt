package com.example.qweather.view_models.contact_us

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.repository.ContactUsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactUsViewModel(private val repository: ContactUsRepository) : ViewModel() {

    private val _response = MutableLiveData<NetworkResult<String>>()
    val response: LiveData<NetworkResult<String>> = _response

    fun sendContactMessage(name: String, email: String, subject: String, message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _response.postValue(NetworkResult.Loading())

            val result = repository.sendContactMessage(name, email, subject, message)
            _response.postValue(result)
        }
    }
}
