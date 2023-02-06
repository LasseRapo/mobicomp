package com.example.mobicomp.ui.login

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController

@Composable
fun LoginScreen(
    navController: NavController,
    prefs: SharedPreferences,
    context: Context
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val username = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }

        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                painter = rememberVectorPainter(Icons.Filled.Person),
                contentDescription = "login_image",
                modifier = Modifier.fillMaxWidth().size(150.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = username.value,
                onValueChange = { text -> username.value = text },
                label = { Text(text = "Username") },
                shape = RoundedCornerShape(corner = CornerSize(50.dp))
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password.value,
                onValueChange = { passwordString -> password.value = passwordString },
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(corner = CornerSize(50.dp))
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { checkPassword(context, username.value, password.value, sharedPreferences = prefs, navController = navController) },
                enabled = true,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(corner = CornerSize(50.dp))
            ) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = { showBiometricPrompt(context, navController) },
                enabled = true,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(corner = CornerSize(50.dp))
            ) {
                Text(text = "Biometric authentication")
            }
        }
    }
}

private fun checkPassword(
    context: Context,
    username: String,
    password: String,
    sharedPreferences: SharedPreferences,
    navController: NavController
) {
    val storedUsername = sharedPreferences.getString("username", "")
    val storedPassword = sharedPreferences.getString("password", "")

    if (username == storedUsername && password == storedPassword) {
        navController.navigate(route = "home")
    } else {
        Toast.makeText(context, "Incorrect username or password", Toast.LENGTH_SHORT).show()
    }
}

// Biometric authenticator source code:
// https://fvilarino.medium.com/adding-a-pin-screen-with-biometric-authentication-in-jetpack-compose-a9bf7bd8acc9

private val biometricsIgnoredErrors = listOf(
    BiometricPrompt.ERROR_NEGATIVE_BUTTON,
    BiometricPrompt.ERROR_CANCELED,
    BiometricPrompt.ERROR_USER_CANCELED,
    BiometricPrompt.ERROR_NO_BIOMETRICS
)

private fun showBiometricPrompt(context: Context, navController: NavController) {

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric authentication")
        .setNegativeButtonText("Cancel")
        .build()

    val biometricPrompt = BiometricPrompt(
        context as FragmentActivity,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                navController.navigate(route = "home")
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                if (biometricsIgnoredErrors.contains(errorCode)) return
                Toast.makeText(
                    context,
                    "Error occurred: $errString",
                    Toast.LENGTH_LONG
                ).show()
            }
            override fun onAuthenticationFailed() {
                Toast.makeText(
                    context,
                    "Authentication failed",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    )
    biometricPrompt.authenticate(promptInfo)
}