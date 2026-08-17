package com.fortytwo.swifty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.fortytwo.swifty.ui.navigation.SwiftyNavHost
import com.fortytwo.swifty.ui.theme.Swifty_CompanionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Swifty_CompanionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SwiftyNavHost(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}