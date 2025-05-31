package br.com.superid


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.superid.ui.theme.SuperIDTheme
import kotlinx.coroutines.delay


private const val SPLASH_TIMEOUT_MS: Long = 3000L

private const val PROGRESS_UPDATE_INTERVAL_MS: Long = 50L

@SuppressLint("CustomSplashScreen")
// Activity de carregamento inicial (
class LoadingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Define o conteúdo da tela com o tema
        setContent {
            SuperIDTheme {
                LoadingScreen(
                    onTimeout = {
                        navigateToWelcomeActivity()
                    }
                )
            }
        }
    }

    // Navega para a próxima tela após o carregamento
    private fun navigateToWelcomeActivity() {
        startActivity(Intent(this@LoadingActivity, TermsActivity::class.java))
        finish() // Finaliza a splash screen
    }
}

@Composable
fun LoadingScreen(onTimeout: () -> Unit) {

    var progress by remember { mutableFloatStateOf(0f) }


    val totalSteps = (SPLASH_TIMEOUT_MS / PROGRESS_UPDATE_INTERVAL_MS).toInt()


    LaunchedEffect(Unit) {
        for (i in 1..totalSteps) {
            progress = i.toFloat() / totalSteps
            delay(PROGRESS_UPDATE_INTERVAL_MS)
        }
        delay(100)
        onTimeout()
    }

    // Interface da tela
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo do SuperID
                Image(
                    painter = painterResource(id = R.drawable.logo_superid),
                    contentDescription = "SuperID Logo",
                    modifier = Modifier.size(200.dp)
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Barra de progresso animada
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .width(250.dp)
                        .height(10.dp),
                    color = Color(0xFF0E2F5A),
                    trackColor = Color(0xFFAAAAAA),
                    strokeCap = StrokeCap.Butt
                )
            }
        }
    }
}

// Preview do composable para visualização no Android Studio
@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    SuperIDTheme {
        LoadingScreen(onTimeout = {})
    }
}
