package com.kingpowerclick.android.auth0

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Auth0ApiInterface {
    @POST("oauth/token")
    fun refreshToken(
        @Body refreshToken: RefreshTokenBody,
    ): Call<RefreshTokenResponseModel>
}
