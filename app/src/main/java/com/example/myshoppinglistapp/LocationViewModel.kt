package com.example.myshoppinglistapp

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LocationViewModel: ViewModel() {
    private val _location = mutableStateOf<LocationData?>(null)
    val location : State<LocationData?> = _location

    private val _address = mutableStateOf(listOf<GeocodingResult>())

    val address : State<List<GeocodingResult>> = _address


    fun updateLocation(newLocation: LocationData){
        _location.value = newLocation
    }

    fun fetchAddress(latlng: String) {
        Log.d("ViewModelFetch", "fetchAddress called with: $latlng") // <--- ADD THIS
        try {
            Log.d("ViewModelFetch", "Attempting to launch coroutine...") // <--- ADD THIS
            viewModelScope.launch {
                Log.d("ViewModelFetch", "Coroutine launched.") // <--- ADD THIS
                try {
                    // ... rest of your coroutine code ...
                    val result = RetrofitClient.create().getAddressFromCoordinates(
                        latlng,
                        "AIzaSyA6DB4Fg7AbmNEfoqTDY0f89g-ZGgaeoXs"
                    )
                    _address.value = result.results
                    Log.d("ViewModelFetch", "API Result: $result")
                    Log.d("ViewModelFetch", "Result.results: ${result.results}")
                    Log.d("ViewModelAddress", "Address updated in ViewModel: ${_address.value.firstOrNull()?.formatted_address}")
                } catch (e: Exception) {
                    Log.e("ViewModelFetchError", "Error fetching address in coroutine: ${e.message}", e)
                }
            }
            Log.d("ViewModelFetch", "Coroutine launch call completed.") // <--- ADD THIS (will appear before coroutine body finishes)
        } catch (e: Exception) {
            Log.e("ViewModelLaunchError", "Error launching coroutine or in outer try: ${e.message}", e)
        }
    }



}