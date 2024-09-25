package upeu.edu.pe.gametictactoe

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import upeu.edu.pe.gametictactoe.api.RetrofitClient
import upeu.edu.pe.gametictactoe.model.Game
import upeu.edu.pe.gametictactoe.ui.presentation.screens.DefaultPreview
import upeu.edu.pe.gametictactoe.ui.presentation.screens.PlayerInputScreen
import upeu.edu.pe.gametictactoe.ui.presentation.screens.TicTacToeBoard
import upeu.edu.pe.gametictactoe.ui.theme.GameTICTACTOETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            /*var gameCreated by remember { mutableStateOf<Game?>(null) }
            var gameStarted by remember { mutableStateOf(false) } // Variable para controlar el estado del juego
            var playerX by remember { mutableStateOf("") }
            var playerO by remember { mutableStateOf("") }

            // Aquí decides si mostrar el tablero o la pantalla de entrada de nombres
            if (gameStarted) {
                TicTacToeBoard(gameCreated!!, playerX, playerO) // Muestra el tablero si el juego ha comenzado
            } else {
                PlayerInputScreen(
                    onGameCreated = { game, pX, pO ->
                        gameCreated = game
                        playerX = pX
                        playerO = pO
                        gameStarted = true // Cambia a la pantalla del tablero
                    }
                )
            }*/
            //DefaultPreview()
            ScaffoldExample()
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldExample() {
    var presses by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.inversePrimary,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("TIC TAC TOE")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "",
                )
            }
        },
        /*floatingActionButton = {
            FloatingActionButton(onClick = { presses++ }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }*/
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            /*Text(
                modifier = Modifier.padding(8.dp),
                text =
                """
            This is an example of a scaffold. It uses the Scaffold composable's parameters to create a screen with a simple top app bar, bottom app bar, and floating action button.

            It also contains some basic inner content, such as this text.

            You have pressed the floating action button $presses times.
        """.trimIndent(),
            )*/
            var gameCreated by remember { mutableStateOf<Game?>(null) }
            var gameStarted by remember { mutableStateOf(false) } // Variable para controlar el estado del juego
            var playerX by remember { mutableStateOf("") }
            var playerO by remember { mutableStateOf("") }

            // Aquí decides si mostrar el tablero o la pantalla de entrada de nombres
            if (gameStarted) {
                TicTacToeBoard(gameCreated!!, playerX, playerO) // Muestra el tablero si el juego ha comenzado
            } else {
                PlayerInputScreen(
                    onGameCreated = { game, pX, pO ->
                        gameCreated = game
                        playerX = pX
                        playerO = pO
                        gameStarted = true // Cambia a la pantalla del tablero
                    }
                )
            }
            // Button(onClick = {}) {
            //Text(text = "A")

            //}
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GameTICTACTOETheme {
        Greeting("Android")
    }
}