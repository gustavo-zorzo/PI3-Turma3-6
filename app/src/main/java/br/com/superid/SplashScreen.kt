package br.com.superid

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import br.com.superid.ui.theme.SuperIDTheme

private const val SPLASH_TIMEOUT_MS: Long = 3000L
private const val PROGRESS_UPDATE_INTERVAL_MS: Long = 50L

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    var progress: Float by remember { mutableFloatStateOf(0f) }
    val totalSteps: Int = (SPLASH_TIMEOUT_MS / PROGRESS_UPDATE_INTERVAL_MS).toInt()

    LaunchedEffect(Unit) {
        for (i in 1..totalSteps) {
            progress = i.toFloat() / totalSteps
            delay(PROGRESS_UPDATE_INTERVAL_MS)
        }
        delay(100) // Pequeno delay extra para garantir a barra cheia
        onTimeout()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White // Equivalente ao background #FFFFFF do XML
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.superidlogoescrito),
                    contentDescription = "SuperID Logo", // Adicionar descrição de conteúdo
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(48.dp)) // Espaçamento maior que o marginTop=24dp original para compensar a ausência da guideline
                LinearProgressIndicator(
                    progress = { progress }, // Usar a nova assinatura
                    modifier = Modifier.width(250.dp).height(10.dp),
                    color = Color(0xFF0E2F5A), // Equivalente a progressTint="#0E2F5A"
                    trackColor = Color(0xFFAAAAAA), // Equivalente a backgroundTint="#AAAAAA"
                    strokeCap = StrokeCap.Butt // Para ter pontas retas como a ProgressBar padrão
                )
            }
        }
    }
}

// Adicionar um Preview para visualização no Android Studio
@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SuperIDTheme { // Usar SuperIDTheme
        SplashScreen(onTimeout = {})
    }
} 