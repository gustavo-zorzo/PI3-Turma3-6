package br.com.superid

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
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
import com.google.firebase.firestore.FirebaseFirestore
import java.security.SecureRandom
import android.app.Activity
import android.util.Base64

data class Senha(
    val id: String = "",
    val titulo: String = "",
    val login: String = "",
    val senha: String = "",
    val categoria: String = "",
    val descricao: String = ""
)

class PasswordManagerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null && !user.isEmailVerified) {
            Toast.makeText(this, "Valide o seu e-mail.", Toast.LENGTH_LONG).show()
        }
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
    val categoriasFixas = listOf("Sites Web", "Aplicativos", "Teclados de Acesso Físico")
    var categoriasPersonalizadas by remember { mutableStateOf(setOf<String>()) }

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
                            categoria = doc.getString("categoria") ?: "",
                            descricao = doc.getString("descricao") ?: ""
                        )
                    }
                    val novasCategorias = senhas.map { it.categoria }
                        .filter { it.isNotBlank() && it !in categoriasFixas }
                        .toSet()
                    categoriasPersonalizadas = novasCategorias
                }
            }
        }
    }

    val todasCategorias = (senhas.map { it.categoria } + categoriasFixas).toSet().sorted()
    val senhasPorCategoria = todasCategorias.associateWith { cat ->
        senhas.filter { it.categoria == cat }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text("Gerenciador de Senhas", fontWeight = FontWeight.Bold, color = Color(0xFF122C4F))
            },
            navigationIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.logo_back),
                    contentDescription = "Voltar",
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(60.dp)
                        .clickable { activity?.finish() },
                    tint = Color.Unspecified
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            senhasPorCategoria.forEach { (categoria, senhasDaCategoria) ->
                item {
                    Spacer(modifier = Modifier.height(50.dp))
                    Text(
                        text = categoria,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                items(senhasDaCategoria) { item ->
                    PasswordItem(
                        senha = item,
                        onEdit = {
                            senhaParaEditar = it
                            showDialog = true
                        },
                        onDelete = {
                            if (it.id.isNotBlank()) {
                                db.collection("Senhas").document(it.id).delete()
                            }
                        }
                    )
                }
            }
        }

        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp)
        ) {
            Text("Adicionar Nova Senha", fontWeight = FontWeight.Bold)
        }
    }

    if (showDialog) {
        NovaSenhaDialog(
            senhaInicial = senhaParaEditar,
            categoriasDisponiveis = categoriasFixas + categoriasPersonalizadas.toList(),
            onDismiss = {
                showDialog = false
                senhaParaEditar = null
            },
            onSave = { titulo, login, senha, categoria, descricao, id ->
                if (uid != null) {
                    val senhaCriptografada = CryptoManager.encryptAES(senha)
                    val tokenBytes = ByteArray(192)
                    SecureRandom().nextBytes(tokenBytes)
                    val accessToken = Base64.encodeToString(tokenBytes, Base64.NO_WRAP)
                    val dados = mapOf(
                        "uid" to uid,
                        "titulo" to titulo,
                        "login" to login,
                        "senha" to senhaCriptografada,
                        "categoria" to categoria,
                        "descricao" to descricao,
                        "accessToken" to accessToken
                    )
                    if (id != null) {
                        db.collection("Senhas").document(id).set(dados)
                    } else {
                        db.collection("Senhas").add(dados)
                    }
                    if (categoria !in categoriasFixas) {
                        categoriasPersonalizadas = categoriasPersonalizadas + categoria
                    }
                }
                showDialog = false
                senhaParaEditar = null
            }
        )
    }
}


@Composable
fun PasswordItem(
    senha: Senha,
    onEdit: (Senha) -> Unit,
    onDelete: (Senha) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showVisualizarDialog by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE6F2FF))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(senha.titulo, fontWeight = FontWeight.Bold)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { showVisualizarDialog = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.logo_olho),
                        contentDescription = "Visualizar senha",
                        modifier = Modifier.size(33.dp)
                    )
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

        if (showVisualizarDialog) {
            VisualizarSenhaDialog(senha = senha, onDismiss = { showVisualizarDialog = false })
        }
    }
}


@Composable
fun VisualizarSenhaDialog(senha: Senha, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Detalhes da Senha") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Título: ${senha.titulo}")
                Text("Login: ${senha.login}")
                Text("Senha: ${senha.senha}")
                Text("Categoria: ${senha.categoria}")
                Text("Descrição: ${senha.descricao}")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Fechar") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaSenhaDialog(
    senhaInicial: Senha? = null,
    categoriasDisponiveis: List<String>,
    onDismiss: () -> Unit,
    onSave: (titulo: String, login: String, senha: String, categoria: String, descricao: String, id: String?) -> Unit
) {
    var titulo by remember { mutableStateOf(senhaInicial?.titulo ?: "") }
    var login by remember { mutableStateOf(senhaInicial?.login ?: "") }
    var senha by remember { mutableStateOf(senhaInicial?.senha ?: "") }
    var descricao by remember { mutableStateOf(senhaInicial?.descricao ?: "") }
    var categoria by remember { mutableStateOf(senhaInicial?.categoria ?: "") }
    var categoriaPersonalizada by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var erroCamposObrigatorios by remember { mutableStateOf(false) }

    val categorias = categoriasDisponiveis + "Criar nova categoria"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (senhaInicial == null) "Nova Senha" else "Editar Senha") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título") })
                OutlinedTextField(value = login, onValueChange = { login = it }, label = { Text("Login (opcional)") })
                OutlinedTextField(value = senha, onValueChange = { senha = it }, label = { Text("Senha") })
                OutlinedTextField(value = descricao, onValueChange = { descricao = it }, label = { Text("Descrição (opcional)") })

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
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categorias.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    categoria = option
                                    if (option != "Criar nova categoria") {
                                        categoriaPersonalizada = ""
                                    }
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                if (categoria == "Criar nova categoria") {
                    OutlinedTextField(
                        value = categoriaPersonalizada,
                        onValueChange = { categoriaPersonalizada = it },
                        label = { Text("Nova Categoria") }
                    )
                }
            }
        },
        confirmButton = {
            Column {
                if (erroCamposObrigatorios) {
                    Text(
                        text = "Preencha todos os campos obrigatórios",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Button(onClick = {
                    val categoriaFinal = if (categoria == "Criar nova categoria") categoriaPersonalizada else categoria
                    if (titulo.isBlank() || senha.isBlank() || categoriaFinal.isBlank()) {
                        erroCamposObrigatorios = true
                    } else {
                        erroCamposObrigatorios = false
                        onSave(titulo, login, senha, categoriaFinal, descricao, senhaInicial?.id)
                    }
                }) {
                    Text("Salvar")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
