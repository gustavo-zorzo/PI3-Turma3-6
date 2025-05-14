package br.com.superid

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.superid.ui.theme.SuperIDTheme
import androidx.compose.ui.platform.LocalContext




class AccountConfigActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperIDTheme {
                TelaPerfil()
            }
        }
    }
}

@Composable
fun TelaPerfil() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logo_pencil),
                contentDescription = "Editar",
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        Toast.makeText(
                            context,
                            "Função editar ainda não implementada",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                tint = Color.Unspecified
            )

            Icon(
                painter = painterResource(id = R.drawable.logo_back),
                contentDescription = "Voltar",
                modifier = Modifier
                    .size(65.dp)
                    .clickable {
                        (context as? ComponentActivity)?.finish()
                    },
                tint = Color.Unspecified
            )

        }

        Spacer(modifier = Modifier.height(15.dp))


        Image(
            painter = painterResource(id = R.drawable.logo_icon),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(230.dp)

        )


        //fazer com que o nome do usuario apareca ao inves da msg - fazer ainda
        Text("Nome de usuário", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(8.dp))

        //fazer com que o email do usuario apareca ao inves da msg - fazer ainda
        Text("emailDoUsuario@email.com", color = Color.DarkGray)

        Spacer(modifier = Modifier.height(80.dp))

        HorizontalDivider()
        ItemOpcaoPerfil("Validar e-mail")
        HorizontalDivider()
        ItemOpcaoPerfil("Gerenciar notificações de segurança")
        HorizontalDivider()

        Spacer(modifier = Modifier.height(160.dp))

        Text(
            text = "Sair da conta",
            color = Color(0xFF122C4F),
            fontSize = 20.sp,
            modifier = Modifier.clickable {
                Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show()

            }
        )
    }
}

@Composable
fun ItemOpcaoPerfil(titulo: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable {

            },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(titulo, fontSize = 16.sp)
        Text(">", fontSize = 16.sp)
    }
}

