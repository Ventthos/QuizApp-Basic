package com.ventthos.quizzappbasic

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
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
val CATEGORY_TAG: Category = Category.SCIENCE

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
    private lateinit var hintButton: ImageButton
    private lateinit var hintsAvailable: TextView
    private lateinit var hintInformer: ImageView

    private val numberOfQuestions = 10
    private var answersPerQuestion = 3

    private var maximumOfHints = 3
    private var numberOfHints = maximumOfHints

    val difficulties = mapOf(
        "Fácil" to 2,
        "Normal" to 3,
        "Difícil" to 4
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_screen)

        questionTextView = findViewById(R.id.questionText)
        answersBox = findViewById(R.id.answersBox)
        nextButton = findViewById(R.id.nextButton)
        backButton = findViewById(R.id.backButton)
        categoryImage = findViewById(R.id.categoryImage)
        categoryText = findViewById(R.id.categoryText)
        questionCounterText = findViewById(R.id.questionCounterText)
        header = findViewById(R.id.header)
        hintButton = findViewById(R.id.hintButton)
        hintsAvailable = findViewById(R.id.hintsAvailable)
        hintInformer = findViewById(R.id.hintInformer)

        if(!quizzAppModel.Initialized){
            val savedDifficulty = intent.getStringExtra(EXTRA_KEY_DIFFICULTY)
            maximumOfHints = intent.getIntExtra(EXTRA_KEY_HINTS_QUANTITY, 3)
            numberOfHints = maximumOfHints

            answersPerQuestion = difficulties[savedDifficulty] ?: 3

            quizzAppModel.HintsCuantity = numberOfHints
            quizzAppModel.NumberOfAnswers = answersPerQuestion
            quizzAppModel.Difficulty = savedDifficulty ?: "Normal"
        }
        prepareField()

        //Aca pongan sus bindings
        nextButton.setOnClickListener {changeNextQuestion()}
        backButton.setOnClickListener {changePreviousQuestion()}
        hintButton.setOnClickListener { useHint() }


        //Lanzar ruleta
        if(!quizzAppModel.Initialized){
            quizzAppModel.Initialized = true
            lauchRulette()
        }
        else{
            changeQuestion()
        }

    }

    fun prepareField(){
        hintsAvailable.text = "${quizzAppModel.HintsCuantity}"
        for(i in 0 until quizzAppModel.NumberOfAnswers) {
            val optionButton = Button(this)
            optionButton.setTextColor(Color.BLACK)
            optionButton.setBackgroundResource(R.drawable.rounded_layer_container)

            answersBox.addView(optionButton)
        }
    }

    fun changeQuestion(){
        if(quizzAppModel.currentQuantityOfQuestions == 0)
            return

        val currentQuestion = quizzAppModel.currentQuestion
        val questionOptions = resources.getStringArray(currentQuestion.question.options)
        val currentQuestionOptions = currentQuestion.optionsSelected
        questionTextView.setText(currentQuestion.question.questionId)
        categoryImage.setImageResource(currentQuestion.question.category.image)
        header.setBackgroundColor(ContextCompat.getColor(this, currentQuestion.question.category.colorCode))
        categoryText.setText(currentQuestion.question.category.categoryText)

        showCheater()

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

    fun showCheater(){
        if(quizzAppModel.currentQuestion.optionsWithHint.isEmpty()){
            hintInformer.visibility = View.INVISIBLE
            return
        }
        hintInformer.visibility = View.VISIBLE
    }

    fun changeNextQuestion() {

        // Verifica si hemos llegado a la última pregunta (pregunta 10 en este caso)
        if (quizzAppModel.CurrentQuestionIndex == 9 && quizzAppModel.currentQuestion.question.answered) { // 9 porque es la pregunta 10, y los índices comienzan en 0
            // Si es la última pregunta, pasar a la pantalla de resultados
            goToResultScreen()
            return
        }

        // Si no es la última pregunta, continúa a la siguiente
        if(quizzAppModel.lastQuestion.question.answered &&
            quizzAppModel.CurrentQuestionIndex == quizzAppModel.currentQuantityOfQuestions -1
            && quizzAppModel.currentQuantityOfQuestions != 10){
                
            lauchRulette()
            return
        }

        quizzAppModel.nextQuestion()
        changeQuestion()
    }

    fun goToResultScreen() {
        // Envía la puntuación y la dificultad a la pantalla de resultados
        val intent = Intent(this, ResultScreen::class.java)
        intent.putExtra("SCORE", quizzAppModel.score) // Pasa la puntuación
        intent.putExtra("DIFFICULTY", quizzAppModel.Difficulty) // Pasa la dificultad
        startActivity(intent)
    }

    fun changePreviousQuestion(){
        quizzAppModel.prevQuestion()
        changeQuestion()
    }

    fun registerAnswer(index: Int, hint: Boolean = false){
        if(quizzAppModel.currentQuestion.question.answered)
            return

        quizzAppModel.currentQuestion.question.answered = true
        quizzAppModel.currentQuestion.question.answerGottenIndex = index

        val isCorrect = quizzAppModel.currentQuestion.question.answerIndex == index
        val hintsUsed = quizzAppModel.currentQuestion.optionsWithHint.size
        val selectedDifficulty = quizzAppModel.Difficulty

        if(!hint){
            quizzAppModel.addToStreak(isCorrect)
            hintsAvailable.text = "${quizzAppModel.HintsCuantity}"
        }

        quizzAppModel.updateScore(isCorrect, hintsUsed, selectedDifficulty)

        changeSylesAnswers()
    }

    fun changeSylesAnswers(){
        for (i in 0 until answersBox.childCount) {
            val optionButton = answersBox.getChildAt(i)
            if(optionButton !is Button)
                return

            optionButton.isEnabled = true

            if(optionButton.tag !in quizzAppModel.currentQuestion.optionsWithHint){
                optionButton.setBackgroundResource(R.drawable.rounded_layer_container)
                continue
            }
            optionButton.setBackgroundResource(R.drawable.rounded_layer_container_gray)
            optionButton.isEnabled = false
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

    fun useHint(){
        val currentQuestion = quizzAppModel.currentQuestion
        if (currentQuestion.question.answered || quizzAppModel.HintsCuantity == 0){
            return
        }

        val possibleHints = currentQuestion.optionsSelected.filter { it !in currentQuestion.optionsWithHint && it != currentQuestion.question.answerIndex}

        val hint = possibleHints.random()
        currentQuestion.optionsWithHint.add(hint)

        if(possibleHints.size == 1){
            registerAnswer(currentQuestion.question.answerIndex, true)
            quizzAppModel.addToStreak(false)
        }

        quizzAppModel.HintsCuantity -= 1
        hintsAvailable.text = "${quizzAppModel.HintsCuantity}"

        changeSylesAnswers()
        showCheater()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == 1 && resultCode == RESULT_OK) {
            val currentCategory =  data?.getSerializableExtra(QUESTION_CATEGORY_EXTRA) as? Category ?: Category.SCIENCE
            quizzAppModel.selectQuestions(1, currentCategory)

            quizzAppModel.nextQuestion()

            changeQuestion()
        }
    }

    fun lauchRulette(){
        val intent = Intent(this, SpinRouletteView::class.java)
        startActivityForResult(intent, 1)
    }

}