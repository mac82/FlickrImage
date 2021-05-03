package com.softinsa.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class ImageSizeModel(
    @SerializedName("canblog") val canblog: Int,
    @SerializedName("canprint") val canprint: String,
    @SerializedName("candownload") val candownload: String,
    @SerializedName("size") val imageSizeDetailList: List<ImageSizeDetailModel>
)