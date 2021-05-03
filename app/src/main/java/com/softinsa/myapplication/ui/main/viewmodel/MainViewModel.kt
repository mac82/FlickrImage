package com.softinsa.myapplication.ui.main.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import com.softinsa.myapplication.data.repository.FlickrRepository
import com.softinsa.myapplication.data.network.utils.ResourceNetworkCall


class MainViewModel(private val mainRepository: FlickrRepository) : ViewModel() {

    fun getBirdsImagesList(tags:String, page:Int) =
        liveData(Dispatchers.IO) {
            emit(ResourceNetworkCall.loading(data = null))
            try {
                emit(ResourceNetworkCall.success(data = mainRepository.getBirdsImagesList(tags, page)))
            } catch (exception: Exception) {
                emit(ResourceNetworkCall.error(data = null, message = exception.message
                        ?: "Error Occurred!"))
            }
        }

    fun getSizeListByImageId(imageId:String) =
        liveData(Dispatchers.IO) {
            emit(ResourceNetworkCall.loading(data = null))
            try {
                emit(ResourceNetworkCall.success(data = mainRepository.getSizeListByImageId(imageId)))
            } catch (exception: Exception) {
                emit(ResourceNetworkCall.error(data = null, message = exception.message
                    ?: "Error Occurred!"))
            }
        }

}