package br.com.superid

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import android.util.Log
import br.com.superid.ui.theme.SuperIDTheme

private lateinit var auth: FirebaseAuth
private val TAG = "SignUpActivityLOG"

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            SuperIDTheme {
                TelaCadastro()
            }
        }
    }

    @Composable
    fun TelaCadastro() {
        var nome by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var senha by remember { mutableStateOf("") }
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Text(
                "Cadastro",
                fontSize = 24.sp
            )

            TextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") },
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 12.dp)
                    .fillMaxWidth()
            )

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 12.dp)
                    .fillMaxWidth()
            )

            TextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 12.dp)
                    .fillMaxWidth()
            )

            Button(
                onClick = {
                    if (nome.isNotBlank() && email.isNotBlank() && senha.length >= 6) {
                        addFirestore(nome, email, senha, context)
                        addAuth(email, senha, context)
                        Log.d(TAG, "Usuário criado com sucesso")
                    } else {
                        Toast.makeText(context, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 12.dp)
            ) {
                Text("Cadastrar")
            }
        }
    }

    fun addFirestore(
        nome: String,
        email: String,
        senha: String,
        context: android.content.Context
    ) {
        val db = Firebase.firestore
        val user = hashMapOf(
            "Nome" to nome,
            "Email" to email,
            "Senha" to senha
        )

        db.collection("Usuario")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "Documento adicionado com ID: ${documentReference.id}")
                Toast.makeText(context, "Usuário salvo no Firestore!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Erro ao adicionar documento", e)
                Toast.makeText(context, "Erro ao salvar dados: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    fun addAuth(email: String, senha: String, context: android.content.Context) {
        Firebase.auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(context, "Cadastro autenticado com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, "Erro ao criar conta: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
