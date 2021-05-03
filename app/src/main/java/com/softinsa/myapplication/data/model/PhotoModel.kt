package com.softinsa.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class PhotoModel(
    @SerializedName("page") val page: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("perpage") val perpage: Int,
    @SerializedName("total") val total: String,
    @SerializedName("photo") val photoList: List<PhotoDetailModel>
)
