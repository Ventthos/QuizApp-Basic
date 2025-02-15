package com.ventthos.quizzappbasic

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import com.google.android.material.snackbar.Snackbar

class GameScreen : AppCompatActivity() {
    private val quizzAppModel: GameModel by viewModels()

    private lateinit var questionTextView: TextView
    private lateinit var answersBox: LinearLayout
    private lateinit var nextButton: Button
    private lateinit var backButton: Button
    private lateinit var categoryImage: ImageView
    private lateinit var categoryText: TextView
    private lateinit var questionCounterText: TextView
    private lateinit var header: LinearLayout

    private val numberOfQuestions = 10
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
        categoryImage = findViewById(R.id.categoryImage)
        categoryText = findViewById(R.id.categoryText)
        questionCounterText = findViewById(R.id.questionCounterText)
        header = findViewById(R.id.header)

        quizzAppModel.selectQuestions(numberOfQuestions, answersPerQuestion)

        prepareField()
        changeQuestion()

        //Aca pongan sus bindings
        nextButton.setOnClickListener {changeNextQuestion()}
        backButton.setOnClickListener {changePreviousQuestion()}
    }

    fun prepareField(){
        for(i in 0 until answersPerQuestion) {
            val optionButton = Button(this)
            optionButton.setTextColor(Color.BLACK)
            optionButton.setBackgroundResource(R.drawable.rounded_layer_container)

            answersBox.addView(optionButton)
        }
    }

    fun changeQuestion(){
        val currentQuestion = quizzAppModel.currentQuestion
        val questionOptions = resources.getStringArray(currentQuestion.question.options)
        val currentQuestionOptions = currentQuestion.optionsSelected
        questionTextView.setText(currentQuestion.question.questionId)
        categoryImage.setImageResource(currentQuestion.question.category.image)
        header.setBackgroundColor(ContextCompat.getColor(this, currentQuestion.question.category.colorCode))
        categoryText.setText(currentQuestion.question.category.categoryText)
        //Cambiar esto por un R string
        questionCounterText.text = "Pregunta ${quizzAppModel.CurrentQuestionIndex+1} / ${numberOfQuestions}"

        for (i in 0 until answersBox.childCount) {
            val optionButton = answersBox.getChildAt(i)
            if(optionButton is Button){
                optionButton.text = questionOptions[currentQuestionOptions[i]]
                optionButton.tag = currentQuestionOptions[i]
                optionButton.setOnClickListener { registerAnswer(currentQuestionOptions[i]) }
            }
        }
        changeSylesAnswers()
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

        quizzAppModel.currentQuestion.question.answered = true
        quizzAppModel.currentQuestion.question.answerGottenIndex = index
        changeSylesAnswers()
    }

    fun changeSylesAnswers(){
        for (i in 0 until answersBox.childCount) {
            val optionButton = answersBox.getChildAt(i)
            if(optionButton is Button) {
                optionButton.setBackgroundResource(R.drawable.rounded_layer_container)
            }
        }

        if(!quizzAppModel.currentQuestion.question.answered)
            return

        val currentQuestion = quizzAppModel.currentQuestion.question
        val selectedButton =answersBox.children.filter { it.tag == currentQuestion.answerGottenIndex}.elementAt(0)

        if(currentQuestion.answerGottenIndex == currentQuestion.answerIndex){
            selectedButton.setBackgroundResource(R.drawable.rounded_layer_container_green)
            return
        }
        selectedButton.setBackgroundResource(R.drawable.rounded_layer_container_red)

    }
}