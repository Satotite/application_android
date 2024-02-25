package com.example.gestion_livre

import java.io.Serializable


data class Book(val id: String,
                val title: String,
                val author: String,
                val coverUrl: String,
                val description: String,
                val publishedDate: String,
                val pageCount: Int,
                val averageRating: Float,
                var isRead: Boolean = false
                ) : Serializable