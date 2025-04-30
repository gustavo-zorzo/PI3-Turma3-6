package br.com.superid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.superid.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    // ViewBinding para acessar os elementos do layout
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa o ViewBinding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Clique no botão Voltar
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }

        // Clique no botão "Entrar" (login)
        binding.btnLogin.setOnClickListener {
            // Exemplo futuro: validar login
        }

        // Clique no "Esqueci minha senha"
        binding.tvForgotPassword.setOnClickListener {
            // Exemplo futuro: recuperar senha
        }
    }
}
