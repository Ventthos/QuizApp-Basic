package com.ventthos.quizzappbasic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var btnJugar: Button
    lateinit var btnOpciones: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        btnJugar = findViewById(R.id.btnJugar)
        btnOpciones = findViewById(R.id.btnOpciones)

        btnJugar.setOnClickListener {
            val intent = Intent(this, GameScreen::class.java)
            startActivity(intent)
        }

    }
}
