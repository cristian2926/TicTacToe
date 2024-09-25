package upeu.edu.pe.gametictactoe.ui.presentation.screens

import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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



@Composable
fun PlayerInputScreen(
    onGameCreated: (Game, String, String) -> Unit // Agrega este callback
) {
    var playerX by remember { mutableStateOf("") }
    var playerO by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = playerX,
            onValueChange = { playerX = it },
            label = { Text("Nombre del Jugador X") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = playerO,
            onValueChange = { playerO = it },
            label = { Text("Nombre del Jugador O") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (playerX.isNotBlank() && playerO.isNotBlank()) {
                    createNewGame(playerX, playerO) { newGame, error ->
                        if (error == null) {
                            onGameCreated(newGame!!, playerX, playerO) // Llamamos al callback cuando el juego se crea
                        } else {
                            errorMessage = error
                        }
                    }
                } else {
                    errorMessage = "Por favor ingresa los nombres de ambos jugadores"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar Juego")
        }

        errorMessage?.let {
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
        }
    }
}


// Función para crear un nuevo juego en el backend
fun createNewGame(playerX: String, playerO: String, onGameCreated: (Game?, String?) -> Unit) {
    val newGame = Game(0, playerX, playerO, "_________", false, null)
    RetrofitClient.instance.createGame(newGame)
        .enqueue(object : Callback<Game> {
            override fun onResponse(call: Call<Game>, response: Response<Game>) {
                if (response.isSuccessful) {
                    response.body()?.let { onGameCreated(it, null) }
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("Game", "Error al crear el juego: $error")
                    onGameCreated(null, error ?: "Error al crear el juego")
                }
            }

            override fun onFailure(call: Call<Game>, t: Throwable) {
                Log.e("Game", "Error de conexión: ${t.message}")
                onGameCreated(null, t.message ?: "Error de conexión")
            }
        })
}

@Composable
fun TicTacToeBoard(game: Game, playerX: String, playerO: String) {
    var board by remember { mutableStateOf(game.board.toCharArray()) }
    var currentPlayer by remember { mutableStateOf("X") }
    var result by remember { mutableStateOf<String?>(null) }
    var gameFinished by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = result?.let { if (it == "EMPATE") "Empate" else "Ganador: $it" }
                ?: "Turno del jugador $currentPlayer",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Board(board = board, onCellClick = { index ->
            if (board[index] == '_' && !gameFinished) {
                board[index] = currentPlayer.single()
                currentPlayer = if (currentPlayer == "X") "O" else "X"
                checkWinner(board, game.id, playerX, playerO) { gameResult ->
                    if (gameResult != null) {
                        result = gameResult
                        gameFinished = true
                    }
                }
            }
        })

        if (gameFinished) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                board = CharArray(9) { '_' }
                currentPlayer = "X"
                result = null
                gameFinished = false
            }) {
                Text("Reiniciar Juego")
            }
        }
    }
}



@Composable
fun Board(board: CharArray, onCellClick: (Int) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, // Centramos las filas
        verticalArrangement = Arrangement.spacedBy(4.dp) // Espaciado entre filas
    ) {
        for (row in 0..2) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp) // Espaciado entre columnas
            ) {
                for (col in 0..2) {
                    val index = row * 3 + col
                    BoardCell(value = board[index].toString(), onClick = { onCellClick(index) })
                }
            }
        }
    }
}



@Composable
fun BoardCell(value: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(100.dp) // Tamaño de la celda
            .padding(4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp) // Sombra para el estilo
    ) {
        Box(
            contentAlignment = Alignment.Center // Centrar el texto "X" u "O"
        ) {
            Text(
                text = if (value == "_") "" else value, // No mostrar el guion bajo
                style = MaterialTheme.typography.headlineLarge, // Texto grande
                color = if (value == "X") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary, // Color diferente para "X" y "O"
                fontSize = 36.sp, // Aumentar el tamaño de la letra
                textAlign = TextAlign.Center // Asegurar que esté centrado
            )
        }
    }
}


fun checkWinner(
    board: CharArray,
    gameId: Long,
    playerX: String,
    playerO: String,
    onResult: (String?) -> Unit // Usamos onResult para manejar ganador o empate
) {
    val winPatterns = listOf(
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // Filas
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // Columnas
        listOf(0, 4, 8), listOf(2, 4, 6)  // Diagonales
    )

    // Revisar si hay un ganador
    for (pattern in winPatterns) {
        val (a, b, c) = pattern
        if (board[a] != '_' && board[a] == board[b] && board[b] == board[c]) {
            val winner = if (board[a] == 'X') playerX else playerO
            updateGameWinner(gameId, winner) // Guardar el ganador en la base de datos
            onResult(winner) // Retornar el ganador
            return
        }
    }

    // Revisar si hay empate (tablero lleno)
    if (board.none { it == '_' }) {
        updateGameWinner(gameId, "EMPATE") // Guardar "EMPATE" en la base de datos
        onResult("EMPATE") // Retornar que es empate
    } else {
        onResult(null) // Si no hay ganador ni empate, continúa el juego
    }
}


fun updateGameWinner(gameId: Long, winner: String) {
    val updatedGame = mapOf("winner" to winner)
    RetrofitClient.instance.updateWinner(gameId, updatedGame)
        .enqueue(object : Callback<Game> {
            override fun onResponse(call: Call<Game>, response: Response<Game>) {
                if (response.isSuccessful) {
                    Log.d("Game", "Resultado guardado: $winner")
                } else {
                    Log.e("Game", "Error al guardar el resultado: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Game>, t: Throwable) {
                Log.e("Game", "Error de conexión: ${t.message}")
            }
        })
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
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
}