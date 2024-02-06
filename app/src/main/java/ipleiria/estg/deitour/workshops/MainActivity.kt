package ipleiria.estg.deitour.workshops

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickBtnStartGame(view: View) {
        var playerName = findViewById<EditText>(R.id.editTextTextPersonName).text.toString()

        if(playerName.isEmpty()) {
            Toast.makeText(this, "Preenche o teu nome para iniciar o jogo!", Toast.LENGTH_SHORT).show()
        }
        else {
            val intent = Intent(this@MainActivity, GamePlayActivity::class.java)
            intent.putExtra("playerName", playerName)
            startActivity(intent)
        }
    }
}