package com.ventthos.quizzappbasic

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val  quizzAppModel: GameModel by viewModels()

    lateinit var textView: TextView
    lateinit var answers: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.texto)
        answers = findViewById(R.id.respuestas)

        quizzAppModel.selectQuestions(4, 3)

        textView.setText( quizzAppModel.currentQuestion.question.questionId)
        val optionsArray = resources.getStringArray(quizzAppModel.currentQuestion.question.options)
        val selectedOption = optionsArray[quizzAppModel.currentQuestion.optionsSelected[0]]
        val selectedOption2 = optionsArray[quizzAppModel.currentQuestion.optionsSelected[1]]
        val selectedOption3 = optionsArray[quizzAppModel.currentQuestion.optionsSelected[2]]
        answers.text = "${selectedOption}, ${selectedOption2}, ${selectedOption3}"
    }
}