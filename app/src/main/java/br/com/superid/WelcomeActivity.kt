package br.com.superid

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import br.com.superid.navigation.AppNavigation
import br.com.superid.ui.theme.SuperIDTheme

class WelcomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Solicitar para não exibir a barra de título
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContent {
            SuperIDTheme { // Aplicar o tema customizado
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}