package br.com.superid.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Cores primárias baseadas nos XMLs
val DarkBlue = Color(0xFF0E2F5A)
val MediumBlue = Color(0xFF1A3B66)
val LightBlue = Color(0xFFA9D0F5)
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val Grey = Color(0xFFAAAAAA)

// Esquema de Cores Claro
private val LightColorScheme = lightColorScheme(
    primary = DarkBlue, // Cor primária principal
    secondary = MediumBlue, // Cor secundária
    tertiary = LightBlue, // Cor terciária
    background = White, // Fundo principal das telas
    surface = White, // Fundo de Cards, Sheets, Menus
    onPrimary = White, // Cor do texto/ícones sobre a cor primária
    onSecondary = White, // Cor do texto/ícones sobre a cor secundária
    onTertiary = Black, // Cor do texto/ícones sobre a cor terciária
    onBackground = Black, // Cor do texto/ícones sobre o fundo principal
    onSurface = Black, // Cor do texto/ícones sobre superfícies
    surfaceVariant = LightBlue, // Cor de fundo para componentes como Chip, Card (alternativa)
    outline = Black, // Cor das bordas (e.g., OutlinedTextField)
    // Outras cores podem ser definidas conforme necessário
)

// Esquema de Cores Escuro (Exemplo básico, pode ser refinado)
private val DarkColorScheme = darkColorScheme(
    primary = LightBlue, // Inverter para melhor contraste no escuro
    secondary = MediumBlue,
    tertiary = DarkBlue,
    background = Black,
    surface = Color(0xFF1C1B1F), // Cor padrão M3 para superfícies escuras
    onPrimary = Black,
    onSecondary = White,
    onTertiary = White,
    onBackground = White,
    onSurface = White,
    surfaceVariant = Color(0xFF49454F), // Cor padrão M3
    outline = Grey
)

// Função para obter o esquema de cores (pode ser usada em Theme.kt)
@Composable
fun getColorScheme(darkTheme: Boolean = isSystemInDarkTheme()): ColorScheme {
    return if (darkTheme) DarkColorScheme else LightColorScheme
} 