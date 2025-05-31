package br.com.superid

import android.content.Intent
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
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.superid.ui.theme.SuperIDTheme
import androidx.compose.material3.TopAppBarDefaults



class TermsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperIDTheme {
                TelaTermosDeServico(
                    onConfirmar = {
                        startActivity(Intent(this, WelcomeActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaTermosDeServico(onConfirmar: () -> Unit) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Termos de serviço",
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0E2F5A),
                            modifier = Modifier.weight(1f) // ocupa o espaço restante à esquerda
                        )
                        Image(
                            painter = painterResource(id = R.drawable.logo_superid),
                            contentDescription = "Logo escudo",
                            modifier = Modifier.size(100.dp) // tamanho aumentado
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD0E2F2)) // azul mais escuro
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("1. Introdução", fontWeight = FontWeight.Bold)
                    Text(
                        "Bem-vindo ao SuperID! Estes Termos de Serviço (\"Termos\") regem o uso do aplicativo SuperID e de seus serviços associados. Ao utilizar o SuperID, você concorda com estes Termos. Caso não concorde, não utilize o aplicativo.",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text("2. Cadastro e Conta do Usuário", fontWeight = FontWeight.Bold)
                    Text(
                        "• Para utilizar o SuperID, você deve criar uma conta, fornecendo Nome, E-mail e Senha Mestre.\n" +
                                "• Você deve validar seu e-mail para utilizar a funcionalidade de Login Sem Senha.\n" +
                                "• O SuperID armazena UID e IMEI do dispositivo para segurança e autenticação.",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text("3. Uso do Aplicativo", fontWeight = FontWeight.Bold)
                    Text(
                        "• O SuperID permite armazenar e gerenciar senhas de forma segura.\n" +
                                "• Senhas são organizadas por categorias e criptografadas antes do armazenamento.\n" +
                                "• Cada senha recebe um accessToken de 256 caracteres para aumentar a segurança.\n" +
                                "• Você pode cadastrar, editar e excluir suas senhas a qualquer momento.",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text("4. Login Sem Senha em Sites Parceiros", fontWeight = FontWeight.Bold)
                    Text(
                        "• O SuperID oferece a opção de login via QR Code em sites parceiros.\n" +
                                "• Ao escanear um QR Code gerado por um site parceiro, sua conta será autenticada automaticamente.\n" +
                                "• O site parceiro pode consultar seu status de login por um período limitado de tempo.",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text("5. Segurança e Proteção de Dados", fontWeight = FontWeight.Bold)
                    Text(
                        "• Todas as senhas armazenadas são criptografadas para garantir a máxima segurança.\n" +
                                "• O usuário é responsável por manter sua Senha Mestre em sigilo.\n" +
                                "• O SuperID não se responsabiliza por acessos não autorizados devido ao compartilhamento da senha de recuperação.",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text("6. Recuperação de Senha Mestre", fontWeight = FontWeight.Bold)
                    Text(
                        "• Caso o usuário esqueça sua Senha Mestre, poderá redefini-la via e-mail, desde que o e-mail tenha sido validado previamente.",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text("7. Responsabilidades e Limitações", fontWeight = FontWeight.Bold)
                    Text(
                        "• O SuperID é um gerenciador de senhas, mas não se responsabiliza por falhas de segurança externas à sua plataforma.\n" +
                                "• O aplicativo pode ser atualizado para melhorias e correções sem aviso prévio.\n" +
                                "• O uso indevido da aplicação pode resultar na suspensão ou exclusão da conta.",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text("8. Modificações nos Termos", fontWeight = FontWeight.Bold)
                    Text(
                        "• Podemos atualizar estes Termos periodicamente. Notificaremos os usuários sobre alterações relevantes.",
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

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

