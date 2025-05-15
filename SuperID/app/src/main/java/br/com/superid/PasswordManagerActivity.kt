package br.com.superid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.superid.ui.theme.SuperIDTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.app.Activity
import android.util.Base64
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.platform.LocalContext
import java.security.SecureRandom
import androidx.compose.ui.res.painterResource


data class Senha(
    val id: String = "",
    val titulo: String = "",
    val login: String = "",
    val senha: String = "",
    val categoria: String = ""
)

class PasswordManagerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperIDTheme {
                PasswordManagerScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordManagerScreen() {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid
    var senhas by remember { mutableStateOf(listOf<Senha>()) }
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as? Activity
    var senhaParaEditar by remember { mutableStateOf<Senha?>(null) }

    LaunchedEffect(uid) {
        uid?.let {
            db.collection("Senhas").whereEqualTo("uid", it).addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    senhas = snapshot.documents.map { doc ->
                        val senhaCriptografada = doc.getString("senha") ?: ""
                        val senhaDescriptografada = try {
                            CryptoManager.decryptAES(senhaCriptografada)
                        } catch (e: Exception) {
                            "[Erro na descriptografia]"
                        }
                        Senha(
                            id = doc.id,
                            titulo = doc.getString("titulo") ?: "",
                            login = doc.getString("login") ?: "",
                            senha = senhaDescriptografada,
                            categoria = doc.getString("categoria") ?: ""
                        )
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FB))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Título
            Text(
                text = "Gerenciador de Senhas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            // Botão de voltar no canto direito
            Icon(
                painter = painterResource(id = R.drawable.logo_back),
                contentDescription = "Voltar",
                modifier = Modifier
                    .size(65.dp)
                    .clickable {
                        activity?.finish()
                    },
                tint = Color.Unspecified
            )
        }


        Column(modifier = Modifier.padding(16.dp)) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 16.dp)
            ) {
                items(senhas) { item ->
                    PasswordItem(
                        senha = item,
                        onEdit = { senhaSelecionada ->
                            senhaParaEditar = senhaSelecionada
                            showDialog = true
                        },
                        onDelete = { senha ->
                            senha.id.takeIf { it.isNotBlank() }?.let {
                                db.collection("Senhas").document(it).delete()
                            }
                        }
                    )
                }
            }

            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Nova Senha", fontWeight = FontWeight.Bold)
            }

            if (showDialog) {
                NovaSenhaDialog(
                    senhaInicial = senhaParaEditar,
                    onDismiss = {
                        showDialog = false
                        senhaParaEditar = null
                    },
                    onSave = { titulo, login, senha, categoria, id ->
                        if (uid != null) {
                            // Criptografa a senha
                            val senhaCriptografada = CryptoManager.encryptAES(senha)

                            // Gera accessToken de 256 caracteres (192 bytes em Base64)
                            val tokenBytes = ByteArray(192)
                            SecureRandom().nextBytes(tokenBytes)
                            val accessToken = Base64.encodeToString(tokenBytes, Base64.NO_WRAP)

                            val dados = mapOf(
                                "uid" to uid,
                                "titulo" to titulo,
                                "login" to login,
                                "senha" to senhaCriptografada,
                                "categoria" to categoria,
                                "accessToken" to accessToken
                            )

                            if (id != null) {
                                db.collection("Senhas").document(id).set(dados)
                            } else {
                                db.collection("Senhas").add(dados)
                            }
                        }
                        showDialog = false
                        senhaParaEditar = null

                    }

                )
            }
        }
    }
}

@Composable
fun PasswordItem(
    senha: Senha,
    onEdit: (Senha) -> Unit,
    onDelete: (Senha) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("${senha.titulo} (${senha.categoria})", fontWeight = FontWeight.Bold)
            Text(senha.login)
        }

        Box {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Mais opções")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Editar") },
                    onClick = {
                        expanded = false
                        onEdit(senha)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Excluir") },
                    onClick = {
                        expanded = false
                        onDelete(senha)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaSenhaDialog(
    senhaInicial: Senha? = null,
    onDismiss: () -> Unit,
    onSave: (titulo: String, login: String, senha: String, categoria: String, id: String?) -> Unit
) {
    var titulo by remember { mutableStateOf(senhaInicial?.titulo ?: "") }
    var login by remember { mutableStateOf(senhaInicial?.login ?: "") }
    var senha by remember { mutableStateOf(senhaInicial?.senha ?: "") }
    var descricao by remember { mutableStateOf(senhaInicial?.senha ?: "") }
    var categoria by remember { mutableStateOf(senhaInicial?.categoria ?: "") }
    var expanded by remember { mutableStateOf(false) }


    val categorias = listOf("Rede Social", "Streaming", "Banco", "E-mail", "Trabalho", "Outro")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (senhaInicial == null) "Nova Senha" else "Editar Senha") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título") })
                OutlinedTextField(
                    value = login,
                    onValueChange = { login = it },
                    label = {
                        Row {
                            Text("Login", modifier = Modifier.weight(1f))
                            Text("(opcional)", fontSize = 12.sp)
                        }
                    }
                )
                OutlinedTextField(value = senha, onValueChange = { senha = it }, label = { Text("Senha") })
                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = {
                        Row {
                            Text("Descrição", modifier = Modifier.weight(1f))
                            Text("(opcional)", fontSize = 12.sp)
                        }
                    }
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = categoria,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoria") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categorias.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    categoria = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(titulo, login, senha, categoria, senhaInicial?.id)
            }) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}