package shahzod.projects.lugatchiuzdemo.data.local

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Query("SELECT * FROM words WHERE unit = :unit ORDER BY id")
    fun getUnitWords(unit: Int): Flow<List<WordEntity>>

    @Query("SELECT * FROM words WHERE is_favourite = 1 ORDER BY unit, id")
    fun getFavourites(): Flow<List<WordEntity>>

    @Query(
        """
        SELECT * FROM words
        WHERE english LIKE :query || '%' OR uzbek LIKE '%' || :query || '%'
        ORDER BY english
        LIMIT 50
        """
    )
    fun search(query: String): Flow<List<WordEntity>>

    @Query("UPDATE words SET is_favourite = :value WHERE id = :id")
    suspend fun setFavourite(id: Int, value: Int)
}