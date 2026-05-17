package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    data class Question(
        val question: String,
        val options: List<String>,
        val correctAnswer: String
    )

    private var questions = listOf<Question>()

    private lateinit var tvTitle: TextView
    private lateinit var tvProgress: TextView
    private lateinit var tvTimer: TextView
    private lateinit var tvQuestion: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var rb1: RadioButton
    private lateinit var rb2: RadioButton
    private lateinit var rb3: RadioButton
    private lateinit var rb4: RadioButton
    private lateinit var btnNext: Button

    private var currentQuestionIndex = 0
    private var score = 0
    private var selectedCategory = "Android"

    private var countDownTimer: CountDownTimer? = null
    private val timePerQuestion: Long = 15000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTitle = findViewById(R.id.tvTitle)
        tvProgress = findViewById(R.id.tvProgress)
        tvTimer = findViewById(R.id.tvTimer)
        tvQuestion = findViewById(R.id.tvQuestion)
        radioGroup = findViewById(R.id.radioGroup)
        rb1 = findViewById(R.id.rb1)
        rb2 = findViewById(R.id.rb2)
        rb3 = findViewById(R.id.rb3)
        rb4 = findViewById(R.id.rb4)
        btnNext = findViewById(R.id.btnNext)

        selectedCategory = intent.getStringExtra("category") ?: "Android"
        tvTitle.text = "🧠 $selectedCategory Quiz"

        loadQuestionsByCategory()

        btnNext.setOnClickListener {
            checkAnswer()
        }
    }

    private fun loadQuestionsByCategory() {
        val rawQuestions = when (selectedCategory) {

            "Android" -> listOf(
                Question("Who developed Android?", listOf("Google", "Apple", "Microsoft", "IBM"), "Google"),
                Question("Android Studio is based on?", listOf("IntelliJ IDEA", "Eclipse", "NetBeans", "VS Code"), "IntelliJ IDEA"),
                Question("Android OS is based on?", listOf("Linux Kernel", "Windows", "MacOS", "Unix"), "Linux Kernel"),
                Question("APK stands for?", listOf("Android Package Kit", "App Kernel", "Android Program Kit", "App Key"), "Android Package Kit"),
                Question("Which file defines UI?", listOf("XML", "JSON", "HTML", "TXT"), "XML"),
                Question("Main Android component?", listOf("Activity", "Class", "Function", "Loop"), "Activity"),
                Question("Which layout uses XML?", listOf("UI Layout", "Database", "API", "Server"), "UI Layout"),
                Question("What is R in Android?", listOf("Resource", "Runtime", "Router", "Root"), "Resource"),
                Question("Android apps are written in?", listOf("Kotlin/Java", "Python", "C++", "Swift"), "Kotlin/Java"),
                Question("Which method starts activity?", listOf("startActivity()", "run()", "begin()", "launch()"), "startActivity()")
            )

            "Java" -> listOf(
                Question("Java was developed by?", listOf("Sun Microsystems", "Google", "Microsoft", "Apple"), "Sun Microsystems"),
                Question("Java is a?", listOf("Object Oriented Language", "Markup Language", "Database", "OS"), "Object Oriented Language"),
                Question("Java file extension?", listOf(".java", ".kt", ".py", ".cpp"), ".java"),
                Question("Java runs on?", listOf("JVM", "CPU", "RAM", "Compiler"), "JVM"),
                Question("Main method signature?", listOf("public static void main", "start()", "run()", "init()"), "public static void main"),
                Question("Inheritance keyword?", listOf("extends", "inherits", "implement", "super"), "extends"),
                Question("Java supports?", listOf("Multithreading", "Only UI", "Only DB", "Only Web"), "Multithreading"),
                Question("Java bytecode runs on?", listOf("JVM", "OS", "Compiler", "IDE"), "JVM"),
                Question("Which loop exists?", listOf("for", "repeat", "iterate", "loop"), "for"),
                Question("Java is?", listOf("Platform Independent", "Platform Dependent", "Hardware", "None"), "Platform Independent")
            )

            "Kotlin" -> listOf(
                Question("Kotlin developed by?", listOf("JetBrains", "Google", "Oracle", "Microsoft"), "JetBrains"),
                Question("Kotlin is used for?", listOf("Android Development", "Game Dev", "Networking", "DB only"), "Android Development"),
                Question("Immutable variable?", listOf("val", "var", "let", "const"), "val"),
                Question("Mutable variable?", listOf("var", "val", "final", "static"), "var"),
                Question("Kotlin file extension?", listOf(".kt", ".java", ".kot", ".k"), ".kt"),
                Question("Kotlin runs on?", listOf("JVM", "CPU", "RAM", "Browser"), "JVM"),
                Question("Function keyword?", listOf("fun", "function", "def", "method"), "fun"),
                Question("Null safety feature?", listOf("Kotlin", "Java", "C", "C++"), "Kotlin"),
                Question("Kotlin interoperable with?", listOf("Java", "Python", "C#", "Swift"), "Java"),
                Question("Android prefers Kotlin because?", listOf("Modern & Safe", "Slow", "Heavy", "Old"), "Modern & Safe")
            )

            "Web Development" -> listOf(
                Question("HTML is used for?", listOf("Structure", "Styling", "Logic", "Database"), "Structure"),
                Question("CSS is used for?", listOf("Styling", "Structure", "Logic", "Backend"), "Styling"),
                Question("JavaScript is used for?", listOf("Interactivity", "Styling", "DB", "OS"), "Interactivity"),
                Question("HTML full form?", listOf("HyperText Markup Language", "HighText Machine Language", "Hyper Tool Language", "None"), "HyperText Markup Language"),
                Question("CSS full form?", listOf("Cascading Style Sheets", "Computer Style System", "Creative Style Sheet", "None"), "Cascading Style Sheets"),
                Question("Hyperlink tag?", listOf("<a>", "<p>", "<div>", "<img>"), "<a>"),
                Question("Backend language example?", listOf("Node.js", "HTML", "CSS", "XML"), "Node.js"),
                Question("Database language?", listOf("SQL", "HTML", "CSS", "JS"), "SQL"),
                Question("Frontend framework?", listOf("React", "MySQL", "Docker", "MongoDB"), "React"),
                Question("Browser renders?", listOf("HTML/CSS/JS", "Java only", "Python only", "C++ only"), "HTML/CSS/JS")
            )

            else -> listOf()
        }

        questions = rawQuestions.shuffled()
        currentQuestionIndex = 0
        score = 0
        loadQuestion()
    }

    private fun loadQuestion() {
        val q = questions[currentQuestionIndex]

        resetOptionStyle()

        tvProgress.text = "Question ${currentQuestionIndex + 1} of ${questions.size}"
        tvQuestion.text = q.question

        val shuffledOptions = q.options.shuffled()

        rb1.text = shuffledOptions[0]
        rb2.text = shuffledOptions[1]
        rb3.text = shuffledOptions[2]
        rb4.text = shuffledOptions[3]

        rb1.tag = shuffledOptions[0]
        rb2.tag = shuffledOptions[1]
        rb3.tag = shuffledOptions[2]
        rb4.tag = shuffledOptions[3]

        radioGroup.clearCheck()

        btnNext.text =
            if (currentQuestionIndex == questions.size - 1) "Finish" else "Next"

        startTimer()
    }

    private fun startTimer() {
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(timePerQuestion, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvTimer.text = "Time: ${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                moveNext()
            }
        }.start()
    }

    private fun checkAnswer() {
        countDownTimer?.cancel()

        val selectedId = radioGroup.checkedRadioButtonId

        if (selectedId == -1) {
            Toast.makeText(this, "Select an answer", Toast.LENGTH_SHORT).show()
            startTimer()
            return
        }

        val selectedButton = findViewById<RadioButton>(selectedId)
        val selectedOption = selectedButton.tag.toString()
        val correctAnswer = questions[currentQuestionIndex].correctAnswer

        rb1.isEnabled = false
        rb2.isEnabled = false
        rb3.isEnabled = false
        rb4.isEnabled = false
        btnNext.isEnabled = false

        val buttons = listOf(rb1, rb2, rb3, rb4)

        for (button in buttons) {
            val option = button.tag.toString()

            if (option == correctAnswer) {
                button.setBackgroundResource(R.drawable.option_correct)
            }

            if (button.id == selectedId && option != correctAnswer) {
                button.setBackgroundResource(R.drawable.option_wrong)
            }
        }

        if (selectedOption == correctAnswer) {
            score++
        }

        Handler(Looper.getMainLooper()).postDelayed({
            moveNext()
        }, 1200)
    }

    private fun resetOptionStyle() {
        val buttons = listOf(rb1, rb2, rb3, rb4)

        for (button in buttons) {
            button.setBackgroundResource(R.drawable.option_default)
            button.isEnabled = true
            button.setTextColor(resources.getColor(android.R.color.black))
        }

        btnNext.isEnabled = true
    }

    private fun moveNext() {
        currentQuestionIndex++

        if (currentQuestionIndex < questions.size) {
            loadQuestion()
        } else {
            showResult()
        }
    }

    private fun showResult() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("score", score)
        intent.putExtra("total", questions.size)
        intent.putExtra("category", selectedCategory)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}