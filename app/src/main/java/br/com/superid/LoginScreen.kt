package br.com.superid

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.superid.ui.theme.SuperIDTheme
import br.com.superid.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    val uiState by loginViewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* Pode adicionar um título se desejar */ },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_desc),
                            modifier = Modifier.size(40.dp) // Tamanho do XML
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    navigationIconContentColor = Color.Black // Cor do ícone de voltar
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp) // Padding geral do ScrollView XML
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.superidlogoescrito),
                contentDescription = stringResource(id = R.string.descricao_logo),
                modifier = Modifier
                    .size(150.dp)
                    .padding(top = 16.dp) // marginTop="16dp"
            )

            Text(
                text = stringResource(id = R.string.welcome_back),
                color = Color(0xFF0E2F5A), // dark_blue
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp)) // marginTop="32dp" para labelEmail

            LoginTextField(
                value = uiState.email,
                onValueChange = { loginViewModel.onEmailChange(it) },
                labelResId = R.string.endereco_email,
                hintResId = R.string.hint_email,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordTextField(
                value = uiState.password,
                onValueChange = { loginViewModel.onPasswordChange(it) },
                labelResId = R.string.senha,
                hintResId = R.string.hint_senha,
                isVisible = uiState.passwordVisible,
                onVisibilityChange = { loginViewModel.togglePasswordVisibility() },
                imeAction = ImeAction.Done,
                onDone = { 
                    focusManager.clearFocus()
                    loginViewModel.attemptLogin() 
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { loginViewModel.attemptLogin() },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium, // app:cornerRadius="12dp"
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0E2F5A), // dark_blue
                    contentColor = Color.White
                )
                // Para a borda (stroke), pode ser necessário um Button com borda customizada
            ) {
                Text(text = stringResource(id = R.string.entrar), modifier = Modifier.padding(vertical = 8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.esqueci_senha),
                color = Color(0xFF0E2F5A), // dark_blue
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable(onClick = onForgotPasswordClick)
                    .padding(8.dp) // Para melhor área de toque
            )
            Spacer(modifier = Modifier.height(24.dp)) // Espaço no final do scroll
        }
    }
}

// Reutilizar os composables de TextField da RegisterScreen se forem idênticos ou criar versões específicas
@Composable
private fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelResId: Int,
    hintResId: Int,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = labelResId),
            style = MaterialTheme.typography.labelLarge,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 4.dp) // Similar ao layout do Register
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(id = hintResId), style = MaterialTheme.typography.bodyMedium) },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            shape = MaterialTheme.shapes.large, // app:boxCornerRadius="16dp"
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black.copy(alpha = 0.7f),
                focusedLabelColor = Color.Black,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )
    }
}

// Reutilizar PasswordTextField da RegisterScreen ou adaptar
@Composable
private fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelResId: Int,
    hintResId: Int,
    isVisible: Boolean,
    onVisibilityChange: () -> Unit,
    imeAction: ImeAction,
    onNext: (() -> Unit)? = null, // Não usado aqui, mas mantido por consistência se reutilizado
    onDone: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = labelResId),
            style = MaterialTheme.typography.labelLarge,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(id = hintResId), style = MaterialTheme.typography.bodyMedium) },
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onNext = { onNext?.invoke() },
                onDone = { onDone?.invoke() }
            ),
            trailingIcon = {
                val image = if (isVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (isVisible) "Hide password" else "Show password"
                IconButton(onClick = onVisibilityChange) {
                    Icon(imageVector = image, contentDescription = description)
                }
            },
            singleLine = true,
            shape = MaterialTheme.shapes.large,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black.copy(alpha = 0.7f),
                focusedLabelColor = Color.Black,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    SuperIDTheme {
        LoginScreen(onNavigateBack = {}, onForgotPasswordClick = {})
    }
} 