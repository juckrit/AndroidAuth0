package com.kingpowerclick.auth0

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kingpowerclick.android.auth0.AuthenticationManager
import com.kingpowerclick.android.auth0.CredentialsModel
import com.kingpowerclick.auth0.ui.theme.Auth0Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var authenticationManager: AuthenticationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authenticationManager = AuthenticationManager(this)
        enableEdgeToEdge()
        setContent {
            val scope = rememberCoroutineScope()
            var accessToken by remember {
                mutableStateOf("")
            }
            var refreshToken by remember {
                mutableStateOf("")
            }
            Auth0Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier =
                            Modifier
                                .verticalScroll(rememberScrollState()),
                    ) {
                        Greeting(
                            modifier = Modifier.padding(innerPadding),
                        )
                        Button(
                            shape = RectangleShape,
                            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
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
                                            accessToken = result.accessToken!!
                                            refreshToken = result.refreshToken!!
                                        },
                                        onFail = {
                                            Toast
                                                .makeText(
                                                    this@MainActivity,
                                                    "logIn Failed",
                                                    Toast.LENGTH_SHORT,
                                                ).show()
                                        },
                                    )
                                }
                            },
                            content = {
                                Text(
                                    text =
                                        if (accessToken.isBlank()) {
                                            "Login"
                                        } else {
                                            "Already Login accessToken = $accessToken"
                                        },
                                )
                            },
                        )
                        Button(
                            shape = RectangleShape,
                            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                            onClick = {
                                scope.launch {
                                    authenticationManager.logOut(
                                        context = this@MainActivity,
                                        onSuccess = {
                                            accessToken = ""
                                            val a = 1
                                        },
                                        onFail = {
                                            Toast
                                                .makeText(
                                                    this@MainActivity,
                                                    "logOut Failed",
                                                    Toast.LENGTH_SHORT,
                                                ).show()
                                        },
                                    )
                                }
                            },
                            content = {
                                Text(
                                    text =
                                        if (accessToken.isBlank()) {
                                            "Already Logout"
                                        } else {
                                            "Logout"
                                        },
                                )
                            },
                        )
                        Button(
                            shape = RectangleShape,
                            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                            onClick = {
                                scope.launch {
                                    authenticationManager.refreshToken(
                                        refreshToken = refreshToken,
                                        onSuccess = { credentials: CredentialsModel ->
                                            accessToken = credentials.accessToken!!
                                        },
                                        onFail = {
                                            Toast
                                                .makeText(
                                                    this@MainActivity,
                                                    "refreshToken Failed",
                                                    Toast.LENGTH_SHORT,
                                                ).show()
                                        },
                                    )
                                }
                            },
                            content = {
                                Text(
                                    text =
                                        if (accessToken.isBlank()) {
                                            "Login to enable refreshToken"
                                        } else {
                                            "accessToken = $accessToken"
                                        },
                                )
                            },
                            enabled = accessToken.isEmpty().not(),
                        )
                        Button(
                            shape = RectangleShape,
                            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    val result =
                                        authenticationManager.refreshTokenBySynchronous(
                                            context = this@MainActivity,
                                            clientId =
                                                authenticationManager
                                                    .getClientId()
                                                    .orEmpty(),
                                            refreshToken = refreshToken,
                                        )
                                    accessToken = result?.accessToken.orEmpty()
                                }
                            },
                            content = {
                                Text(
                                    text =
                                        if (accessToken.isBlank()) {
                                            "Login to enable refreshToken synchronous"
                                        } else {
                                            "accessToken = $accessToken"
                                        },
                                )
                            },
                            enabled = accessToken.isEmpty().not(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String = stringResource(R.string.app_name),
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
        Greeting(stringResource(R.string.app_name))
    }
}
