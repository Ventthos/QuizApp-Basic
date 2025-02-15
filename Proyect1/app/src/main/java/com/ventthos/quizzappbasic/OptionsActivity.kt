package com.ventthos.quizzappbasic

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider

class OptionsActivity : AppCompatActivity() {

    lateinit var spinnerDifficulty: Spinner
    lateinit var sliderHints: Slider
    lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        spinnerDifficulty = findViewById(R.id.spinnerDifficulty)
        sliderHints = findViewById(R.id.sliderHints)
        btnBack = findViewById(R.id.btnBack)

        val prefs = getSharedPreferences("TempSettings", Context.MODE_PRIVATE)

        var savedDifficulty = prefs.getInt("difficulty_level", 1)
        var savedHints = prefs.getInt("hints", 0)

        val difficultyOptions = resources.getStringArray(R.array.difficulty_levels)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, difficultyOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDifficulty.adapter = adapter
        spinnerDifficulty.setSelection(savedDifficulty)

        sliderHints.value = savedHints.toFloat()

        spinnerDifficulty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                prefs.edit().putInt("difficulty_level", position).apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        sliderHints.addOnChangeListener { _, value, _ ->
            prefs.edit().putInt("hints", value.toInt()).apply()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}
