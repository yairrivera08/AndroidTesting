package com.eycr.tvguidance.components.character

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.eycr.network.models.domain.CharacterStatus
import com.eycr.tvguidance.ui.theme.Purple80

@Composable
fun CharacterNameplateComponent(name: String, status: CharacterStatus) {
    Column (modifier = Modifier.fillMaxWidth()){
        CharacterStatusComponent(characterStatus = status)
        Text(
            text = name,
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = Purple80
        )
    }
}

@Preview
@Composable
fun NamePlatePreviewAlive() {
    CharacterNameplateComponent(name = "Rick Sanchez", status = CharacterStatus.Alive)
}