package com.eycr.tvguidance.components.character

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eycr.network.models.domain.CharacterStatus
import com.eycr.tvguidance.ui.theme.TVGuidanceTheme

@Composable
fun CharacterStatusComponent(characterStatus: CharacterStatus) {
    Row(
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .border(width = 2.dp, color = characterStatus.color, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text( text = "Status: ", fontSize = 14.sp, color = Color.White)
        Text( text = characterStatus.displayName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Preview
@Composable
fun CharacterAlive(){
    TVGuidanceTheme {
        CharacterStatusComponent(characterStatus = CharacterStatus.Alive)
    }
}
@Preview
@Composable
fun CharacterDead(){
    TVGuidanceTheme {
        CharacterStatusComponent(characterStatus = CharacterStatus.Dead)
    }
}
@Preview
@Composable
fun CharacterUnknow(){
    TVGuidanceTheme {
        CharacterStatusComponent(characterStatus = CharacterStatus.Unknown)
    }
}