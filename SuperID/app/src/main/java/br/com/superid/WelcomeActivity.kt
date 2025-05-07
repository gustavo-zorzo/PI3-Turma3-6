package br.com.superid


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.superid.ui.theme.SuperIDTheme


class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperIDTheme {
                WelcomeScreen()
            }
        }
    }
}

@Composable
fun WelcomeScreen() {
    val context = LocalContext.current
    val backgroundColor = Color(0xFF162A4B)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Fundo com círculos
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(Color(0xFF153560), 400f, Offset(200f, 400f))
            drawCircle(Color(0xFF153560), 300f, Offset(size.width - 100f, size.height - 200f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header()

            Spacer(modifier = Modifier.height(24.dp))

            DescriptionCircle()

            // Botão de cadastro
            ActionButton(
                text = "Cadastre-se",
                onClick = { context.startActivity(Intent(context, SignUpActivity::class.java)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botão de login
            ActionButton(
                text = "Login",
                onClick = { context.startActivity(Intent(context, SignInActivity::class.java)) }
            )
        }
    }
}

@Composable
fun Header() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
    ) {
        // Título principal
        Text(
            text = "Bem vindo ao SuperID!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp)
        )
        // logo no topo direito
        Box(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(Color(0xFF5B88B2)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo_superid),
                contentDescription = "Logo SuperID",
                modifier = Modifier.size(88.dp)
            )
        }
    }
}

@Composable
fun DescriptionCircle() {
    // Texto dentro do circulo do meio
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .height(500.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(
                color = Color(0xFF0D1F37),
                radius = size.minDimension / 1.5f,
                center = Offset(size.width / 2f, size.height / 2f)
            )
        }

        Text(
            text = """
                O SuperID é a maneira mais 
                inovadora e segura de 
                gerenciar suas credenciais e 
                acessar sites sem precisar 
                memorizar senhas.

                Com tecnologia de ponta, ele 
                protege suas informações com 
                criptografia avançada e 
                permite login rápido via QR 
                Code em sites parceiros.

                Simples, prático e confiável – 
                sua segurança sempre em 
                primeiro lugar!
            """.trimIndent(),
            color = Color.White,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}

@Composable
fun ActionButton(text: String, onClick: () -> Unit) {
    // Botões com cor (cadastro e login)
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(3.dp, Color(0xFF5B88B2)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0D1F37),
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
        modifier = Modifier
            .wrapContentWidth()
            .height(52.dp)
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}
