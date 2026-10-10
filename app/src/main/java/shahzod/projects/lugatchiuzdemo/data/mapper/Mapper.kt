package shahzod.projects.lugatchiuzdemo.data.mapper

import shahzod.projects.lugatchiuzdemo.data.local.WordEntity
import shahzod.projects.lugatchiuzdemo.domain.model.Word

fun WordEntity.toDomain(book: Int) = Word(
    id = id,
    book = book,
    unit = unit,
    english = english,
    uzbek = uzbek,
    isFavourite = isFavourite == 1
)