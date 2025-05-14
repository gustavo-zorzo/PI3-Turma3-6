package br.com.superid

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.superid.ui.theme.SuperIDTheme

@Composable
fun TermsScreen(onConfirmClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White // Cor de fundo do ScrollView
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp), // Padding do ScrollView XML
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.superidlogoescrito),
                contentDescription = stringResource(id = R.string.descricao_logo),
                modifier = Modifier
                    .size(80.dp)
                    .padding(top = 8.dp) // marginTop="8dp"
            )

            Spacer(modifier = Modifier.height(16.dp)) // marginTop="16dp"

            Text(
                text = stringResource(id = R.string.terms),
                color = Color(0xFF0E2F5A), // dark_blue
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp)) // marginTop="24dp"

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium, // app:cardCornerRadius="12dp"
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), // app:cardElevation="2dp"
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFA9D0F5) // light_blue (Ajustar se a cor for diferente)
                    // Verificar o valor exato de @color/light_blue
                )
            ) {
                SelectionContainer {
                    Text(
                        text = stringResource(id = R.string.termos_de_uso),
                        modifier = Modifier.padding(16.dp),
                        color = Color.Black,
                        fontSize = 14.sp,
                        lineHeight = 18.sp // Aproximação de lineSpacingExtra="4dp"
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp)) // marginTop="32dp"

            Button(
                onClick = onConfirmClick,
                modifier = Modifier.wrapContentWidth(), // O botão no XML tinha wrap_content
                shape = MaterialTheme.shapes.medium, // app:cornerRadius="12dp"
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0E2F5A), // dark_blue
                    contentColor = Color.White
                )
                // Para a borda (stroke), pode ser necessário um Button com borda customizada
            ) {
                Text(
                    text = stringResource(id = R.string.confirmar_leitura),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp) // Padding interno
                )
            }
            Spacer(modifier = Modifier.height(24.dp)) // Espaço no final
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TermsScreenPreview() {
    SuperIDTheme {
        TermsScreen(onConfirmClick = {})
    }
} 