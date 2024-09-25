package upeu.edu.pe.gametictactoe.api


import upeu.edu.pe.gametictactoe.model.Game
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
interface ApiService {
    // Método para crear un nuevo juego en el backend
    @POST("api/games")
    fun createGame(@Body game: Game): Call<Game>

    @PATCH("api/games/{id}")
    fun updateWinner(@Path("id") gameId: Long, @Body updatedGame: Map<String, String>): Call<Game>

}
