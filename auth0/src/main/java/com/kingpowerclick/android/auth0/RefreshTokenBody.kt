package com.kingpowerclick.android.auth0

data class RefreshTokenBody(
    val grant_type: String = "refresh_token",
    val client_id: String,
    val refresh_token: String,
)
