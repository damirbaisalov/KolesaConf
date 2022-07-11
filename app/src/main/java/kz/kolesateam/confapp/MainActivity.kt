package kz.kolesateam.confapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kz.kolesateam.confapp.hello.presentation.HelloActivity

private const val SPLASH_SCREEN_DELAY = 2000
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(SPLASH_SCREEN_DELAY.toLong())
        startActivity(Intent(this, HelloActivity::class.java))
        finish()
    }
}