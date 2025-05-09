package br.com.superid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import br.com.superid.ui.theme.SuperIDTheme

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperIDTheme {
                TelaPrincipal()
            }
        }
    }
}

@Composable
fun TelaPrincipal() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopAppBarContent()

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OpcaoMenu(
                iconRes = R.drawable.logo_qrcode,
                texto = "Login sem senha"
            )
            OpcaoMenu(
                iconRes = R.drawable.logo_senha,
                texto = "Gerenciar senhas"
            )
            OpcaoMenu(
                iconRes = R.drawable.logo_conf,
                texto = "Configurar conta"
            )
        }
    }
}

@Composable
fun TopAppBarContent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_superidauth),
            contentDescription = "Logo",
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
        )

        IconButton(onClick = {

        }) {
            Image(
                painter = painterResource(id = R.drawable.logo_menu),
                contentDescription = "Menu",
                modifier = Modifier
                    .size(32.dp)
            )
        }
    }
}

@Composable
fun OpcaoMenu(iconRes: Int, texto: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFDFF0FF))
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = texto,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = texto, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}
