package com.ventthos.quizzappbasic

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var btnJugar: CardView
    private lateinit var btnOpciones: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        btnJugar = findViewById(R.id.cardPlay)
        btnOpciones = findViewById(R.id.btnOpciones)

        //btnJugar.setOnClickListener {
        //    val intent = Intent(this, GameScreen::class.java)
        //   startActivity(intent)
        //}
        btnJugar.setOnClickListener {
            val intent = Intent(this, SpinRouletteView::class.java)
            startActivity(intent)
        }
    }
}
