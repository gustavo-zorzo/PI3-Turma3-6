package br.com.superid

import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.superid.ui.theme.SuperIDTheme
import br.com.superid.viewmodel.RegisterUiState
import br.com.superid.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by registerViewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* Pode adicionar um título se desejar */ },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_desc)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    navigationIconContentColor = Color.Black
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.superidlogoescrito),
                contentDescription = stringResource(id = R.string.descricao_logo),
                modifier = Modifier
                    .size(150.dp)
                    .padding(top = 24.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            RegisterTextField(
                value = uiState.username,
                onValueChange = { registerViewModel.onUsernameChange(it) },
                labelResId = R.string.nome_usuario,
                hintResId = R.string.hint_nome_usuario,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            Spacer(modifier = Modifier.height(16.dp))

            RegisterTextField(
                value = uiState.email,
                onValueChange = { registerViewModel.onEmailChange(it) },
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
                onValueChange = { registerViewModel.onPasswordChange(it) },
                labelResId = R.string.senha,
                hintResId = R.string.hint_senha,
                isVisible = uiState.passwordVisible,
                onVisibilityChange = { registerViewModel.togglePasswordVisibility() },
                imeAction = ImeAction.Next,
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordTextField(
                value = uiState.confirmPassword,
                onValueChange = { registerViewModel.onConfirmPasswordChange(it) },
                labelResId = R.string.confirme_senha,
                hintResId = R.string.hint_confirme_senha,
                isVisible = uiState.confirmPasswordVisible,
                onVisibilityChange = { registerViewModel.toggleConfirmPasswordVisibility() },
                imeAction = ImeAction.Done,
                onDone = { focusManager.clearFocus() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = uiState.termsAccepted,
                    onCheckedChange = { registerViewModel.onTermsAcceptedChange(it) },
                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                )
                Text(
                    text = stringResource(id = R.string.termos),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { registerViewModel.attemptRegistration() },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium, // Equivalente a cornerRadius=\"12dp\"
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0E2F5A), // dark_blue
                    contentColor = Color.White
                )
                // Para a borda (stroke), pode ser necessário um Button com borda customizada
                // ou usar um Surface com borda envolvendo o Button
            ) {
                Text(text = stringResource(id = R.string.cadastre_se), modifier = Modifier.padding(vertical = 8.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun RegisterTextField(
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
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(id = hintResId), style = MaterialTheme.typography.bodyMedium) },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            shape = MaterialTheme.shapes.large, // app:boxCornerRadius=\"16dp\"
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, // app:boxStrokeColor=\"@color/black\"
                unfocusedBorderColor = Color.Black.copy(alpha = 0.7f), // Um pouco mais claro para unfocused
                focusedLabelColor = Color.Black,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )
    }
}

@Composable
private fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelResId: Int,
    hintResId: Int,
    isVisible: Boolean,
    onVisibilityChange: () -> Unit,
    imeAction: ImeAction,
    onNext: (() -> Unit)? = null,
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
fun RegisterScreenPreview() {
    SuperIDTheme {
        RegisterScreen(onNavigateBack = {})
    }
} 