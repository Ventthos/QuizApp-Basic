package com.ventthos.quizzappbasic

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton

val DIFFICULTYTAG = "DIFFICULTY_KEY"
val HINTSTAG = "HINTSAVAILABLE_KEY"

class MainActivity : AppCompatActivity() {

    private lateinit var btnJugar: CardView
    private lateinit var btnOpciones: MaterialButton

    private var currentDifficulty = "Fácil"
    private var currentNumerOfHints = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        if(savedInstanceState != null){
            currentDifficulty = savedInstanceState.getString(DIFFICULTYTAG, "Fácil")
            currentNumerOfHints = savedInstanceState.getInt(HINTSTAG, currentNumerOfHints)
        }

        btnJugar = findViewById(R.id.cardPlay)
        btnOpciones = findViewById(R.id.btnOpciones)

        btnJugar.setOnClickListener {
            val intent = Intent(this, GameScreen::class.java)
            intent.putExtra(EXTRA_KEY_DIFFICULTY, currentDifficulty)
            intent.putExtra(EXTRA_KEY_HINTS_QUANTITY, currentNumerOfHints)
            startActivity(intent)
        }

        btnOpciones.setOnClickListener {
            val intent = Intent(this, OptionsActivity::class.java)
            intent.putExtra(EXTRA_KEY_DIFFICULTY, currentDifficulty)
            intent.putExtra(EXTRA_KEY_HINTS_QUANTITY, currentNumerOfHints)
            startActivityForResult(intent, 0)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(DIFFICULTYTAG, currentDifficulty)
        outState.putInt(HINTSTAG, currentNumerOfHints)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == RESULT_OK) {
            currentDifficulty = data?.getStringExtra(DIFFICULTYTAG) ?: "Fácil"
            currentNumerOfHints = data?.getIntExtra(HINTSTAG, 3) ?: 3

        }
    }
}
