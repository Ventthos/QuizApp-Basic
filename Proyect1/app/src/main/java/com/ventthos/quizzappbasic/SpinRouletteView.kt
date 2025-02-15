package com.ventthos.quizzappbasic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

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
                // Mostrar el mensaje cuando termine la animación
                val intent = Intent(this, GameScreen::class.java)
                intent.putExtra("selectedCategory", winner)  // Pasar el nombre del enum
                startActivity(intent)

                // Habilitar el botón de nuevo después de la animación
                spinButton.isEnabled = true
            }
        }
    }

    // Guardar el estado cuando la actividad es pausada o el sistema lo necesita
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        luckyWheelView.saveState(outState)
    }
}