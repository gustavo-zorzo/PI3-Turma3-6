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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
        var senhaMestre by remember { mutableStateOf("") }
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Text("Cadastro", fontSize = 24.sp)

            TextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") },
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth()
            )

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
                    if (nome.isNotBlank() && email.isNotBlank() && senhaMestre.length >= 6) {
                        cadastrarUsuario(nome, email, senhaMestre, context)
                    } else {
                        Toast.makeText(context, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Text("Cadastrar")
            }
        }
    }

    fun cadastrarUsuario(nome: String, email: String, senhaMestre: String, context: android.content.Context) {
        Firebase.auth.createUserWithEmailAndPassword(email, senhaMestre)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val uid = Firebase.auth.currentUser?.uid
                    val db = Firebase.firestore

                    val dadosUsuario = hashMapOf(
                        "uid" to uid,
                        "nome" to nome,
                        "email" to email,
                        "senha" to senhaMestre
                    )

                    if (uid != null) {
                        db.collection("Usuario").document(uid)
                            .set(dadosUsuario)
                            .addOnSuccessListener {
                                Log.d(TAG, "Usuário salvo com sucesso no Firestore.")
                                Toast.makeText(context, "Cadastro completo!", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Erro ao salvar no Firestore", e)
                                Toast.makeText(context, "Erro ao salvar dados: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    }

                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, "Erro ao criar conta: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
