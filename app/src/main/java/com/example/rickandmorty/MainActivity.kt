package com.example.rickandmorty

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.rickandmorty.ui.theme.RickAndMortyTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppNavigation(modifier: Modifier) {
    var characters by remember { mutableStateOf<List<Character>>(emptyList()) }
    LaunchedEffect(Unit) {
        characters = fetchCharacters() // Llama a la función suspend
        Log.d("Rick And Morty API", "The characters: $characters")
    }

    val navController = rememberNavController()
    NavHost(navController, startDestination = "characterList") {
        composable("characterList") {
            CharacterListScreen(characters = characters, modifier) { character ->
                navController.navigate("characterDetail/${character.id}")
            }
        }
        composable("characterDetail/{characterId}") { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")?.toInt()
            val character = characters.find { it.id == characterId }
            character?.let { CharacterDetailScreen(it, modifier) }
        }
    }
}

@Composable
fun CharacterListScreen(characters: List<Character>, modifier: Modifier, onCharacterClick: (Character) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = modifier
    ) {
        items(characters) { character ->
            CharacterCard(character, onClick = { onCharacterClick(character) })
        }
    }
}

@Composable
fun CharacterCard(character: Character, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = character.name, style = MaterialTheme.typography.headlineLarge)
            Text(text = "Species: ${character.species}")
            Text(text = "Status: ${character.status}")
            AsyncImage(
                model = character.image,
                contentDescription = "Image of the character ${character.name}."
            )
        }
    }
}

@Composable
fun CharacterDetailScreen(character: Character, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = "Image of the character ${character.name}.",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = character.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Status: ${character.status}",
                style = MaterialTheme.typography.bodyLarge,
            )

            Text(
                text = "Species: ${character.species}",
                style = MaterialTheme.typography.bodyLarge,
            )

            Text(
                text = "Gender: ${character.gender}",
                style = MaterialTheme.typography.bodyLarge,
            )

            Text(
                text = "Location: ${character.location?.name ?: "Unknown"}",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RickAndMortyTheme {}
}

// Start to API Code

interface RickAndMortyApi {
    @GET("character/")
    suspend fun getCharacters(): RickAndMortyResponse
}

data class RickAndMortyResponse(
    val results: List<Character>
)

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String,
    val location: Location
)

data class Location(
    val name: String
)

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://rickandmortyapi.com/api/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val api: RickAndMortyApi = retrofit.create(RickAndMortyApi::class.java)

suspend fun fetchCharacters(): List<Character> {
    return try {
        val response = api.getCharacters()
        response.results
    } catch (e: Exception) {
        emptyList()
    }
}

// End to API Code