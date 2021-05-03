package com.softinsa.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class ImageSizesResponseModel(
        @SerializedName("sizes") val sizes: ImageSizeModel,
        @SerializedName("stat") val stat: String
)