package br.com.superid

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.superid.ui.theme.SuperIDTheme

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContent {
            SuperIDTheme {
                SplashScreen(onTimeout = {
                    navigateToHostActivity()
                })
            }
        }
    }

    private fun navigateToHostActivity() {
        startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
        finish()
    }
}