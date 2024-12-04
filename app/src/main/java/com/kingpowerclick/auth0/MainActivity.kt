package com.kingpowerclick.auth0

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kingpowerclick.android.auth0.AuthenticationManager
import com.kingpowerclick.android.auth0.CredentialsModel
import com.kingpowerclick.auth0.ui.theme.Auth0Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var authenticationManager: AuthenticationManager
    private var refreshToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authenticationManager = AuthenticationManager(this)
        enableEdgeToEdge()
        setContent {
            val scope = rememberCoroutineScope()

            Auth0Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column {
                        Greeting(
                            name = "Android",
                            modifier = Modifier.padding(innerPadding),
                        )
                        Button(
                            onClick = {
                                scope.launch {
                                    authenticationManager.logIn(
                                        context = this@MainActivity,
                                        onSuccess = { result: CredentialsModel ->
                                            Log.d("asdf", "${result.idToken}")
                                            Log.d("asdf", "${result.accessToken}")
                                            Log.d("asdf", "${result.type}")
                                            Log.d("asdf", "${result.refreshToken}")
                                            Log.d("asdf", "${result.expiresAt}")
                                            Log.d("asdf", "${result.scope}")
                                            refreshToken = result.refreshToken!!
                                        },
                                        onFail = {},
                                    )
                                }
                            },
                            content = {
                            },
                        )
                        Button(
                            onClick = {
                                scope.launch {
                                    authenticationManager.logOut(
                                        context = this@MainActivity,
                                        onSuccess = {
                                            val a = 1
                                        },
                                        onFail = {},
                                    )
                                }
                            },
                            content = {
                            },
                        )
                        Button(
                            onClick = {
                                scope.launch {
                                    authenticationManager.refreshToken(
                                        refreshToken = refreshToken,
                                        onSuccess = {credentials: CredentialsModel ->

                                            val a = credentials.refreshToken
                                        },
                                        onFail = {},
                                    )
                                }
                            },
                            content = {
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Auth0Theme {
        Greeting("Android")
    }
}
