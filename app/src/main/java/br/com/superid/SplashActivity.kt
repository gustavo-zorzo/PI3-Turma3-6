package br.com.superid

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import br.com.superid.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startProgressBarAnimation()
    }

    private fun startProgressBarAnimation() {
        val totalTime = 3000L // 3 segundos
        val interval = 50L // Atualiza a cada 50ms

        binding.progressBar.max = (totalTime / interval).toInt()

        val timer = object : CountDownTimer(totalTime, interval) {
            var progress = 0

            override fun onTick(millisUntilFinished: Long) {
                progress++
                binding.progressBar.progress = progress
            }

            override fun onFinish() {
                startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
                finish()
            }
        }
        timer.start()
    }
}
