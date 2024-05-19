package com.eycr.tvguidance.components.commons

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.eycr.tvguidance.ui.theme.Purple80

@Composable
fun CharacterName(name: String) {
    Text(
        text = name,
        fontSize = 42.sp,
        fontWeight = FontWeight.Bold,
        color = Purple80
    )
}