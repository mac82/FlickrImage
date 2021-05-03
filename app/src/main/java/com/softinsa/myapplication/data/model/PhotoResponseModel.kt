package com.softinsa.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class PhotoResponseModel (
    @SerializedName("photos") val photos: PhotoModel,
    @SerializedName("stat") val stat: String
    )
