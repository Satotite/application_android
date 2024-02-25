package com.example.gestion_livre

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.app.Activity
import android.view.Menu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.ActivityResultLauncher

class BookListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: BookAdapter
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var startForResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        sharedPrefs = getSharedPreferences("BookPrefs", Context.MODE_PRIVATE)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Liste des livres"
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))

        recyclerView = findViewById(R.id.recyclerViewBooks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getSerializableExtra("book")?.let { updatedBook ->
                    bookAdapter.updateBook(updatedBook as Book)

                    with(sharedPrefs.edit()) {
                        putBoolean(updatedBook.id, updatedBook.isRead)
                        apply()
                    }
                }
            }
        }

        bookAdapter = BookAdapter(mutableListOf(), this) { book ->

            val intent = Intent(this@BookListActivity, DetailBookActivity::class.java).apply {
                putExtra("book", book)
            }
            startForResult.launch(intent)
        }

        recyclerView.adapter = bookAdapter

        fetchBooksFromGoogleBooksApi()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }




    private fun fetchBooksFromGoogleBooksApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(GoogleBooksService::class.java)

        service.searchBooks("android development","AIzaSyAnE2Rj0DFz1rrUgnzu8GLyTEIuI3zmBTI").enqueue(object : Callback<BooksResponse> {
            override fun onResponse(call: Call<BooksResponse>, response: Response<BooksResponse>) {
                if (response.isSuccessful) {
                    val booksResponse = response.body()
                    val books = booksResponse?.items?.map { item ->
                        val id = generateBookId(item)
                        Book(
                            id = id,
                            title = item.volumeInfo.title,
                            author = item.volumeInfo.authors?.joinToString(separator = ", ") ?: "Unknown Author",
                            coverUrl = item.volumeInfo.imageLinks?.thumbnail ?: "",
                            description = item.volumeInfo.description ?: "Pas de description disponible.",
                            publishedDate = item.volumeInfo.publishedDate ?: "Date de publication inconnue",
                            pageCount = item.volumeInfo.pageCount ?: 0,
                            averageRating = item.volumeInfo.averageRating ?: 0f
                        )
                    } ?: listOf()
                    updateBooksList(books)
                }
            }

            override fun onFailure(call: Call<BooksResponse>, t: Throwable) {
                Log.e("BookListActivity", "API call failed", t)
            }

        })
    }

    private fun generateBookId(item: BookItem): String {

        return item.volumeInfo.title + item.volumeInfo.authors?.joinToString(separator = ", ")
    }

    private fun updateBooksList(books: List<Book>) {
        books.forEach { book ->
            book.isRead = sharedPrefs.getBoolean(book.id, false)
        }
        bookAdapter.books = books.toMutableList()
        bookAdapter.notifyDataSetChanged()


        sharedPrefs.all.forEach { (key, value) ->
            Log.d("BookListActivity", "SharedPreferences - Key: $key, Value: $value")
        }
    }

}
