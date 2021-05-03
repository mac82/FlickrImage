package com.softinsa.myapplication.data.repository

import com.softinsa.myapplication.data.api.FlickrHelper

class FlickrRepository(private val flickrHelper: FlickrHelper) {

    suspend fun getBirdsImagesList(tags:String, page:Int) = flickrHelper.getBirdsImagesList(tags, page)

    suspend fun getSizeListByImageId(imageId:String) = flickrHelper.getSizeListByImageId(imageId)

}
