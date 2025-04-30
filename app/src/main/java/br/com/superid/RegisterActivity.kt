package br.com.superid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.superid.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    // ViewBinding para acessar os elementos do XML
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa o ViewBinding
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Clique no botão de voltar
        binding.btnBack.setOnClickListener {
            // Volta para a tela de Welcome
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish() // Finaliza a tela atual para não ficar empilhada
        }

        // Clique no botão "Cadastrar" (aqui você ainda pode implementar a lógica de cadastro)
        binding.btnRegister.setOnClickListener {
            // Exemplo futuro: validar campos, criar conta, etc.
        }
    }
}
