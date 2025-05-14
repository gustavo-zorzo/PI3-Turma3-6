package br.com.superid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.com.superid.LoginScreen
import br.com.superid.RegisterScreen
import br.com.superid.TermsScreen
import br.com.superid.WelcomeScreen
// Importar ViewModels se forem passados explicitamente, ou usar hiltViewModel() dentro do composable de destino

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Welcome.route) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onNavigateToTerms = { navController.navigate(Screen.Terms.route) }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                // loginViewModel = viewModel(), // Pode ser instanciado aqui ou injetado
                onNavigateBack = { navController.popBackStack() },
                onForgotPasswordClick = { /* TODO: Implementar navegação para esqueci senha */ }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                // registerViewModel = viewModel(), // Pode ser instanciado aqui ou injetado
                onNavigateBack = { navController.popBackStack() }
                // A lógica de onRegisterClick (do ViewModel) pode navegar para outra tela no sucesso
            )
        }
        composable(Screen.Terms.route) {
            TermsScreen(
                onConfirmClick = {
                    // Volta para a WelcomeActivity limpando a pilha acima dela
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                    // Ou, se a intenção é apenas voltar para a tela anterior que a chamou (Welcome ou Register):
                    // navController.popBackStack()
                }
            )
        }
        // Adicionar outros destinos aqui
    }
} 