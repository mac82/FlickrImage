package com.softinsa.myapplication.data.api.interfaces

import com.softinsa.myapplication.data.model.PhotoModel
import com.softinsa.myapplication.data.model.PhotoResponseModel
import com.softinsa.myapplication.data.model.PhotoSizesResponseModel
import retrofit2.http.GET
import retrofit2.http.Query


interface IFlickrService {

    @GET("services/rest/")
    suspend fun getBirdsImagesList(
        @Query("method") queryMethod:String,
        @Query("api_key") api_key:String ,
        @Query("tags") tags:String,
        @Query("page") page:Int,
        @Query("format") format:String,
        @Query("nojsoncallback") nojsoncallback:Int
    ): PhotoResponseModel

    @GET("services/rest/")
    suspend fun getSizeListByImageId(
        @Query("method") queryMethod:String,
        @Query("api_key") api_key:String ,
        @Query("photo_id") photo_id:String,
        @Query("format") format:String,
        @Query("nojsoncallback") nojsoncallback:Int
    ): PhotoSizesResponseModel
}