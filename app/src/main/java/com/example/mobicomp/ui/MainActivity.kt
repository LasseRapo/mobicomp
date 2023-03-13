package com.example.mobicomp.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.mobicomp.ui.theme.MobicompTheme
import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.example.mobicomp.navigation.MainNavigation
import com.google.android.gms.location.LocationRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("username", "User123") //Username: User123
        editor.putString("password", "Pass123") //Password: Pass123

        LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        editor.apply()
        setContent {
            MobicompTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainNavigation(sharedPreferences = sharedPreferences)
                }
            }
        }
    }
}
