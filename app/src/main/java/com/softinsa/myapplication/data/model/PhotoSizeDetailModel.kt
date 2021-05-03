package com.softinsa.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class PhotoSizeDetailModel(
    @SerializedName("label") val label: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("source") val source: String,
    @SerializedName("url") val url: String,
    @SerializedName("media") val media: String,
)
