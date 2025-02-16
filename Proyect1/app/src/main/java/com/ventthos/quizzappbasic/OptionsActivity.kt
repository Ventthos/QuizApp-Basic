package com.ventthos.quizzappbasic

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.android.material.slider.Slider
import com.google.android.material.button.MaterialButton
import androidx.appcompat.app.AppCompatActivity

class OptionsActivity : AppCompatActivity() {

    private lateinit var spinnerDifficulty: Spinner
    private lateinit var sliderHints: Slider
    private lateinit var btnBack: MaterialButton
    private lateinit var sharedPrefs: SharedPreferences

    companion object {
        const val PREFS_NAME = "QuizAppPreferences"
        const val KEY_DIFFICULTY = "difficulty"
        const val KEY_HINTS = "hints"
    }

    // maximo de pistas pa cada uno
    private fun allowedMaxForDifficulty(difficulty: String): Int {
        return when (difficulty.uppercase()) {
            "FÁCIL" -> 3
            "NORMAL" -> 2
            "DIFÍCIL" -> 0
            else -> 3
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        spinnerDifficulty = findViewById(R.id.spinnerDifficulty)
        sliderHints = findViewById(R.id.sliderHints)
        btnBack = findViewById(R.id.btnBack)

        //esto es para que el slider siempre este en 3
        sliderHints.valueFrom = 0f
        sliderHints.valueTo = 3f
        sliderHints.stepSize = 1f

        sharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val difficulties = resources.getStringArray(R.array.difficulty_levels)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, difficulties)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDifficulty.adapter = adapter

        // Recuperamos la dificultad guardada (por defecto "NORMAL")
        val savedDifficulty = sharedPrefs.getString(KEY_DIFFICULTY, "NORMAL") ?: "NORMAL"
        // Seleccionamos la dificultad en el spinner
        val index = difficulties.indexOfFirst { it.equals(savedDifficulty, ignoreCase = true) }
        if (index >= 0) spinnerDifficulty.setSelection(index)

        //las pistas guardadas o usamos el máximo permitido según la dificultad
        val savedHints = sharedPrefs.getInt(KEY_HINTS, allowedMaxForDifficulty(savedDifficulty))
        sliderHints.value = savedHints.toFloat()

        spinnerDifficulty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val selectedDifficulty = spinnerDifficulty.selectedItem.toString()
                val allowedMax = allowedMaxForDifficulty(selectedDifficulty)
                // Si el valor actual del slider supera el máximo permitido para la nueva dificultad, lo ajustamos
                if (sliderHints.value > allowedMax) {
                    sliderHints.value = allowedMax.toFloat()
                    Toast.makeText(
                        applicationContext,
                        "Para la dificultad $selectedDifficulty, el máximo de pistas es $allowedMax",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Listener para el slider: si el usuario intenta manualmente sobrepasar el máximo
        sliderHints.addOnChangeListener { slider, value, fromUser ->
            if (fromUser) {
                val currentDifficulty = spinnerDifficulty.selectedItem.toString()
                val allowedMax = allowedMaxForDifficulty(currentDifficulty)
                if (value > allowedMax) {
                    Toast.makeText(
                        applicationContext,
                        "Para la dificultad $currentDifficulty, el máximo de pistas es $allowedMax",
                        Toast.LENGTH_SHORT
                    ).show()
                    slider.value = allowedMax.toFloat()
                }
            }
        }

        // Guardamos los ajustes y volvemos a la pantalla anterior
        btnBack.setOnClickListener {
            saveSettings()
            finish()
        }
    }

    private fun saveSettings() {
        val selectedDifficulty = spinnerDifficulty.selectedItem.toString()
        val selectedHints = sliderHints.value.toInt()

        with(sharedPrefs.edit()) {
            putString(KEY_DIFFICULTY, selectedDifficulty)
            putInt(KEY_HINTS, selectedHints)
            apply()
        }
    }
}

//val savedDifficulty = sharedPrefs.getString(KEY_DIFFICULTY, "NORMAL")
//val savedHints = sharedPrefs.getInt(KEY_HINTS, allowedMaxForDifficulty(savedDifficulty))
