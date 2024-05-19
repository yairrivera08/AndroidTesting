package com.eycr.tvguidance.views

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eycr.network.ApiOperation
import com.eycr.network.KtorClient
import com.eycr.network.models.domain.CharacterStatus
import com.eycr.network.models.domain.ShowCharacter
import com.eycr.tvguidance.components.character.CharacterNameplateComponent
import com.eycr.tvguidance.components.commons.CharacterImage
import com.eycr.tvguidance.components.commons.DataPoint
import com.eycr.tvguidance.components.commons.DataPointComponent
import com.eycr.tvguidance.components.commons.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel


class ShowCharacterRepository @Inject constructor(private val ktorClient: KtorClient) {
    suspend fun fetchCharacter(characterId: Int): ApiOperation<ShowCharacter> {
        return ktorClient.getCharacter(characterId)
    }
}

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val characterRepository: ShowCharacterRepository
): ViewModel() {
    private val _internalStorageFlow = MutableStateFlow<CharacterDetailsViewState>(
        value = CharacterDetailsViewState.Loading
    )
    val stateFlow = _internalStorageFlow.asStateFlow()

    fun fetchCharacter(characterId: Int) = viewModelScope.launch {
        _internalStorageFlow.update { return@update CharacterDetailsViewState.Loading }
        characterRepository.fetchCharacter(characterId)
            .onSuccess { character ->
                val dataPoints = buildList {
                        add(DataPoint("Last known location", character.location.name))
                        add(DataPoint("Species", character.species))
                        add(DataPoint("Gender", character.gender.displayName))
                        character.type.takeIf { it.isNotEmpty() }?.let { type -> add(DataPoint("Type", type)) }
                        add(DataPoint("Origin", character.origin.name))
                        add(DataPoint("Episode count", character.episodeIds.size.toString()))
                }
                _internalStorageFlow.update {
                    return@update CharacterDetailsViewState.Success(
                        character = character,
                        characterDataPoints = dataPoints
                    )
                }
            }
            .onFailure { exception ->
                _internalStorageFlow.update {
                    return@update CharacterDetailsViewState.Error(
                        message = exception.message ?: "Unknown error occurred"
                    )
                }
            }
    }
}

sealed interface CharacterDetailsViewState {
    object Loading: CharacterDetailsViewState
    data class Error(val message: String): CharacterDetailsViewState
    data class Success(
        val character: ShowCharacter,
        val characterDataPoints: List<DataPoint>
    ): CharacterDetailsViewState
}

@Composable
fun CharacterDetailsScreen(
    characterId: Int,
    viewModel: CharacterDetailsViewModel = hiltViewModel(),
    onEpisodeClicked: (Int) -> Unit
) {
    LaunchedEffect(key1 = Unit, block = {
        viewModel.fetchCharacter(characterId)
    })

    val state by viewModel.stateFlow.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 16.dp)
    ) {
        when (val viewState = state) {
            is CharacterDetailsViewState.Error -> TODO()
            CharacterDetailsViewState.Loading -> item { LoadingState() }
            is CharacterDetailsViewState.Success -> {
                with(viewState) {
                    item {
                        CharacterNameplateComponent(
                            name = character.name ?: "Name not found",
                            status = character.status ?: CharacterStatus.Unknown
                        )
                    }
                    item {
                        CharacterImage(imageUrl = character.imageUrl)
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                    items(characterDataPoints) {
                        Spacer(modifier = Modifier.height(32.dp))
                        DataPointComponent(dataPoint = it)
                    }
                    item { Spacer(modifier = Modifier.height(32.dp)) }
                    item {
                        Text(
                            text = "View all episodes",
                            color = Color.Red,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .border(
                                    width = 1.dp,
                                    color = Color.Red,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    onEpisodeClicked(characterId)
                                }
                                .padding(vertical = 8.dp)
                                .fillMaxWidth()
                        )
                    }

                    item { Spacer(modifier = Modifier.height(64.dp)) }
                }
            }
        }
    }
}
