package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var tvResultIcon: TextView
    private lateinit var tvResultTitle: TextView
    private lateinit var tvCategory: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvPercentage: TextView
    private lateinit var tvMessage: TextView
    private lateinit var btnRestart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        tvResultIcon = findViewById(R.id.tvResultIcon)
        tvResultTitle = findViewById(R.id.tvResultTitle)
        tvCategory = findViewById(R.id.tvCategory)
        tvScore = findViewById(R.id.tvScore)
        tvPercentage = findViewById(R.id.tvPercentage)
        tvMessage = findViewById(R.id.tvMessage)
        btnRestart = findViewById(R.id.btnRestart)

        val score = intent.getIntExtra("score", 0)
        val total = intent.getIntExtra("total", 10)
        val category = intent.getStringExtra("category") ?: "Quiz"

        val percentage = (score * 100) / total

        tvCategory.text = "Category: $category"
        tvScore.text = "Score: $score/$total"
        tvPercentage.text = "Percentage: $percentage%"

        when {

            percentage >= 90 -> {
                tvResultIcon.text = "🏆"
                tvResultTitle.text = "Quiz Master"
                tvMessage.text =
                    "Excellent work! Your coding knowledge is impressive."
            }

            percentage >= 70 -> {
                tvResultIcon.text = "💻"
                tvResultTitle.text = "Pro Coder"
                tvMessage.text =
                    "Great job! You have strong programming fundamentals."
            }

            percentage >= 50 -> {
                tvResultIcon.text = "📘"
                tvResultTitle.text = "Good Learner"
                tvMessage.text =
                    "Nice effort! Keep practicing to improve your skills."
            }

            else -> {
                tvResultIcon.text = "🔥"
                tvResultTitle.text = "Keep Practicing"
                tvMessage.text =
                    "Don't give up. Practice regularly and you'll improve quickly."
            }
        }

        btnRestart.setOnClickListener {

            val intent = Intent(this, CategoryActivity::class.java)

            startActivity(intent)

            finish()
        }
    }
}