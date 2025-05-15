package br.com.superid

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.superid.ui.theme.SuperIDTheme
import com.google.firebase.auth.FirebaseAuth

class EmailVerificationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperIDTheme {
                EmailVerificationScreen()
            }
        }
    }
}

@Composable
fun EmailVerificationScreen() {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email ?: ""
    var emailState by remember { mutableStateOf(email) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Validar e-mail",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF122C4F)
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

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(thickness = 1.dp, color = Color(0xFF122C4F))

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Verifique se o endereço de e-mail está correto,\nvamos te enviar um link de confirmação",
            fontSize = 19.sp,
            color = Color(0xFF122C4F),
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(150.dp))

        Text(
            "Digite seu endereço de email:",
            fontSize = 20   .sp,
            color = Color(0xFF122C4F),
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = emailState,
            onValueChange = { emailState = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            shape = RoundedCornerShape(6.dp),
            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
        )

        Spacer(modifier = Modifier.height(200.dp))

        Button(
            onClick = {
                user?.sendEmailVerification()
                    ?.addOnSuccessListener {
                        Toast.makeText(context, "E-mail de verificação enviado!", Toast.LENGTH_SHORT).show()
                    }
                    ?.addOnFailureListener {
                        Toast.makeText(context, "Erro ao enviar verificação: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF122C4F))
        ) {
            Text(
                "Validar e-mail",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Reenviar e-mail",
            color = Color(0xFF122C4F),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                user?.sendEmailVerification()
                    ?.addOnSuccessListener {
                        Toast.makeText(context, "E-mail reenviado com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                    ?.addOnFailureListener {
                        Toast.makeText(context, "Erro: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
        )
    }
}
