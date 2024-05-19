package com.eycr.tvguidance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.eycr.network.KtorClient
import com.eycr.tvguidance.ui.theme.PurpleGrey40
import com.eycr.tvguidance.ui.theme.TVGuidanceTheme
import com.eycr.tvguidance.views.CharacterDetailsScreen
import com.eycr.tvguidance.views.CharacterEpisodeView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var ktorClient: KtorClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            TVGuidanceTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = PurpleGrey40) {
                    NavHost(navController =  navController, startDestination = "character_details") {
                        composable("character_details"){
                            CharacterDetailsScreen(
                                ktorClient = ktorClient,
                                characterId = 1
                            ) {
                                navController.navigate("character_episodes/$it")
                            }
                        }
                        composable(
                            route = "character_episodes/{characterId}",
                            arguments = listOf(
                                navArgument("characterId") { type = NavType.IntType}
                            )
                        ) { backStackEntry ->
                            val characterId: Int = backStackEntry.arguments?.getInt("characterId") ?: -1
                            CharacterEpisodeView(
                                characterId = characterId,
                                ktorClient = ktorClient
                            )
                        }
                    }



                }
            }
        }
    }
}

@Composable
fun CharacterEpisodeScreen(characterId: Int) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Character Episode Screen: $characterId", fontSize = 28.sp)
    }
}