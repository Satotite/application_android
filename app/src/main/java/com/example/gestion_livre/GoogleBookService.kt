package com.example.gestion_livre


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksService {
    @GET("books/v1/volumes")
    fun searchBooks(
        @Query("q") query: String,
        @Query("key") apiKey: String
    ): Call<BooksResponse>
}


