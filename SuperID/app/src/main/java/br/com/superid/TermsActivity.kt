package br.com.superid


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.TopAppBarDefaults

//activity da tela de termos
class TermsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperIDTheme {
                // Exibe a tela de termos e redireciona para a tela de boas-vindas ao confirmar
                TelaTermosDeServico(
                    onConfirmar = {
                        startActivity(Intent(this, WelcomeActivity::class.java))
                        finish() // finaliza esta activity para não voltar com o botão "voltar"
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun TelaTermosDeServico(onConfirmar: () -> Unit) {
    val scrollState = rememberScrollState() // estado para rolagem vertical

    Scaffold(
        topBar = {
            // Barra superior com título e logo
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Título da tela
                        Text(
                            text = "Termos de serviço",
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0E2F5A),
                            modifier = Modifier.weight(1f)
                        )
                        // Logo no canto superior direito
                        Image(
                            painter = painterResource(id = R.drawable.logo_superid),
                            contentDescription = "Logo escudo",
                            modifier = Modifier.size(100.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { padding ->
        // Conteúdo principal da tela com rolagem
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            // Card contendo o texto dentro
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD0E2F2))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Cada seção do termo é separada por título em negrito e texto descritivo

                    Text("1. Introdução", fontWeight = FontWeight.Bold)
                    Text(
                        "Bem-vindo ao SuperID! Estes Termos de Serviço (\"Termos\") regem o uso do aplicativo...",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text("2. Cadastro e Conta do Usuário", fontWeight = FontWeight.Bold)
                    Text(
                        "• Para utilizar o SuperID, você deve criar uma conta...\n" +
                                "• Você deve validar seu e-mail...\n" +
                                "• O SuperID armazena UID e IMEI...",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text("3. Uso do Aplicativo", fontWeight = FontWeight.Bold)
                    Text(
                        "• O SuperID permite armazenar e gerenciar senhas...\n" +
                                "• Senhas são organizadas e criptografadas...\n" +
                                "• Cada senha recebe um accessToken...\n" +
                                "• Você pode cadastrar, editar e excluir...",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text("4. Login Sem Senha em Sites Parceiros", fontWeight = FontWeight.Bold)
                    Text(
                        "• O SuperID oferece login via QR Code...\n" +
                                "• Ao escanear, sua conta será autenticada...\n" +
                                "• O site parceiro pode consultar seu status...",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text("5. Segurança e Proteção de Dados", fontWeight = FontWeight.Bold)
                    Text(
                        "• Todas as senhas são criptografadas...\n" +
                                "• O usuário deve manter a Senha Mestre em sigilo...\n" +
                                "• SuperID não se responsabiliza por compartilhamento indevido...",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text("6. Recuperação de Senha Mestre", fontWeight = FontWeight.Bold)
                    Text(
                        "• Caso esqueça sua Senha Mestre, você poderá redefini-la por e-mail (validado previamente).",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text("7. Responsabilidades e Limitações", fontWeight = FontWeight.Bold)
                    Text(
                        "• O SuperID não se responsabiliza por falhas externas...\n" +
                                "• O aplicativo pode ser atualizado sem aviso...\n" +
                                "• Uso indevido pode levar à suspensão da conta.",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text("8. Modificações nos Termos", fontWeight = FontWeight.Bold)
                    Text(
                        "• Os termos podem ser atualizados periodicamente. Notificações serão feitas quando necessário.",
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botão de confirmação da leitura dos termos
            Button(
                onClick = { onConfirmar() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0E2F5A),
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    text = "Confirmar leitura",
                    modifier = Modifier.padding(vertical = 8.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
