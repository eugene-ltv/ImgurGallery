package com.saiferwp.imgurgallery.api.response

import com.google.gson.annotations.SerializedName

abstract class Response(
    @SerializedName("success")
    private val success: Boolean = false,
    @SerializedName("status")
    private val status: Int = 0
)