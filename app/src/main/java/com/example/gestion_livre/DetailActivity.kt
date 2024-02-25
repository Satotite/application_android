package com.example.gestion_livre

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide


class DetailBookActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Détail du livre"
            setDisplayHomeAsUpEnabled(true)
        }


        val book = intent.getSerializableExtra("book") as? Book ?: return


        val checkBox: CheckBox = findViewById(R.id.checkboxRead)
        checkBox.isChecked = book.isRead
        checkBox.setOnCheckedChangeListener { _, isChecked ->

            book.isRead = isChecked


            val resultIntent = Intent().apply {
                putExtra("book", book)
            }
            setResult(RESULT_OK, resultIntent)
        }

        val titleTextView: TextView = findViewById(R.id.bookTitleTextView)
        val authorTextView: TextView = findViewById(R.id.bookAuthorTextView)
        val coverImageView: ImageView = findViewById(R.id.imageViewCoverDetail)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val publishedDateTextView: TextView = findViewById(R.id.publishedDateTextView)
        val pageCountTextView: TextView = findViewById(R.id.pageCountTextView)
        val averageRatingTextView: TextView = findViewById(R.id.averageRatingTextView)

        // Set the book details on the views
        titleTextView.text = book.title
        authorTextView.text = book.author
        descriptionTextView.text = book.description
        publishedDateTextView.text = book.publishedDate
        pageCountTextView.text = book.pageCount.toString()
        averageRatingTextView.text = book.averageRating.toString()

        // Load the cover image
        Glide.with(this).load(book.coverUrl).into(coverImageView)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Prepare the result when navigating up
                val book = intent.getSerializableExtra("book") as? Book
                book?.let {
                    val resultIntent = Intent().apply {
                        putExtra("book", it)
                    }
                    setResult(RESULT_OK, resultIntent)
                }
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
