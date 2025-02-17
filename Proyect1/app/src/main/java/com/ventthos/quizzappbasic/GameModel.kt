package com.ventthos.quizzappbasic

import android.util.Log
import androidx.lifecycle.ViewModel

class GameModel: ViewModel() {

    private val questions = listOf(
        Question(
            R.string.question1,
            R.array.question1_options,
            3,
            Category.SCIENCE
        ),
        Question(
            R.string.question2,
            R.array.question2_options,
            2,
            Category.SCIENCE
        ),
        Question(
            R.string.question3,
            R.array.question3_options,
            3,
            Category.SCIENCE
        ),
        Question(
            R.string.question4,
            R.array.question4_options,
            2,
            Category.SCIENCE
        ),
        Question(
            R.string.question5,
            R.array.question5_options,
            0,
            Category.SCIENCE
        ),
        Question(
            R.string.question6,
            R.array.question6_options,
            2,
            Category.GEOGRAPHY
        ),
        Question(
            R.string.question7,
            R.array.question7_options,
            1,
            Category.GEOGRAPHY
        ),
        Question(
            R.string.question8,
            R.array.question8_options,
            2,
            Category.GEOGRAPHY
        ),
        Question(
            R.string.question9,
            R.array.question9_options,
            2,
            Category.GEOGRAPHY
        ),
        Question(
            R.string.question10,
            R.array.question10_options,
            3,
            Category.GEOGRAPHY
        ),
        Question(
            R.string.question11,
            R.array.question11_options,
            2,
            Category.HISTORY
        ),
        Question(
            R.string.question12,
            R.array.question12_options,
            2,
            Category.HISTORY
        ),
        Question(
            R.string.question13,
            R.array.question13_options,
            0,
            Category.HISTORY
        ),
        Question(
            R.string.question14,
            R.array.question14_options,
            2,
            Category.HISTORY
        ),
        Question(
            R.string.question15,
            R.array.question15_options,
            1,
            Category.HISTORY
        ),
        Question(
            R.string.question16,
            R.array.question16_options,
            3,
            Category.MOVIES
        ),
        Question(
            R.string.question17,
            R.array.question17_options,
            2,
            Category.MOVIES
        ),
        Question(
            R.string.question18,
            R.array.question18_options,
            0,
            Category.MOVIES
        ),
        Question(
            R.string.question19,
            R.array.question19_options,
            2,
            Category.MOVIES
        ),
        Question(
            R.string.question20,
            R.array.question20_options,
            1,
            Category.MOVIES
        ),
        Question(
            R.string.question21,
            R.array.question21_options,
            1,
            Category.CULTURE
        ),
        Question(
            R.string.question22,
            R.array.question22_options,
            1,
            Category.CULTURE
        ),
        Question(
            R.string.question23,
            R.array.question23_options,
            2,
            Category.CULTURE
        ),
        Question(
            R.string.question24,
            R.array.question24_options,
            1,
            Category.CULTURE
        ),
        Question(
            R.string.question25,
            R.array.question25_options,
            2,
            Category.CULTURE
        )
    )

    private var initialized = false
    private var selectedQuestions = mutableListOf<QuestionForScreen>()
    private var numberOfAnswers = 4
    private var currentQuestionIndex = 0
    private var hintsCuantity = -1
    private var streak = 0

    fun selectQuestions(quantity: Int, category: Category? = null ) {

        var questionsQuantity = quantity
        var answersQuantity = numberOfAnswers
        val listOfSelectedQuestions: MutableList<QuestionForScreen> = mutableListOf()

        if (quantity > questions.size)
            questionsQuantity = questions.size

        if (answersQuantity > 4)
            answersQuantity = 4

        var filteredQuestions = questions
        if(category != null){
            filteredQuestions = filteredQuestions.filter { it.category == category }
        }

        val shuffledQuestions = filteredQuestions.asSequence().shuffled().take(questionsQuantity).toList()

        for (question in shuffledQuestions){
            val indices = (0 until 4).toMutableList()
            indices.removeAt(question.answerIndex)
            val shuffledIndices = indices.shuffled().take(answersQuantity-1).toMutableList()
            shuffledIndices.add(question.answerIndex)
            listOfSelectedQuestions.add(QuestionForScreen(question, shuffledIndices.shuffled()))
        }
        selectedQuestions.addAll(listOfSelectedQuestions)
    }

    val currentQuestion: QuestionForScreen
        get() = selectedQuestions[currentQuestionIndex]

    val CurrentQuestionIndex: Int
        get() = currentQuestionIndex

    var HintsCuantity: Int
        get() = hintsCuantity
        set(value){
             if (value >= 0) hintsCuantity = value
            else throw IllegalArgumentException("Numero invalido de pistas")
        }

    var Initialized: Boolean
        get() = initialized
        set(value){
            initialized = value
        }

    var NumberOfAnswers: Int
        get() = numberOfAnswers
        set(value){
            if (value >= 0) numberOfAnswers = value
            else throw IllegalArgumentException("Numero invalido de pistas")
        }

    fun nextQuestion(){
        currentQuestionIndex = (currentQuestionIndex+1)% selectedQuestions.size
    }

    fun prevQuestion(){
        currentQuestionIndex = (currentQuestionIndex - 1 + selectedQuestions.size)% selectedQuestions.size
    }

    fun addToStreak(isCorrect: Boolean){
        if(!isCorrect){
            streak = 0
            return
        }

        streak += 1
        if(streak >=2){
            hintsCuantity += 1
            streak = 0
        }
    }

    val lastQuestion
        get() = selectedQuestions[selectedQuestions.size-1]

    val currentQuantityOfQuestions
        get() = selectedQuestions.size

}