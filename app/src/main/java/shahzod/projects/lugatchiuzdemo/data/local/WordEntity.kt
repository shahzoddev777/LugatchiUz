package shahzod.projects.lugatchiuzdemo.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val unit: Int,
    val english: String,
    val uzbek: String,
    @ColumnInfo(name = "is_favourite", defaultValue = "0")
    val isFavourite: Int = 0
)