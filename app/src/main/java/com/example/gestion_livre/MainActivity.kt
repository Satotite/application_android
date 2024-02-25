package com.example.gestion_livre

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {


    override fun onStart() {
        super.onStart()
        FirebaseApp.initializeApp(this);
        val currentUser = Firebase.auth.currentUser
        if (currentUser == null) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        } else {

            val bookListIntent = Intent(this, BookListActivity::class.java)

            startActivity(bookListIntent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}
