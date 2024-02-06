package ipleiria.estg.deitour.workshops

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

class GamePlayActivity : AppCompatActivity() {
    var MIN = 1
    var MAX = 9
    var numGerado = -1
    var numTentativas = 0
    var editTextNumber: EditText ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)

        //Obter o nome do utilizador preenchido na atividade anterior
        val playerName = intent.getStringExtra("playerName")
        editTextNumber = findViewById<EditText>(R.id.editTextNumber)

        Snackbar.make(findViewById(android.R.id.content), "Bem-vindo e bom-jogo, " + playerName + "! Neste nível tens tentativas ilimitadas para descobrir o número mágico gerado pela aplicação", Snackbar.LENGTH_LONG).show()

        //Gerar o número aleatório dentro dos limites definidos
        numGerado = (MIN..MAX).random()
    }

    fun onClickBtnVerificar(view: View) {
        var numUser = editTextNumber?.text.toString().trim()
        var textViewResultado = findViewById<TextView>(R.id.textViewResultado)
        var textViewNumTentativas = findViewById<TextView>(R.id.textViewNumTentativas)
        var btnVerificar = findViewById<Button>(R.id.btnVerificar)

        //Incrementar o número de tentativas efetuadas
        numTentativas++

        //Verificar se o utilizador clicou no botão sem introduzir um número ou se introduziu um número fora dos limites
        if(numUser.isEmpty()) {
            Toast.makeText(this, "Não foi introduzido um número!", Toast.LENGTH_LONG).show()
            return
        }
        if(numUser.toInt() < MIN || numUser.toInt() > MAX) {
            Toast.makeText(this, "Introduza um número entre 1 e 9", Toast.LENGTH_LONG).show()
            return
        }

        //Esconder o teclado
        hideKeyboard()

        //Obter o número introduzido pelo utilizador
        var numUserInt = numUser.toInt()

        //Veriricar se o número está correto
        if(numUserInt == numGerado) {
            textViewResultado.setTextColor(Color.rgb(79,143,0))
            textViewResultado.setText("PARABÉNS, conseguiste acertar no número mágico em " + numTentativas + " tentativas!")

            textViewNumTentativas.setText("Para jogar novamente volta à janela anterior e inicia novo jogo")

            editTextNumber?.isEnabled = false
            findViewById<Button>(R.id.btnVerificar).isEnabled = false
        }
        else {
            //Verificar se o número é superior
            if(numUserInt > numGerado) {
                countdownToast("Ops! O número mágico é mais baixo... Tenta novamente em ", 3000);
            }
            else {
                countdownToast("Ups! O número mágico é mais alto... Tenta novamente em ", 3000);
            }

            //Bloquear o editTextNumber por 3 segundos
            btnVerificar.setEnabled(false)
            editTextNumber?.isEnabled = false
            val handler = Handler()
            handler.postDelayed(Runnable {
                editTextNumber?.setText("")
                editTextNumber?.isEnabled = true
                btnVerificar.setEnabled(true)
            }, 3000)
        }
    }

    fun countdownToast(text: String, time: Int) {
        var time = time
        time += 1000
        val snackbar = Snackbar.make(findViewById(android.R.id.content),text + time, Snackbar.LENGTH_INDEFINITE)
        object : CountDownTimer(time.toLong(), 1000) {
            override fun onTick(m: Long) {
                if (m / 1000 == 0L) {
                    snackbar.dismiss()
                    editTextNumber?.requestFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(editTextNumber, InputMethodManager.SHOW_IMPLICIT)
                } else {
                    snackbar.setText(text + m / 1000)
                    snackbar.show()
                }
            }
            override fun onFinish() {
                snackbar.dismiss()
            }
        }.start()
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }
    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }
    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}