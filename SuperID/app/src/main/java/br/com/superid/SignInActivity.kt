package br.com.superid

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import br.com.superid.ui.theme.SuperIDTheme
import com.google.firebase.auth.FirebaseAuth
import android.util.Log

private const val TAG = "SignInActivityLOG"

class SignInActivity : ComponentActivity() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperIDTheme {
                TelaLogin()
            }
        }
    }

    @Composable
    fun TelaLogin() {
        var email by remember { mutableStateOf("") }
        var senhaMestre by remember { mutableStateOf("") }
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth()
            )

            TextField(
                value = senhaMestre,
                onValueChange = { senhaMestre = it },
                label = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth()
            )

            Button(
                onClick = {
                    if (email.isNotBlank() && senhaMestre.isNotBlank()) {
                        loginAuth(email, senhaMestre) { login ->
                            if (login) {
                                Toast.makeText(context, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()

                                // vai para a tela DashboardActivity
                                val intent = Intent(this@SignInActivity, DashboardActivity::class.java)
                                startActivity(intent)
                                finish()

                            } else {
                                Toast.makeText(context, "Erro ao fazer login. Verifique email e senha.", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Text("Logar")
            }
        }
    }

    fun loginAuth(
        email: String,
        senhaMestre: String,
        onResult: (Boolean) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, senhaMestre)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Logado com sucesso")
                    onResult(true)
                } else {
                    Log.w(TAG, "Erro ao logar", task.exception)
                    onResult(false)
                }
            }
    }
}
