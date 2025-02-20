package com.ventthos.quizzappbasic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

val QUESTION_CATEGORY_EXTRA = "com.ventthos.quizzappbasic.category"

class SpinRouletteView : AppCompatActivity() {
    private lateinit var luckyWheelView: Roulette
    private lateinit var spinButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activityroulette)

        luckyWheelView = findViewById(R.id.RouletteToSpin)
        spinButton = findViewById(R.id.btnGirar)

        // Restaurar el estado de la ruleta si es necesario
        savedInstanceState?.let {
            luckyWheelView.restoreState(it)
        }

        spinButton.setOnClickListener {
            // Deshabilitar el botón cuando se presiona
            spinButton.isEnabled = false

            luckyWheelView.spinWheel { winner ->

                // Habilitar el botón de nuevo después de la animación
                spinButton.isEnabled = true
                returnWinner(winner)
            }
        }
    }

    // Guardar el estado cuando la actividad es pausada o el sistema lo necesita
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        luckyWheelView.saveState(outState)
    }

    fun returnWinner(winner: Category){

        val resultIntent = Intent().apply {
            putExtra(QUESTION_CATEGORY_EXTRA, winner)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        returnWinner(Category.SCIENCE)
    }
}