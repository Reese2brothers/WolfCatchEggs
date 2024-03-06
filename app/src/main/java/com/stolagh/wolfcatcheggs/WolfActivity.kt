package com.stolagh.wolfcatcheggs

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.stolagh.wolfcatcheggs.databinding.ActivityWolfBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WolfActivity : AppCompatActivity() {
    private lateinit var binding : ActivityWolfBinding
    private var coroutineScope = CoroutineScope(Dispatchers.Main + Job())
    private var coroutine : Job? = null
    var press = true
    private var currentRotation = 0f
    private var currentTranslationX = 0f
    private var currentTranslationY = 0f
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWolfBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemUI()

        binding.ivPlay.setOnClickListener {
            if(press){
                binding.ivPlay.setImageResource(R.drawable.baseline_stop_24)
                binding.tvPlay.text = "stop"
                coroutine = coroutineScope.launch {
                    eggToLeft()
                    delay(200)
                    eggToLeft()
                    delay(200)
                    eggToLeft()
                    delay(200)
                    eggToLeft()
                    delay(200)
                    eggToLeftDown()
                    delay(200)
                    eggToLeftDown()
                    delay(200)
                    eggToLeftDown()
                    delay(200)
                    eggToLeftDown()
                    delay(200)
                    eggToLeftSmashUp()
                    delay(200)
                    binding.ivEggRightDown.visibility = View.GONE
                    binding.ivSmashEggRight.visibility = View.VISIBLE
                }
                press = false
            } else {
                binding.ivPlay.setImageResource(R.drawable.baseline_play_arrow_24)
                binding.tvPlay.text = " play"
                coroutine?.cancel()
                press = true
                binding.ivEggRightDown.visibility = View.VISIBLE
                binding.ivSmashEggRight.visibility = View.GONE
                binding.ivEggRightDown.translationX = 0f
                binding.ivEggRightDown.translationY = 0f
            }
        }
        binding.btRightDown.setOnClickListener {
            binding.ivWolfRightDown.visibility = View.VISIBLE
            binding.ivWolfRightUp.visibility = View.GONE
            binding.ivWolfLeftDown.visibility = View.GONE
            binding.ivWolfLeftUp.visibility = View.GONE
        }
        binding.btRightUp.setOnClickListener {
            binding.ivWolfRightDown.visibility = View.GONE
            binding.ivWolfRightUp.visibility = View.VISIBLE
            binding.ivWolfLeftDown.visibility = View.GONE
            binding.ivWolfLeftUp.visibility = View.GONE
        }
        binding.btLeftDown.setOnClickListener {
            binding.ivWolfRightDown.visibility = View.GONE
            binding.ivWolfRightUp.visibility = View.GONE
            binding.ivWolfLeftDown.visibility = View.VISIBLE
            binding.ivWolfLeftUp.visibility = View.GONE
        }
        binding.btLeftUp.setOnClickListener {
            binding.ivWolfRightDown.visibility = View.GONE
            binding.ivWolfRightUp.visibility = View.GONE
            binding.ivWolfLeftDown.visibility = View.GONE
            binding.ivWolfLeftUp.visibility = View.VISIBLE
        }
    }
    @SuppressLint("Recycle")
    private fun eggToLeft(){
        currentRotation = binding.ivEggRightDown.rotation
        currentTranslationX = binding.ivEggRightDown.translationX
            val translationXAnimator = ObjectAnimator.ofFloat(binding.ivEggRightDown, "translationX", currentTranslationX, (currentTranslationX - 25f))
            translationXAnimator.duration = 200
            translationXAnimator.start()

            val rotationAnimator = ObjectAnimator.ofFloat(binding.ivEggRightDown, "rotation", currentRotation, (currentRotation - 90f))
            rotationAnimator.duration = 200
            rotationAnimator.start()

            currentRotation = binding.ivEggRightDown.rotation
            currentTranslationX = binding.ivEggRightDown.translationX
    }
    private fun eggToLeftDown(){
        currentRotation = binding.ivEggRightDown.rotation
        currentTranslationX = binding.ivEggRightDown.translationX
        currentTranslationY = binding.ivEggRightDown.translationY
        val translationXAnimator = ObjectAnimator.ofFloat(binding.ivEggRightDown, "translationX", currentTranslationX, (currentTranslationX - 25f))
        translationXAnimator.duration = 200
        translationXAnimator.start()
        val translationYAnimator = ObjectAnimator.ofFloat(binding.ivEggRightDown, "translationY", currentTranslationY, (currentTranslationY + 15f))
        translationYAnimator.duration = 200
        translationYAnimator.start()

        val rotationAnimator = ObjectAnimator.ofFloat(binding.ivEggRightDown, "rotation", currentRotation, (currentRotation - 90f))
        rotationAnimator.duration = 200
        rotationAnimator.start()

        currentRotation = binding.ivEggRightDown.rotation
        currentTranslationX = binding.ivEggRightDown.translationX
        currentTranslationY = binding.ivEggRightDown.translationY
    }
    private fun eggToLeftSmashUp(){
        currentRotation = binding.ivEggRightDown.rotation
        currentTranslationX = binding.ivEggRightDown.translationX
        currentTranslationY = binding.ivEggRightDown.translationY
        val translationXAnimator = ObjectAnimator.ofFloat(binding.ivEggRightDown, "translationX", currentTranslationX, (currentTranslationX - 25f))
        translationXAnimator.duration = 200
        translationXAnimator.start()
        val translationYAnimator = ObjectAnimator.ofFloat(binding.ivEggRightDown, "translationY", currentTranslationY, (currentTranslationY + 100f))
        translationYAnimator.duration = 200
        translationYAnimator.start()

        val rotationAnimator = ObjectAnimator.ofFloat(binding.ivEggRightDown, "rotation", currentRotation, (currentRotation - 90f))
        rotationAnimator.duration = 200
        rotationAnimator.start()

        currentRotation = binding.ivEggRightDown.rotation
        currentTranslationX = binding.ivEggRightDown.translationX
        currentTranslationY = binding.ivEggRightDown.translationY
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
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finishAffinity()
        super.onBackPressed()
    }

}