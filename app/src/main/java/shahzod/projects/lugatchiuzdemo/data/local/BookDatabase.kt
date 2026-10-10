package shahzod.projects.lugatchiuzdemo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WordEntity::class], version = 1, exportSchema = false)
abstract class BookDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
}