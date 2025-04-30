package br.com.superid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.superid.databinding.ActivityTermsBinding

class TermsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa o ViewBinding
        binding = ActivityTermsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Clique no botão "Confirmar leitura"
        binding.btnConfirm.setOnClickListener {
            // Voltar para a tela de boas-vindas (WelcomeActivity)
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish() // Finaliza a TermsActivity para não ficar em stack
        }
    }
}
