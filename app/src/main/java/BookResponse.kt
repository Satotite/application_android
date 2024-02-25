package com.example.gestion_livre


data class BooksResponse(val items: List<BookItem>)

data class BookItem(val volumeInfo: VolumeInfo)

data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val imageLinks: ImageLinks?,
    val description: String?,
    val publishedDate: String?,
    val pageCount: Int?,
    val averageRating: Float?
)

data class ImageLinks(val thumbnail: String)
