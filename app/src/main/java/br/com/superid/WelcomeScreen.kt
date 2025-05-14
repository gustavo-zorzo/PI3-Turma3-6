package br.com.superid

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.superid.ui.theme.SuperIDTheme

@Composable
fun WelcomeScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToTerms: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF0E2F5A) // Cor de fundo principal
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Círculos decorativos - Posições e tamanhos são aproximações do XML.
            // Pode ser necessário ajustar para um match perfeito.
            Image(
                painter = painterResource(id = R.drawable.bluecircle),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(120.dp)
                    .offset(x = (-25).dp, y = 65.dp)
                    .rotate(-263f)
                    .alpha(0.6f)
            )

            Image(
                painter = painterResource(id = R.drawable.darkbluecircle),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopStart) // Ajustar alinhamento se necessário
                    .size(400.dp)
                    .offset(x = (-50).dp, y = 100.dp)
                    .alpha(0.7f)
            )
            
            Image(
                painter = painterResource(id = R.drawable.shieldcircle),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(80.dp)
                    .offset(x = 5.dp, y = 0.dp) // Em Compose, offset positivo X vai para direita
                    .alpha(0.6f)
            )

            Image(
                painter = painterResource(id = R.drawable.bluecircle),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd) // Tentativa de replicar o posicionamento
                    .size(160.dp)
                    .offset(x = 30.dp, y = (-200).dp) // Em Compose, offset Y negativo vai para cima
                    .alpha(0.6f)
            )


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 24.dp, end = 24.dp, top = 80.dp, bottom = 24.dp), // Adicionado padding superior para não sobrepor o shield
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top // O XML usa um ScrollView, então o conteúdo começa no topo
            ) {

                Text(
                    text = stringResource(id = R.string.welcome),
                    color = Color.White,
                    fontSize = 26.sp,
                    style = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
                    modifier = Modifier.fillMaxWidth()
                                       .padding(top = 32.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(id = R.string.info),
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 22.sp, // Equivalente a lineSpacingExtra="6dp" (16sp + 6sp)
                    style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(36.dp))

                Button(
                    onClick = onNavigateToRegister,
                    modifier = Modifier
                        .fillMaxWidth(0.7f), // Para dar um tamanho razoável, não muito largo
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1A3B66),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = stringResource(id = R.string.register))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.have_account),
                    color = Color(0xFFA9D0F5),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clickable(onClick = onNavigateToLogin)
                        .padding(8.dp) // Adicionar padding para melhor área de toque
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.ler_termos),
                    color = Color(0xFFA9D0F5),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clickable(onClick = onNavigateToTerms)
                        .padding(8.dp) // Adicionar padding para melhor área de toque
                )
                Spacer(modifier = Modifier.height(24.dp)) // Espaço no final do scroll
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E2F5A)
@Composable
fun WelcomeScreenPreview() {
    SuperIDTheme {
        WelcomeScreen(
            onNavigateToRegister = {},
            onNavigateToLogin = {},
            onNavigateToTerms = {}
        )
    }
} 