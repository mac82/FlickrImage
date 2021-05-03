package com.softinsa.myapplication.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.softinsa.myapplication.data.api.FlickrHelper
import com.softinsa.myapplication.data.repository.FlickrRepository
import com.softinsa.myapplication.ui.main.viewmodel.MainViewModel

class ViewModelFactory(private val flickrHelper: FlickrHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(FlickrRepository(flickrHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}