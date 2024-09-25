package upeu.edu.pe.gametictactoe.model

data class Game(
    val id: Long,
    val playerX: String,
    val playerO: String,
    val board: String,  // Representación del tablero, por ejemplo "XOXOXO___"
    val isFinished: Boolean,
    val winner: String?
)