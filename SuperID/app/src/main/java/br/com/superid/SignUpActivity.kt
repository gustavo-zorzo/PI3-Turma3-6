package br.com.superid

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import br.com.superid.ui.theme.SuperIDTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.util.Log
import androidx.compose.ui.text.font.FontWeight

private lateinit var auth: FirebaseAuth
private val TAG = "SignUpActivityLOG"

//activity da tela de cadastro
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
        var confirmarSenha by remember { mutableStateOf("") }
        var termosAceitos by remember { mutableStateOf(false) }
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Botão de voltar no topo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_back),
                    contentDescription = "Voltar",
                    modifier = Modifier
                        .size(65.dp)
                        .clickable {
                            if (context is ComponentActivity) context.finish()
                        },
                    contentScale = ContentScale.Fit
                )
            }

            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo_superidauth),
                contentDescription = "Logo SuperID",
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Fit
            )



            Text(
                text = "Crie a sua conta:",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0E2F5A),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Campo: Nome
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome de usuário:") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo: Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Endereço de e-mail:") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )

            // Campo: Senha
            OutlinedTextField(
                value = senhaMestre,
                onValueChange = { senhaMestre = it },
                label = { Text("Senha:") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )

            // Campo: Confirmar senha
            OutlinedTextField(
                value = confirmarSenha,
                onValueChange = { confirmarSenha = it },
                label = { Text("Confirme a senha:") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )

            // Checkbox de termos
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Checkbox(
                    checked = termosAceitos,
                    onCheckedChange = { termosAceitos = it }
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Eu li e concordo com os termos de uso", fontSize = 12.sp)
            }

            // Botão de cadastro
            Button(
                onClick = {
                    if (nome.isNotBlank() && email.isNotBlank() && senhaMestre.length >= 6 && senhaMestre == confirmarSenha && termosAceitos) {
                        cadastrarUsuario(nome, email, senhaMestre, context)
                    } else {
                        Toast.makeText(context, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF122C4F)),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Cadastre-se", color = Color.White)
            }
        }
    }
//funcao para vincular o firebaseauth com o app
    fun cadastrarUsuario(nome: String, email: String, senhaMestre: String, context: android.content.Context) {
        Firebase.auth.createUserWithEmailAndPassword(email, senhaMestre)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = Firebase.auth.currentUser
                    val uid = user?.uid

                    // Envia o e-mail de verificação
                    user?.sendEmailVerification()
                        ?.addOnSuccessListener {
                            Toast.makeText(context, "E-mail de verificação enviado!", Toast.LENGTH_SHORT).show()
                        }
                        ?.addOnFailureListener {
                            Toast.makeText(context, "Erro ao enviar verificação: ${it.message}", Toast.LENGTH_LONG).show()
                        }

                    // Atualiza o nome do usuário no FirebaseAuth
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(nome)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                Log.d(TAG, "Nome de usuário atualizado com sucesso.")
                            }
                        }

                    // Salvar dados no Firestore
                    val db = Firebase.firestore
                    val dadosUsuario = hashMapOf(
                        "uid" to uid,
                        "nome" to nome,
                        "email" to email,
                    )

                    if (uid != null) {
                        db.collection("Usuario").document(uid)
                            .set(dadosUsuario)
                            .addOnSuccessListener {
                                Log.d(TAG, "Usuário salvo com sucesso no Firestore.")
                                Toast.makeText(context, "Cadastro completo!", Toast.LENGTH_SHORT).show()

                                val intent = Intent(context, DashboardActivity::class.java)
                                context.startActivity(intent)
                                if (context is ComponentActivity) context.finish()
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
