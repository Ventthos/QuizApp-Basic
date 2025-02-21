package com.ventthos.quizzappbasic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ResultScreen : AppCompatActivity() {

    private var score: Int = 0
    private var difficulty: String = "Normal"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_screen)

        val scoreTextView = findViewById<TextView>(R.id.scoreTextView)
        val resultImageView = findViewById<ImageView>(R.id.resultImageView)
        val resultMessageTextView =
            findViewById<TextView>(R.id.resultMessageTextView)  // Nuevo TextView para el mensaje

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt("SCORE")
            difficulty = savedInstanceState.getString("DIFFICULTY") ?: "Normal"
        }else{
            score = intent.getIntExtra("SCORE", 0)
            difficulty = intent.getStringExtra("DIFFICULTY") ?: "Normal"
        }

        scoreTextView.text = "${getString(R.string.puntuation)} $score"

        // Selección del mensaje según el puntaje y la dificultad
        // HAY QUE CAMBIAR LAS CONDICIONES POR RANGOS COMO EN LAS CONDICIONES PARA gifResource
        val resultMessage = when {
            difficulty == "Fácil" -> {
                if (score >= 1000) R.string.easy_highestResult
                else if (score in 600..999) R.string.easy_result2
                else if (score in 300..599) R.string.easy_result1
                else R.string.easy_lowestResult
            }

            difficulty == "Normal" -> {
                if (score >= 2000) R.string.normal_highestResult
                else if (score in 1200..1999) R.string.normal_result2
                else if (score in 600 .. 1199) R.string.normal_result1
                else R.string.normal_lowestResult
            }

            difficulty == "Difícil" -> {
                if (score >= 3000) R.string.hard_highestResult
                else if (score in 1800..2999) R.string.hard_result1
                else if (score in 900 .. 1799) R.string.hard_result2
                else R.string.hard_lowestResult
            }

            else -> R.string.just_try_again
        }

        resultMessageTextView.text = getString(resultMessage)  // Asignar el mensaje al TextView

        // Selección del GIF según el puntaje y la dificultad
        val gifResource = when {
            difficulty == "Fácil" -> {
                if (score >= 1000) R.drawable.ratherdull
                else if (score in 600..999) R.drawable.gilgalol
                else if (score in 300..599) R.drawable.gilgafail
                else R.drawable.obliteratedsaber
            }

            difficulty == "Normal" -> {
                if (score >= 2000) R.drawable.ratherdull
                else if (score in 1200..1999) R.drawable.gilgalol
                else if (score in 600 .. 1199) R.drawable.gilgafail
                else R.drawable.obliteratedsaber
            }

            difficulty == "Difícil" -> {
                if (score >= 3000) R.drawable.ratherdull
                else if (score in 1800..2999) R.drawable.gilgalol
                else if (score in 900 .. 1799) R.drawable.gilgafail
                else R.drawable.obliteratedsaber
            }

            else -> {
                R.drawable.obliteratedsaber
            }
        }

        // Cargar el GIF con Glide
        Glide.with(this)
            .asGif()
            .load(gifResource)
            .into(resultImageView)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("SCORE", score)
        outState.putString("DIFFICULTY", difficulty)
    }

}


