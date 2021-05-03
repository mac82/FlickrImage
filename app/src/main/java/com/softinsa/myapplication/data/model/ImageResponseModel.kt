package com.softinsa.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class ImageResponseModel (
        @SerializedName("photos") val images: ImageModel,
        @SerializedName("stat") val stat: String
    )
