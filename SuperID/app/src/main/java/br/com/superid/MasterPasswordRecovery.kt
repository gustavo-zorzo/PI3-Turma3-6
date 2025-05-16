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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.superid.ui.theme.SuperIDTheme
import com.google.firebase.auth.FirebaseAuth

class MasterPasswordRecovery : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SuperIDTheme {
                PasswordRecoveryScreen()
            }
        }
    }
}

@Composable
fun PasswordRecoveryScreen() {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val email = user?.email ?: ""
    var emailState by remember { mutableStateOf(email) }
    var isVerified by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        user?.reload()?.addOnSuccessListener {
            isVerified = user.isEmailVerified
        }
    }

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
                text = "Recuperação da senha\nmestre:",
                fontSize = 20.sp,
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
            text = "Verifique se seu endereço de e-mail está\nvalidado.\nVocê receberá um link para a recuperação da senha.",
            fontSize = 19.sp,
            color = Color(0xFF122C4F),
            lineHeight = 24.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(130.dp))

        Text(
            "Endereço de email:",
            fontSize = 20.sp,
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

        Spacer(modifier = Modifier.height(250.dp))

        if (isVerified) {
            Button(
                onClick = {
                    auth.sendPasswordResetEmail(emailState)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Link de recuperação enviado!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Erro: ${it.message}", Toast.LENGTH_LONG).show()
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF122C4F))
            ) {
                Text(
                    "Enviar link de recuperação",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        } else {
            Text(
                text = "Valide o seu e-mail para recuperar sua senha mestre.",
                fontSize = 16.sp,
                color = Color.Red,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}
