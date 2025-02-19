package com.ventthos.quizzappbasic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ResultScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_screen)

        val scoreTextView = findViewById<TextView>(R.id.scoreTextView)
        val resultImageView = findViewById<ImageView>(R.id.resultImageView)
        val resultMessageTextView =
            findViewById<TextView>(R.id.resultMessageTextView)  // Nuevo TextView para el mensaje
        val restartButton = findViewById<Button>(R.id.restartButton)

        val score = intent.getIntExtra("SCORE", 0)
        val difficulty = intent.getStringExtra("DIFFICULTY") ?: "Normal"

        scoreTextView.text = "Tu puntuación: $score"

        // Selección del mensaje según el puntaje y la dificultad
        // HAY QUE CAMBIAR LAS CONDICIONES POR RANGOS COMO EN LAS CONDICIONES PARA gifResource
        val resultMessage = when {
            difficulty == "Fácil" -> {
                if (score >= 800) "¡Buen comienzo, sigue así!"
                else "¡Sigue practicando!"
            }

            difficulty == "Normal" -> {
                if (score >= 800) "¡Excelente trabajo!"
                else if (score >= 500) "¡Buen trabajo!"
                else "¡Sigue practicando!"
            }

            difficulty == "Difícil" -> {
                if (score >= 800) "¡Increíble trabajo, dominaste todo!"
                else if (score >= 500) "¡Buen trabajo, pero la dificultad fue alta!"
                else "¡No te rindas, puedes mejorar!"
            }

            else -> "¡Intenta de nuevo, puedes mejorar!"
        }

        resultMessageTextView.text = resultMessage  // Asignar el mensaje al TextView

        // Selección del GIF según el puntaje y la dificultad
        val gifResource = when {
            difficulty == "Fácil" -> {
                if (score == 1000) R.drawable.ratherdull
                else if (score in 600..999) R.drawable.gilgalol
                else if (score in 300..599) R.drawable.gilgafail
                else R.drawable.obliteratedsaber
            }

            difficulty == "Normal" -> {
                if (score == 2000) R.drawable.ratherdull
                else if (score in 1200..1999) R.drawable.gilgalol
                else if (score in 600 .. 1199) R.drawable.gilgafail
                else R.drawable.obliteratedsaber
            }

            difficulty == "Difícil" -> {
                if (score == 3000) R.drawable.ratherdull
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

        restartButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()  // Cierra esta Activity y vuelve al juego
        }
    }
}


