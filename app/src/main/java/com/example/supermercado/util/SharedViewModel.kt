package com.example.supermercado.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {

    private val _refreshAction = MutableLiveData<Boolean>()
    val refreshAction: LiveData<Boolean> get() = _refreshAction

    fun triggerRefreshAction() {
        _refreshAction.value = true
    }
}