# Rick and Morty Android App

This is an Android app built with Jetpack Compose, leveraging the Rick and Morty API to display a list of characters and their details. The app features a two-screen navigation structure: a list screen and a detail screen.

## Features

- **LazyVerticalGrid Character List**: Displays a scrollable list of characters in a grid.
- **Character Detail View**: On selecting a character, detailed information is displayed, including name, species, status, and location.
- **Navigation**: Uses Jetpack Navigation to transition between the character list and detail screens.
- **API Integration**: Uses Retrofit and Gson to fetch data from the Rick and Morty API.

## Project Structure

- **MainActivity**: Sets up the UI with a `Scaffold` layout and initiates `AppNavigation`.
- **AppNavigation**: Manages navigation between the character list and detail views.
- **CharacterListScreen**: Displays a list of characters using `LazyVerticalGrid`.
- **CharacterCard**: Composable for individual character items in the list.
- **CharacterDetailScreen**: Shows details for a selected character.
- **RickAndMortyApi**: Interface for API calls to fetch character data.
- **Data Models**: `Character` and `Location` classes model the API response data.

## Getting Started

### Prerequisites

- **Android Studio**: Latest version with Jetpack Compose support.
- **Internet Connection**: Required to fetch character data from the Rick and Morty API.

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/rickandmorty.git
   ```
2. Open the project in Android Studio.
3. Build and run the app on an emulator or Android device.

### Running the App

1. On launch, the app displays a grid of characters from the Rick and Morty API.
2. Tap on any character to view additional details.

## API Configuration

The app connects to the Rick and Morty API at `https://rickandmortyapi.com/api/`. The API configuration is defined using Retrofit:

```kotlin
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://rickandmortyapi.com/api/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
```

## License

This project is licensed under the GNU General Public License v3.0
GNU GPLv3 License
