package com.kingpowerclick.android.auth0

import com.google.gson.annotations.SerializedName
import java.util.Date

data class RefreshTokenResponseModel(
    @SerializedName("id_token")
    val idToken :String,
    @SerializedName("access_token")
    val accessToken :String,
    @SerializedName("token_type")
    val type :String,
    @SerializedName("expires_in")
    val expiresIn : Long,
    @SerializedName("scope")
    val scope :String?,
)
