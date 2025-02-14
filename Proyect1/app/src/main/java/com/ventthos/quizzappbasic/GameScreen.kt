package com.ventthos.quizzappbasic

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class GameScreen : AppCompatActivity() {
    private val  quizzAppModel: GameModel by viewModels()

    private lateinit var questionTextView: TextView
    private lateinit var answersBox: LinearLayout
    private lateinit var nextButton: Button
    private lateinit var backButton: Button

    private val numberOfQuestions = 4
    private val answersPerQuestion = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_screen)

        // Aqui vayan declarando sus elementos
        questionTextView = findViewById(R.id.questionText)
        answersBox = findViewById(R.id.answersBox)
        nextButton = findViewById(R.id.nextButton)
        backButton = findViewById(R.id.backButton)

        quizzAppModel.selectQuestions(numberOfQuestions, answersPerQuestion)

        prepareField()
        changeQuestion()

        //Aca pongan sus bindings
        nextButton.setOnClickListener {changeNextQuestion()}
        backButton.setOnClickListener {changePreviousQuestion()}
    }

    fun prepareField(){
        for(i in 0 until numberOfQuestions){
            val optionButton = Button(this)
            answersBox.addView(optionButton)
        }
    }

    fun changeQuestion(){
        val currentQuestion = quizzAppModel.currentQuestion
        val questionOptions = resources.getStringArray(currentQuestion.question.options)
        val currentQuestionOptions = currentQuestion.optionsSelected
        questionTextView.setText(currentQuestion.question.questionId)


        for (i in 0 until answersBox.childCount) {
            val optionButton = answersBox.getChildAt(i)
            if(optionButton is Button){
                optionButton.text = questionOptions[currentQuestionOptions[i]]
                optionButton.setOnClickListener { registerAnswer(currentQuestionOptions[i]) }
            }
        }
    }

    fun changeNextQuestion(){
        quizzAppModel.nextQuestion()
        changeQuestion()
    }

    fun changePreviousQuestion(){
        quizzAppModel.prevQuestion()
        changeQuestion()
    }

    fun registerAnswer(index: Int){
        if(quizzAppModel.currentQuestion.question.answered)
            return

        quizzAppModel.currentQuestion.question.answerGottenIndex = index

    }

    fun changeSylesAnswers(){
        if(!quizzAppModel.currentQuestion.question.answered)
            return
    }
}