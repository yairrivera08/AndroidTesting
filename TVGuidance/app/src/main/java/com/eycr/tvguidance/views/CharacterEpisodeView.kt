package com.eycr.tvguidance.views

import android.widget.Space
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eycr.network.KtorClient
import com.eycr.network.models.domain.Episode
import com.eycr.network.models.domain.ShowCharacter
import com.eycr.tvguidance.components.commons.CharacterImage
import com.eycr.tvguidance.components.commons.CharacterName
import com.eycr.tvguidance.components.commons.DataPoint
import com.eycr.tvguidance.components.commons.DataPointComponent
import com.eycr.tvguidance.components.commons.LoadingState
import com.eycr.tvguidance.components.episode.EpisodeRowComponent
import kotlinx.coroutines.launch

@Composable
fun CharacterEpisodeView(characterId: Int, ktorClient: KtorClient) {
    var characterState by remember {
        mutableStateOf<ShowCharacter?>(null)
    }
    var episodeState by remember {
        mutableStateOf<List<Episode>>(emptyList())
    }

    LaunchedEffect(key1 = Unit){
        ktorClient.getCharacter(characterId).onSuccess { character ->
            characterState = character
            launch {
                ktorClient.getEpisodes(character.episodeIds).onSuccess { episodes ->
                    episodeState = episodes
                }.onFailure {
                    // Handle error later
                }
            }
        }.onFailure {
            // Handle the errors
        }
    }

    characterState?.let { character ->
        MainScreen(showCharacter = character, episodes = episodeState)
    } ?: LoadingState()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(showCharacter: ShowCharacter, episodes: List<Episode>) {

    val episodesBySeasonMap = episodes.groupBy { it.seasonNumber }

    LazyColumn (contentPadding = PaddingValues(all = 16.dp)) {
        item { CharacterName(name = showCharacter.name) }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item {
            LazyRow {
                episodesBySeasonMap.forEach {mapEntry ->
                    item { DataPointComponent(
                        dataPoint = DataPoint(
                            title = "Season ${mapEntry.key}",
                            description = "${mapEntry.value.size} ep"))
                        Spacer(modifier = Modifier.width(32.dp))
                    }
                }
            }
        }
        item { CharacterImage(imageUrl = showCharacter.imageUrl) }
        item { Spacer(modifier = Modifier.height(32.dp)) }

        episodes.groupBy { it.seasonNumber }.forEach { mapEntry ->
            stickyHeader { SeasonHeader(seasonNumber = mapEntry.key) }
            item { Spacer(modifier = Modifier.height(16.dp))}
            items(mapEntry.value) { episode ->
                EpisodeRowComponent(episode = episode)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SeasonHeader(seasonNumber: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Gray)
            .padding(top = 8.dp, bottom = 16.dp)
    ) {
        Text(
            text = "Season $seasonNumber",
            color = Color.Magenta,
            fontSize = 32.sp,
            lineHeight = 32.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Magenta,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 4.dp)
        )
    }
}