package shahzod.projects.lugatchiuzdemo.data.local

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookDatabaseProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val BOOK_COUNT = 6
    }

    private val databases: Map<Int, BookDatabase> by lazy {
        (1..BOOK_COUNT).associateWith { book ->
            Room.databaseBuilder(context, BookDatabase::class.java, "essential_book$book.db")
                .createFromAsset("database/pdf_db/essential_book$book.db")
                .build()
        }
    }

    fun dao(book: Int): WordDao =
        requireNotNull(databases[book]) { "Noto'g'ri book: $book" }.wordDao()

    fun allDaos(): Map<Int, WordDao> = databases.mapValues { it.value.wordDao() }
}