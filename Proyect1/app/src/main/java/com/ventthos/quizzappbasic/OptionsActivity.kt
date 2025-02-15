package com.ventthos.quizzappbasic

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider

class OptionsActivity : AppCompatActivity() {

    private lateinit var spinnerDifficulty: Spinner
    private lateinit var sliderHints: Slider
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        spinnerDifficulty = findViewById(R.id.spinnerDifficulty)
        sliderHints = findViewById(R.id.sliderHints)
        btnBack = findViewById(R.id.btnBack)

        val prefs = getSharedPreferences("TempSettings", Context.MODE_PRIVATE)
        val savedDifficulty = prefs.getInt("difficulty_level", 1)
        val savedHints = prefs.getInt("hints", 0)

        val difficultyOptions = resources.getStringArray(R.array.difficulty_levels)
        val adapter = ArrayAdapter(this, R.layout.custom_spinner_item, difficultyOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDifficulty.adapter = adapter
        spinnerDifficulty.setSelection(savedDifficulty)

        updateSliderLimits(savedDifficulty, savedHints)

        spinnerDifficulty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val newMaxHints = getMaxHints(position)
                val adjustedHints = savedHints.coerceAtMost(newMaxHints)
                prefs.edit().putInt("difficulty_level", position).putInt("hints", adjustedHints).apply()
                updateSliderLimits(position, adjustedHints)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        sliderHints.addOnChangeListener { _, value, _ ->
            val maxHints = getMaxHints(spinnerDifficulty.selectedItemPosition)
            if (value > maxHints) {
                Toast.makeText(this, "No puedes aumentar las pistas en este modo", Toast.LENGTH_SHORT).show()
                sliderHints.value = maxHints.toFloat()
            } else {
                prefs.edit().putInt("hints", value.toInt()).apply()
            }
        }

        btnBack.setOnClickListener { finish() }
    }

    private fun getMaxHints(difficulty: Int): Int {
        return when (difficulty) {
            0 -> 3  // Fácil: Máximo 3 pistas
            1 -> 2  // Normal: Máximo 2 pistas
            else -> 0  // Difícil: 0 pistas permitidas
        }
    }

    private fun updateSliderLimits(difficulty: Int, selectedHints: Int) {
        sliderHints.valueTo = 3f
        sliderHints.value = selectedHints.toFloat()
    }
}
