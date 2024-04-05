package com.stolagh.wolfcatcheggs

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.stolagh.wolfcatcheggs.databinding.ActivitySplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    private var coroutineScope = CoroutineScope(Dispatchers.Main + Job())
    private var coroutine : Job? = null
    private var coroutinetwo : Job? = null
    private var currentRotation = 0f
    private var currentTranslationX = 0f
    private var currentTranslationY = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemUI()
        Glide.with(this).asGif().load(R.drawable.butorange).into(binding.ivButOrange)
        Glide.with(this).asGif().load(R.drawable.butbroun).into(binding.ivButBroun)
        ObjectAnimator.ofFloat(binding.ivButBroun, "translationX", 500f).apply {
            duration = 3500
            start()
        }
        ObjectAnimator.ofFloat(binding.ivButBroun, "translationY", 800f).apply {
            duration = 3500
            start()
        }
        coroutinetwo = coroutineScope.launch {
            delay(1000)
            ObjectAnimator.ofFloat(binding.ivButOrange, "translationX", 1200f).apply {
                duration = 3500
                start()
            }
            ObjectAnimator.ofFloat(binding.ivButOrange, "translationY", -400f).apply {
                duration = 3500
                start()
            }
            coroutinetwo?.cancel()
        }
        coroutine = coroutineScope.launch {
            eggToLeft()
            delay(300)
            eggToLeft()
            delay(300)
            eggToLeft()
            delay(300)
            eggToLeft()
            delay(300)
            eggToLeft()
            delay(300)
            eggToLeft()
            delay(300)
            eggToLeft()
            delay(300)
            eggToLeft()
            delay(300)
            eggToLeft()
            delay(300)
            eggToLeft()
            binding.ivEgg.visibility = View.GONE
            delay(500)
            startActivity(Intent(this@SplashActivity, WolfActivity::class.java))
            coroutine?.cancel()
        }
    }
    @SuppressLint("Recycle")
    private fun eggToLeft(){
        currentRotation = binding.ivEgg.rotation
        currentTranslationX = binding.ivEgg.translationX
        currentTranslationY = binding.ivEgg.translationY
        val translationXAnimator = ObjectAnimator.ofFloat(binding.ivEgg, "translationX", currentTranslationX, (currentTranslationX - 50f))
        translationXAnimator.duration = 200
        translationXAnimator.start()

        val translationYAnimator = ObjectAnimator.ofFloat(binding.ivEgg, "translationY", currentTranslationY, (currentTranslationY + 25f))
        translationYAnimator.duration = 200
        translationYAnimator.start()


        val rotationAnimator = ObjectAnimator.ofFloat(binding.ivEgg, "rotation", currentRotation, (currentRotation - 90f))
        rotationAnimator.duration = 200
        rotationAnimator.start()

        currentRotation = binding.ivEgg.rotation
        currentTranslationX = binding.ivEgg.translationX
        currentTranslationY = binding.ivEgg.translationY
    }
    private fun hideSystemUI () {
        val window : Window? = window
        if (window != null) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
        if (window != null) {
            WindowInsetsControllerCompat (window, window.decorView).let { controller ->
                controller.hide (WindowInsetsCompat.Type.systemBars ())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
}