package com.kingpowerclick.android.auth0

import java.util.Date

data class CredentialsModel(
    val idToken :String,
    val accessToken :String,
    val type :String,
    val refreshToken :String?,
    val expiresAt : Date,
    val scope :String?,
)
