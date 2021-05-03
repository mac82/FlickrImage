package com.softinsa.myapplication.data.api.interfaces

import com.softinsa.myapplication.data.model.ImageResponseModel
import com.softinsa.myapplication.data.model.ImageSizesResponseModel
import retrofit2.http.GET
import retrofit2.http.Query


interface IFlickrService {

    @GET("services/rest/")
    suspend fun getBirdsImagesList(
        @Query("method") queryMethod:String,
        @Query("api_key") apiKey:String ,
        @Query("tags") tags:String,
        @Query("page") page:Int,
        @Query("format") format:String,
        @Query("nojsoncallback") nojsoncallback:Int
    ): ImageResponseModel

    @GET("services/rest/")
    suspend fun getSizeListByImageId(
        @Query("method") queryMethod:String,
        @Query("api_key") apiKey:String ,
        @Query("photo_id") imageId:String,
        @Query("format") format:String,
        @Query("nojsoncallback") nojsoncallback:Int
    ): ImageSizesResponseModel
}