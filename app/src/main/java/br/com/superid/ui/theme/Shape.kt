package br.com.superid.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Definição de Shapes (cantos arredondados)
// Os valores são exemplos e podem ser ajustados conforme o design do app
val AppShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp), // Usado para botões e cards no XML com ~12dp
    large = RoundedCornerShape(16.dp)  // Usado para OutlinedTextFields no XML com 16dp
) 