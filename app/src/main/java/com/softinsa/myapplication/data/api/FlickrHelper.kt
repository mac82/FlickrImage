package com.softinsa.myapplication.data.api

import com.softinsa.myapplication.BuildConfig
import com.softinsa.myapplication.data.api.interfaces.IFlickrService
import com.softinsa.myapplication.data.network.RetrofitBuilder

class FlickrHelper(private val flickrService: IFlickrService) {

    suspend fun getBirdsImagesList(tags:String, page:Int) =
        flickrService.getBirdsImagesList(
            queryMethod = "flickr.photos.search",
            api_key = BuildConfig.API_KEY,
            tags,
            page,
            format = "json",
            nojsoncallback = 1
    )


    suspend fun getSizeListByImageId(imageId:String) =
        flickrService.getSizeListByImageId(
            queryMethod = "flickr.photos.getSizes",
            api_key = BuildConfig.API_KEY,
            photo_id = imageId,
            format = "json",
            nojsoncallback = 1
        )
}