package com.softinsa.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class PhotoSizesResponseModel(
    @SerializedName("sizes") val sizes: PhotoSizeModel,
    @SerializedName("stat") val stat: String
)