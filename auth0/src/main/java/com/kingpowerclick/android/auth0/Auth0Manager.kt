package com.kingpowerclick.android.auth0

import android.content.Context
import android.content.pm.PackageManager
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.provider.CustomTabsOptions
import com.auth0.android.provider.WebAuthProvider
import java.util.Date

class AuthenticationManager constructor(
    private val context: Context,
) {
    private val clientId = getMetadataValue("auth0ClientId", context)
    private val domain = getMetadataValue("auth0Domain", context)
    private val audience = getMetadataValue("auth0Audience", context)
    private val scope = getMetadataValue("auth0Scope", context)
    private val scheme = getMetadataValue("auth0Scheme", context)
    private val organization = getMetadataValue("auth0Organization", context)

    private val account =
        Auth0.getInstance(clientId!!, domain!!)

    fun getClientId() = clientId

    fun getMetadataValue(
        key: String,
        context: Context,
    ): String? =
        try {
            val applicationInfo =
                context.packageManager.getApplicationInfo(
                    context.packageName,
                    PackageManager.GET_META_DATA,
                )
            val metaData = applicationInfo.metaData
            metaData?.getString(key) // Get the value by key
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }

    suspend fun logIn(
        context: Context,
        onSuccess: suspend (result: CredentialsModel) -> Unit,
        onFail: suspend (error: ErrorModel) -> Unit,
    ) {
        try {
            val credentials =
                WebAuthProvider
                    .login(account)
                    .withParameters(
                        mapOf("organization" to organization),
                    ).withScheme(scheme!!)
                    .withAudience(audience!!)
                    .withScope(scope!!)
                    .withCustomTabsOptions(
                        options =
                            CustomTabsOptions
                                .newBuilder()
                                .showTitle(true)
                                .build(),
                    ).await(context)
            onSuccess(
                CredentialsModel(
                    idToken = credentials.idToken,
                    accessToken = credentials.accessToken,
                    type = credentials.type,
                    refreshToken = credentials.refreshToken,
                    expiresAt = credentials.expiresAt,
                    scope = credentials.scope,
                ),
            )
        } catch (error: AuthenticationException) {
            onFail(
                ErrorModel(
                    statusCode = error.statusCode,
                ),
            )
        }
    }

    suspend fun logOut(
        context: Context,
        onSuccess: suspend () -> Unit,
        onFail: suspend (error: ErrorModel) -> Unit,
    ) {
        try {
            WebAuthProvider
                .logout(account)
                .withCustomTabsOptions(
                    options =
                        CustomTabsOptions
                            .newBuilder()
                            .showTitle(true)
                            .build(),
                ).await(context)
            onSuccess()
        } catch (error: AuthenticationException) {
            onFail(
                ErrorModel(
                    statusCode = error.statusCode,
                ),
            )
        }
    }

    suspend fun refreshToken(
        refreshToken: String,
        onSuccess: suspend (credentials: CredentialsModel) -> Unit,
        onFail: suspend (error: ErrorModel) -> Unit,
    ) {
        try {
            val client =
                AuthenticationAPIClient(account)
                    .renewAuth(refreshToken)
                    .await()
            onSuccess(
                CredentialsModel(
                    idToken = client.idToken,
                    accessToken = client.accessToken,
                    type = client.type,
                    refreshToken = client.refreshToken,
                    expiresAt = client.expiresAt,
                    scope = client.scope,
                ),
            )
        } catch (error: AuthenticationException) {
            onFail(
                ErrorModel(
                    statusCode = error.statusCode,
                ),
            )
        }
    }

    /*
        not return new refreshToken
        but return refreshToken that pass as param
     */
    fun refreshTokenBySynchronous(
        clientId: String,
        refreshToken: String,
    ): CredentialsModel? {
        val response =
            RetrofitInstance
                .getInstance()
                .create(Auth0ApiInterface::class.java)
                .refreshToken(
                    RefreshTokenBody(
                        client_id = clientId,
                        refresh_token = refreshToken,
                    ),
                ).execute()
        return if (response.isSuccessful) {
            if (response.body() != null) {
                val dateTimeAsLong: Long = response.body()!!.expiresIn
                val date = Date(dateTimeAsLong)
                CredentialsModel(
                    idToken = response.body()!!.idToken,
                    accessToken = response.body()!!.accessToken,
                    refreshToken = refreshToken,
                    type = response.body()!!.type,
                    expiresAt = date,
                    scope = response.body()!!.scope,
                )
            } else {
                null
            }
        } else {
            null
        }
    }
}
