package com.kingpower.library.authentication

import android.content.Context
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.provider.CustomTabsOptions
import com.auth0.android.provider.WebAuthProvider
import com.kingpowerclick.android.auth0.CredentialsModel
import com.kingpowerclick.android.auth0.ErrorModel

class Authentication constructor(
    private val clientId: String,
    private val domain: String,
    private val audience: String,
    private val scope: String,
) {
    private val account =
        Auth0(
            clientId = clientId,
            domain = domain,
        )

    suspend fun logIn(
        context: Context,
        onSuccess: suspend (result: CredentialsModel) -> Unit,
        onFail: suspend (error: ErrorModel) -> Unit,
    ) {
        try {
            val credentials =
                WebAuthProvider
                    .login(account)
                    .withAudience(audience)
                    .withScope(scope)
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
}
