package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CategoryActivity : AppCompatActivity() {

    private lateinit var btnAndroid: Button
    private lateinit var btnJava: Button
    private lateinit var btnKotlin: Button
    private lateinit var btnWeb: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        // Connect views
        btnAndroid = findViewById(R.id.btnAndroid)
        btnJava = findViewById(R.id.btnJava)
        btnKotlin = findViewById(R.id.btnKotlin)
        btnWeb = findViewById(R.id.btnWeb)

        // Set click listeners
        btnAndroid.setOnClickListener {
            openQuiz("Android")
        }

        btnJava.setOnClickListener {
            openQuiz("Java")
        }

        btnKotlin.setOnClickListener {
            openQuiz("Kotlin")
        }

        btnWeb.setOnClickListener {
            openQuiz("Web Development")
        }
    }

    private fun openQuiz(category: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("category", category)
        startActivity(intent)
    }
}