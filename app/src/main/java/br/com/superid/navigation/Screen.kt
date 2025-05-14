package br.com.superid.navigation

// Sealed class para definir as rotas da aplicação
sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Terms : Screen("terms")
    // Adicionar outras telas aqui se necessário
} 