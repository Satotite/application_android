package com.example.gestion_livre

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BookAdapter(
    var books: MutableList<Book>,
    private val context: Context,
    private val clickListener: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    inner class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val authorTextView: TextView = view.findViewById(R.id.authorTextView)
        val coverImageView: ImageView = view.findViewById(R.id.imageViewCover)

        fun bind(book: Book) {
            titleTextView.text = book.title
            authorTextView.text = book.author
            Glide.with(itemView.context).load(book.coverUrl).into(coverImageView)

            titleTextView.setTextColor(
                if (book.isRead) ContextCompat.getColor(context, R.color.readBookTitle)
                else ContextCompat.getColor(context, R.color.unreadBookTitle)
            )

            itemView.setOnClickListener { clickListener(book) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount(): Int = books.size

    fun updateBook(updatedBook: Book) {
        val index = books.indexOfFirst { it.id == updatedBook.id }
        if (index != -1) {
            books[index] = updatedBook
            notifyItemChanged(index)
        }
    }

}
