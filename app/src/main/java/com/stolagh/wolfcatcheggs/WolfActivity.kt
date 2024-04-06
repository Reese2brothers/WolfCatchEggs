package com.stolagh.wolfcatcheggs

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Rect
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.stolagh.wolfcatcheggs.databinding.ActivityWolfBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class WolfActivity : AppCompatActivity() {
    private lateinit var binding : ActivityWolfBinding
    private var coroutineScope = CoroutineScope(Dispatchers.Main + Job())
    private var coroutine : Job? = null
    private var coroutine2 : Job? = null
    private var coroutineButterfly : Job? = null
    private var corButOne : Job? = null
    private var corButTwo : Job? = null
    private var corButThree : Job? = null
    private var corButFour : Job? = null
    private var corLUB : Job? = null
    private var corLDB : Job? = null
    private var corRUB : Job? = null
    private var corRDB : Job? = null
    private var corListMusic : Job? = null
    private var coroutineRightDownFall : Job? = null
    private var coroutineRightDownFallTwo : Job? = null
    private var coroutineRightUpFall : Job? = null
    private var coroutineRightUpFallTwo : Job? = null
    private var coroutineLeftUpFall : Job? = null
    private var coroutineLeftUpFallTwo : Job? = null
    private var coroutineLeftDownFall : Job? = null
    private var coroutineLeftDownFallTwo: Job? = null
    private var press = true
    private var press2 = true
    private var pressListTracks = false
    private var pressMusic = false
    private var pressSpeed = false
    private var currentRotation = 0f
    private var currentTranslationX = 0f
    private var currentTranslationY = 0f
    private var count = 0
    private var bestA = 0
    private var bestB = 0
    private var speed = 0
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var musicPlay: MediaPlayer
    private var lastFunction: (() -> Unit)? = null
    private var newFunction: (() -> Unit)? = null
    private var lastFunction2: (() -> Unit)? = null
    private var newFunction2: (() -> Unit)? = null
    private val functions = arrayOf(
        { coroutineRightDownFall() }, { coroutineLeftUpFall() },
        { coroutineRightUpFall() } ,  { coroutineLeftDownFall() }
    )
    private val functions2 = arrayOf(
        { coroutineRightDownFallTwo() }, { coroutineLeftUpFallTwo() },
        { coroutineRightUpFallTwo() } ,  { coroutineLeftDownFallTwo() }
    )
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWolfBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemUI()
        butterflies()
        seekbar()
        Glide.with(this).asGif().load(R.drawable.butbroun).into(binding.ivButterflyOne)
        Glide.with(this).asGif().load(R.drawable.butgreen).into(binding.ivButterflyTwo)
        Glide.with(this).asGif().load(R.drawable.butrose).into(binding.ivButterflyThree)
        Glide.with(this).asGif().load(R.drawable.butwhite).into(binding.ivButterflyFour)

        binding.cvListMusic.isEnabled = false
        binding.cvListMusic.alpha = 0.8f
        sharedPrefs = getSharedPreferences("SaveBestCount", MODE_PRIVATE)
        bestA = sharedPrefs.getInt("savebestA", 0)
        bestB = sharedPrefs.getInt("savebestB", 0)
        if(bestA != 0){
            binding.tvBestRecordA.text = "Game A : $bestA"
        } else {
            binding.tvBestRecordA.text = "Game A : $bestA"
        }
        if(bestB != 0){
            binding.tvBestRecordB.text = "Game B : $bestB"
        } else {
            binding.tvBestRecordB.text = "Game B : $bestB"
        }
        binding.tvSpeedMinus.setOnClickListener {
            binding.seekBar.progress = binding.seekBar.progress - 1
            binding.tvSpeedSeekLabel.text = "${binding.seekBar.progress}"
        }
        binding.tvSpeedPlus.setOnClickListener {
            binding.seekBar.progress = binding.seekBar.progress + 1
            binding.tvSpeedSeekLabel.text = "${binding.seekBar.progress}"
        }
        binding.tvSpeedOk.setOnClickListener {
            binding.cvSpeed.setCardBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.yellowlight))
            binding.cvSeekSpeed.visibility = View.GONE
            seekspeed()
            showSpeedBack()
            pressSpeed = false
        }
        binding.cvSpeed.setOnClickListener {
            if(!pressSpeed){
                binding.cvSpeed.setCardBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.white))
                binding.cvSeekSpeed.visibility = View.VISIBLE
                showSpeed()
                pressSpeed = true
            } else {
                binding.cvSpeed.setCardBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.yellowlight))
                binding.cvSeekSpeed.visibility = View.GONE
                showSpeedBack()
                pressSpeed = false
            }
        }
        binding.cvMusic.setOnClickListener {
                if (!pressMusic) {
                    binding.cvMusic.setCardBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.white))
                    binding.ivMusicOn.visibility = View.VISIBLE
                    binding.ivMusicOff.visibility = View.GONE
                    binding.cvListMusic.isEnabled = true
                    binding.cvListMusic.alpha = 1f
                    binding.cvTracks.visibility = View.VISIBLE
                    showTable()
                    pressListTracks = true
                    playMusic(::track1)
                    binding.tvTrack1.setBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.white))
                    binding.tvTrack2.background = null
                    binding.tvTrack3.background = null
                    binding.tvTrack4.background = null
                    binding.tvTrack5.background = null
                    binding.tvTrack6.background = null
                    binding.tvTrack7.background = null
                    pressMusic = true
                }
                else {
                    binding.cvMusic.setCardBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.yellowlight))
                    binding.ivMusicOn.visibility = View.GONE
                    binding.ivMusicOff.visibility = View.VISIBLE
                    if(binding.cvTracks.visibility == View.VISIBLE) {
                        binding.cvTracks.visibility = View.GONE
                        showTableBack()
                    }
                    stopMusic()
                    binding.cvListMusic.isEnabled = false
                    binding.cvListMusic.alpha = 0.8f
                    pressMusic = false
                }
        }
        binding.cvListMusic.setOnClickListener {
            corListMusic = coroutineScope.launch {
                binding.cvListMusic.setCardBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.white))
                delay(150)
                binding.cvListMusic.setCardBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.yellowlight))
                corListMusic?.cancel()
            }
            if (pressListTracks == false) {
                binding.cvTracks.visibility = View.VISIBLE
                showTable()
                pressListTracks = true
            }
            else {
                binding.cvTracks.visibility = View.GONE
                showTableBack()
                pressListTracks = false
            }
        }
        binding.btGameA.setOnClickListener {
            binding.ivPlayA.visibility = View.VISIBLE
            binding.ivPlayA.setImageResource(R.drawable.baseline_play_arrow_24)
            binding.ivPlayB.visibility = View.GONE
            binding.tvPlay.visibility = View.VISIBLE
            binding.tvPlay.text = "PLAY"
            binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
            binding.btGameA.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.btGameB.setBackgroundColor(ContextCompat.getColor(this, R.color.yellowlight))
            binding.ivEggRightDown.visibility = View.GONE
            binding.ivEggRightUp.visibility = View.GONE
            binding.ivEggRightDownTwo.visibility = View.GONE
            binding.ivEggRightUpTwo.visibility = View.GONE
            binding.ivEggLeftDown.visibility = View.GONE
            binding.ivEggLeftUp.visibility = View.GONE
            binding.ivEggLeftDownTwo.visibility = View.GONE
            binding.ivEggLeftUpTwo.visibility = View.GONE
            count = 0
            if(binding.tvSpeedSeekLabel.text == "1"){
                speed = 300
            }
            if(binding.tvSpeedSeekLabel.text == "2"){
                speed = 290
            }
            if(binding.tvSpeedSeekLabel.text == "3"){
                speed = 280
            }
            if(binding.tvSpeedSeekLabel.text == "4"){
                speed = 270
            }
            if(binding.tvSpeedSeekLabel.text == "5"){
                speed = 260
            }
            if(binding.tvSpeedSeekLabel.text == "6"){
                speed = 250
            }
            if(binding.tvSpeedSeekLabel.text == "7"){
                speed = 240
            }
            if(binding.tvSpeedSeekLabel.text == "8"){
                speed = 230
            }
            if(binding.tvSpeedSeekLabel.text == "9"){
                speed = 220
            }
            if(binding.tvSpeedSeekLabel.text == "10"){
                speed = 210
            }
            if(binding.tvSpeedSeekLabel.text == "11"){
                speed = 200
            }
            if(binding.tvSpeedSeekLabel.text == "12"){
                speed = 190
            }
            if(binding.tvSpeedSeekLabel.text == "13"){
                speed = 180
            }
            if(binding.tvSpeedSeekLabel.text == "14"){
                speed = 170
            }
            if(binding.tvSpeedSeekLabel.text == "15"){
                speed = 160
            }
            if(binding.tvSpeedSeekLabel.text == "16"){
                speed = 150
            }
            if(binding.tvSpeedSeekLabel.text == "17"){
                speed = 140
            }
            if(binding.tvSpeedSeekLabel.text == "18"){
                speed = 130
            }
            if(binding.tvSpeedSeekLabel.text == "19"){
                speed = 120
            }
            if(binding.tvSpeedSeekLabel.text == "20"){
                speed = 110
            }
            press = true
            press2 = true
            coroutineRightUpFall?.cancel()
            coroutineRightDownFall?.cancel()
            coroutineLeftUpFall?.cancel()
            coroutineLeftDownFall?.cancel()
            coroutineRightUpFallTwo?.cancel()
            coroutineRightDownFallTwo?.cancel()
            coroutineLeftUpFallTwo?.cancel()
            coroutineLeftDownFallTwo?.cancel()
            coroutine?.cancel()
            coroutine2?.cancel()
        }
        binding.btGameB.setOnClickListener {
            binding.ivPlayB.visibility = View.VISIBLE
            binding.ivPlayB.setImageResource(R.drawable.baseline_play_arrow_24)
            binding.ivPlayA.visibility = View.GONE
            binding.tvPlay.visibility = View.VISIBLE
            binding.tvPlay.text = "PLAY"
            binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
            binding.btGameB.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.btGameA.setBackgroundColor(ContextCompat.getColor(this, R.color.yellowlight))
            binding.ivEggRightDown.visibility = View.GONE
            binding.ivEggRightUp.visibility = View.GONE
            binding.ivEggRightDownTwo.visibility = View.GONE
            binding.ivEggRightUpTwo.visibility = View.GONE
            binding.ivEggLeftDown.visibility = View.GONE
            binding.ivEggLeftUp.visibility = View.GONE
            binding.ivEggLeftDownTwo.visibility = View.GONE
            binding.ivEggLeftUpTwo.visibility = View.GONE
            count = 0
            if(binding.tvSpeedSeekLabel.text == "1"){
                speed = 300
            }
            if(binding.tvSpeedSeekLabel.text == "2"){
                speed = 290
            }
            if(binding.tvSpeedSeekLabel.text == "3"){
                speed = 280
            }
            if(binding.tvSpeedSeekLabel.text == "4"){
                speed = 270
            }
            if(binding.tvSpeedSeekLabel.text == "5"){
                speed = 260
            }
            if(binding.tvSpeedSeekLabel.text == "6"){
                speed = 250
            }
            if(binding.tvSpeedSeekLabel.text == "7"){
                speed = 240
            }
            if(binding.tvSpeedSeekLabel.text == "8"){
                speed = 230
            }
            if(binding.tvSpeedSeekLabel.text == "9"){
                speed = 220
            }
            if(binding.tvSpeedSeekLabel.text == "10"){
                speed = 210
            }
            if(binding.tvSpeedSeekLabel.text == "11"){
                speed = 200
            }
            if(binding.tvSpeedSeekLabel.text == "12"){
                speed = 190
            }
            if(binding.tvSpeedSeekLabel.text == "13"){
                speed = 180
            }
            if(binding.tvSpeedSeekLabel.text == "14"){
                speed = 170
            }
            if(binding.tvSpeedSeekLabel.text == "15"){
                speed = 160
            }
            if(binding.tvSpeedSeekLabel.text == "16"){
                speed = 150
            }
            if(binding.tvSpeedSeekLabel.text == "17"){
                speed = 140
            }
            if(binding.tvSpeedSeekLabel.text == "18"){
                speed = 130
            }
            if(binding.tvSpeedSeekLabel.text == "19"){
                speed = 120
            }
            if(binding.tvSpeedSeekLabel.text == "20"){
                speed = 110
            }
            press = true
            press2 = true
            coroutineRightUpFall?.cancel()
            coroutineRightDownFall?.cancel()
            coroutineLeftUpFall?.cancel()
            coroutineLeftDownFall?.cancel()
            coroutineRightUpFallTwo?.cancel()
            coroutineRightDownFallTwo?.cancel()
            coroutineLeftUpFallTwo?.cancel()
            coroutineLeftDownFallTwo?.cancel()
            coroutine?.cancel()
            coroutine2?.cancel()
        }
        binding.ivPlayA.setOnClickListener {
            if (press) {
                coroutine?.cancel()
                coroutineRightDownFall?.cancel()
                coroutineRightUpFall?.cancel()
                coroutineLeftDownFall?.cancel()
                coroutineLeftUpFall?.cancel()
                binding.ivEggRightDown.visibility = View.GONE
                binding.ivEggRightUp.visibility = View.GONE
                binding.ivEggRightDownTwo.visibility = View.GONE
                binding.ivEggRightUpTwo.visibility = View.GONE
                binding.ivEggLeftDown.visibility = View.GONE
                binding.ivEggLeftUp.visibility = View.GONE
                binding.ivEggLeftDownTwo.visibility = View.GONE
                binding.ivEggLeftUpTwo.visibility = View.GONE
                binding.ivEggRightDown.translationX = 0f
                binding.ivEggRightDown.translationY = 0f
                binding.ivEggRightUp.translationX = 0f
                binding.ivEggRightUp.translationY = 0f
                binding.ivEggLeftDown.translationX = 0f
                binding.ivEggLeftDown.translationY = 0f
                binding.ivEggLeftUp.translationX = 0f
                binding.ivEggLeftUp.translationY = 0f
                count = 0
                if(binding.tvSpeedSeekLabel.text == "1"){
                    speed = 300
                }
                if(binding.tvSpeedSeekLabel.text == "2"){
                    speed = 290
                }
                if(binding.tvSpeedSeekLabel.text == "3"){
                    speed = 280
                }
                if(binding.tvSpeedSeekLabel.text == "4"){
                    speed = 270
                }
                if(binding.tvSpeedSeekLabel.text == "5"){
                    speed = 260
                }
                if(binding.tvSpeedSeekLabel.text == "6"){
                    speed = 250
                }
                if(binding.tvSpeedSeekLabel.text == "7"){
                    speed = 240
                }
                if(binding.tvSpeedSeekLabel.text == "8"){
                    speed = 230
                }
                if(binding.tvSpeedSeekLabel.text == "9"){
                    speed = 220
                }
                if(binding.tvSpeedSeekLabel.text == "10"){
                    speed = 210
                }
                if(binding.tvSpeedSeekLabel.text == "11"){
                    speed = 200
                }
                if(binding.tvSpeedSeekLabel.text == "12"){
                    speed = 190
                }
                if(binding.tvSpeedSeekLabel.text == "13"){
                    speed = 180
                }
                if(binding.tvSpeedSeekLabel.text == "14"){
                    speed = 170
                }
                if(binding.tvSpeedSeekLabel.text == "15"){
                    speed = 160
                }
                if(binding.tvSpeedSeekLabel.text == "16"){
                    speed = 150
                }
                if(binding.tvSpeedSeekLabel.text == "17"){
                    speed = 140
                }
                if(binding.tvSpeedSeekLabel.text == "18"){
                    speed = 130
                }
                if(binding.tvSpeedSeekLabel.text == "19"){
                    speed = 120
                }
                if(binding.tvSpeedSeekLabel.text == "20"){
                    speed = 110
                }
                binding.tvCount.text = count.toString()
                binding.ivPlayA.setImageResource(R.drawable.baseline_stop_24)
                binding.tvPlay.text = "stop"
                binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
                press = false
                press2 = true
                coroutine = coroutineScope.launch {
                    while (true) {
                        if(speed == 300){
                            launchRandomFunction()
                            delay((1000..2000).random().toLong())
                            launchRandomFunction()
                            delay(3600)
                        }
                        if(speed == 290){
                           launchRandomFunction()
                            delay((1000..2000).random().toLong())
                            launchRandomFunction()
                            delay(3500)
                        }
                        if(speed == 280){
                           launchRandomFunction()
                            delay((1000..2000).random().toLong())
                            launchRandomFunction()
                            delay(3400)
                        }
                        if(speed == 270){
                           launchRandomFunction()
                            delay((1000..2000).random().toLong())
                            launchRandomFunction()
                            delay(3300)
                        }
                        if(speed == 260){
                           launchRandomFunction()
                            delay((1000..2000).random().toLong())
                            launchRandomFunction()
                            delay(3200)
                        }
                        if(speed == 250){
                           launchRandomFunction()
                            delay((1000..2000).random().toLong())
                            launchRandomFunction()
                            delay(3100)
                        }
                        if(speed == 240){
                           launchRandomFunction()
                            delay((1000..2000).random().toLong())
                            launchRandomFunction()
                            delay(3000)
                        }
                        if(speed == 230){
                           launchRandomFunction()
                            delay((1000..1900).random().toLong())
                            launchRandomFunction()
                            delay(2900)
                        }
                        if(speed == 220){
                            launchRandomFunction()
                            delay((1000..1800).random().toLong())
                            launchRandomFunction()
                            delay(2800)
                        }
                        if(speed == 210){
                            launchRandomFunction()
                            delay((1000..1800).random().toLong())
                            launchRandomFunction()
                            delay(2700)
                        }
                        if(speed == 200){
                            launchRandomFunction()
                            delay((1000..1800).random().toLong())
                            launchRandomFunction()
                            delay(2600)
                        }
                        if(speed == 190){
                            launchRandomFunction()
                            delay((1000..1800).random().toLong())
                            launchRandomFunction()
                            delay(2500)
                        }
                        if(speed == 180){
                            launchRandomFunction()
                            delay((1000..1700).random().toLong())
                            launchRandomFunction()
                            delay(2400)
                        }
                        if(speed == 170){
                            launchRandomFunction()
                            delay((1000..1700).random().toLong())
                            launchRandomFunction()
                            delay(2300)
                        }
                        if(speed == 160){
                            launchRandomFunction()
                            delay((1000..1600).random().toLong())
                            launchRandomFunction()
                            delay(2200)
                        }
                        if(speed == 150){
                            launchRandomFunction()
                            delay((1000..1500).random().toLong())
                            launchRandomFunction()
                            delay(2100)
                        }
                        if(speed == 140){
                            launchRandomFunction()
                            delay((1000..1400).random().toLong())
                            launchRandomFunction()
                            delay(2000)
                        }
                        if(speed == 130){
                            launchRandomFunction()
                            delay((1000..1300).random().toLong())
                            launchRandomFunction()
                            delay(1900)
                        }
                        if(speed == 120){
                            launchRandomFunction()
                            delay((1000..1300).random().toLong())
                            launchRandomFunction()
                            delay(1800)
                        }
                        if(speed == 110){
                            launchRandomFunction()
                            delay((1000..1300).random().toLong())
                            launchRandomFunction()
                            delay(1700)
                        }
                    }
                }
            } else {
                if(count > bestA){
                    bestA = count
                    binding.tvBestRecordA.text = "Game A : $bestA"
                }
                sharedPrefs = getSharedPreferences("SaveBestCount", MODE_PRIVATE)
                val editor = sharedPrefs.edit()
                editor?.putInt("savebestA", bestA)
                editor?.apply()
                count = 0
                binding.tvCount.text = count.toString()
                binding.ivPlayA.setImageResource(R.drawable.baseline_play_arrow_24)
                binding.tvPlay.text = " play"
                press = true
                binding.ivSmashEggRight.visibility = View.GONE
                binding.ivSmashEggLeft.visibility = View.GONE
                binding.ivEggRightDown.translationX = 0f
                binding.ivEggRightDown.translationY = 0f
                binding.ivEggRightUp.translationX = 0f
                binding.ivEggRightUp.translationY = 0f
                binding.ivEggLeftDown.translationX = 0f
                binding.ivEggLeftDown.translationY = 0f
                binding.ivEggLeftUp.translationX = 0f
                binding.ivEggLeftUp.translationY = 0f
                coroutine?.cancel()
                coroutineRightDownFall?.cancel()
                coroutineRightUpFall?.cancel()
                coroutineLeftDownFall?.cancel()
                coroutineLeftUpFall?.cancel()
            }
        }
        binding.ivPlayB.setOnClickListener {
            if (press2) {
                coroutine?.cancel()
                coroutine2?.cancel()
                coroutineRightDownFall?.cancel()
                coroutineRightUpFall?.cancel()
                coroutineLeftDownFall?.cancel()
                coroutineLeftUpFall?.cancel()
                coroutineRightDownFallTwo?.cancel()
                coroutineRightUpFallTwo?.cancel()
                coroutineLeftDownFallTwo?.cancel()
                coroutineLeftUpFallTwo?.cancel()
                binding.ivEggRightDown.visibility = View.GONE
                binding.ivEggRightUp.visibility = View.GONE
                binding.ivEggRightDownTwo.visibility = View.GONE
                binding.ivEggRightUpTwo.visibility = View.GONE
                binding.ivEggLeftDown.visibility = View.GONE
                binding.ivEggLeftUp.visibility = View.GONE
                binding.ivEggLeftDownTwo.visibility = View.GONE
                binding.ivEggLeftUpTwo.visibility = View.GONE
                binding.ivEggRightDown.translationX = 0f
                binding.ivEggRightDown.translationY = 0f
                binding.ivEggRightUp.translationX = 0f
                binding.ivEggRightUp.translationY = 0f
                binding.ivEggLeftDown.translationX = 0f
                binding.ivEggLeftDown.translationY = 0f
                binding.ivEggLeftUp.translationX = 0f
                binding.ivEggLeftUp.translationY = 0f
                binding.ivEggRightDownTwo.translationX = 0f
                binding.ivEggRightDownTwo.translationY = 0f
                binding.ivEggRightUpTwo.translationX = 0f
                binding.ivEggRightUpTwo.translationY = 0f
                binding.ivEggLeftDownTwo.translationX = 0f
                binding.ivEggLeftDownTwo.translationY = 0f
                binding.ivEggLeftUpTwo.translationX = 0f
                binding.ivEggLeftUpTwo.translationY = 0f
                count = 0
                if(binding.tvSpeedSeekLabel.text == "1"){
                    speed = 300
                }
                if(binding.tvSpeedSeekLabel.text == "2"){
                    speed = 290
                }
                if(binding.tvSpeedSeekLabel.text == "3"){
                    speed = 280
                }
                if(binding.tvSpeedSeekLabel.text == "4"){
                    speed = 270
                }
                if(binding.tvSpeedSeekLabel.text == "5"){
                    speed = 260
                }
                if(binding.tvSpeedSeekLabel.text == "6"){
                    speed = 250
                }
                if(binding.tvSpeedSeekLabel.text == "7"){
                    speed = 240
                }
                if(binding.tvSpeedSeekLabel.text == "8"){
                    speed = 230
                }
                if(binding.tvSpeedSeekLabel.text == "9"){
                    speed = 220
                }
                if(binding.tvSpeedSeekLabel.text == "10"){
                    speed = 210
                }
                if(binding.tvSpeedSeekLabel.text == "11"){
                    speed = 200
                }
                if(binding.tvSpeedSeekLabel.text == "12"){
                    speed = 190
                }
                if(binding.tvSpeedSeekLabel.text == "13"){
                    speed = 180
                }
                if(binding.tvSpeedSeekLabel.text == "14"){
                    speed = 170
                }
                if(binding.tvSpeedSeekLabel.text == "15"){
                    speed = 160
                }
                if(binding.tvSpeedSeekLabel.text == "16"){
                    speed = 150
                }
                if(binding.tvSpeedSeekLabel.text == "17"){
                    speed = 140
                }
                if(binding.tvSpeedSeekLabel.text == "18"){
                    speed = 130
                }
                if(binding.tvSpeedSeekLabel.text == "19"){
                    speed = 120
                }
                if(binding.tvSpeedSeekLabel.text == "20"){
                    speed = 110
                }
                binding.tvCount.text = count.toString()
                binding.ivPlayB.setImageResource(R.drawable.baseline_stop_24)
                binding.tvPlay.text = "stop"
                binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
                press2 = false
                coroutine2 = coroutineScope.launch {
                    while (true) {
                        if(speed == 300){
                            launchRandomFunction()
                            delay((700..1500).random().toLong())
                            launchRandomFunctionTwo()
                            delay(1200)
                            launchRandomFunction()
                            delay((500..900).random().toLong())
                            launchRandomFunctionTwo()
                            delay(3600)
                        }
                        if(speed == 290){
                            launchRandomFunction()
                            delay((700..1450).random().toLong())
                            launchRandomFunctionTwo()
                            delay(1150)
                            launchRandomFunction()
                            delay((500..900).random().toLong())
                            launchRandomFunctionTwo()
                            delay(3500)
                        }
                        if(speed == 280){
                            launchRandomFunction()
                            delay((700..1400).random().toLong())
                            launchRandomFunctionTwo()
                            delay(1100)
                            launchRandomFunction()
                            delay((500..900).random().toLong())
                            launchRandomFunctionTwo()
                            delay(3400)
                        }
                        if(speed == 270){
                            launchRandomFunction()
                            delay((700..1350).random().toLong())
                            launchRandomFunctionTwo()
                            delay(1050)
                            launchRandomFunction()
                            delay((500..900).random().toLong())
                            launchRandomFunctionTwo()
                            delay(3300)
                        }
                        if(speed == 260){
                            launchRandomFunction()
                            delay((700..1300).random().toLong())
                            launchRandomFunctionTwo()
                            delay(1000)
                            launchRandomFunction()
                            delay((500..900).random().toLong())
                            launchRandomFunctionTwo()
                            delay(3200)
                        }
                        if(speed == 250){
                            launchRandomFunction()
                            delay((700..1250).random().toLong())
                            launchRandomFunctionTwo()
                            delay(950)
                            launchRandomFunction()
                            delay((500..900).random().toLong())
                            launchRandomFunctionTwo()
                            delay(3100)
                        }
                        if(speed == 240){
                            launchRandomFunction()
                            delay((700..1200).random().toLong())
                            launchRandomFunctionTwo()
                            delay(900)
                            launchRandomFunction()
                            delay((500..900).random().toLong())
                            launchRandomFunctionTwo()
                            delay(3000)
                        }
                        if(speed == 230){
                            launchRandomFunction()
                            delay((700..1150).random().toLong())
                            launchRandomFunctionTwo()
                            delay(850)
                            launchRandomFunction()
                            delay((500..900).random().toLong())
                            launchRandomFunctionTwo()
                            delay(2900)
                        }
                        if(speed == 220){
                            launchRandomFunction()
                            delay((700..1100).random().toLong())
                            launchRandomFunctionTwo()
                            delay(800)
                            launchRandomFunction()
                            delay((500..900).random().toLong())
                            launchRandomFunctionTwo()
                            delay(2800)
                        }
                        if(speed == 210){
                            launchRandomFunction()
                            delay((700..1050).random().toLong())
                            launchRandomFunctionTwo()
                            delay(800)
                            launchRandomFunction()
                            delay((500..850).random().toLong())
                            launchRandomFunctionTwo()
                            delay(2700)
                        }
                        if(speed == 200){
                            launchRandomFunction()
                            delay((700..1000).random().toLong())
                            launchRandomFunctionTwo()
                            delay(800)
                            launchRandomFunction()
                            delay((500..800).random().toLong())
                            launchRandomFunctionTwo()
                            delay(2600)
                        }
                        if(speed == 190){
                            launchRandomFunction()
                            delay((700..950).random().toLong())
                            launchRandomFunctionTwo()
                            delay(750)
                            launchRandomFunction()
                            delay((500..800).random().toLong())
                            launchRandomFunctionTwo()
                            delay(2500)
                        }
                        if(speed == 180){
                            launchRandomFunction()
                            delay((700..900).random().toLong())
                            launchRandomFunctionTwo()
                            delay(700)
                            launchRandomFunction()
                            delay((500..800).random().toLong())
                            launchRandomFunctionTwo()
                            delay(2400)
                        }
                        if(speed == 170){
                            launchRandomFunction()
                            delay((700..850).random().toLong())
                            launchRandomFunctionTwo()
                            delay(700)
                            launchRandomFunction()
                            delay((500..750).random().toLong())
                            launchRandomFunctionTwo()
                            delay(2300)
                        }
                        if(speed == 160){
                            launchRandomFunction()
                            delay((700..800).random().toLong())
                            launchRandomFunctionTwo()
                            delay(700)
                            launchRandomFunction()
                            delay((500..700).random().toLong())
                            launchRandomFunctionTwo()
                            delay(2200)
                        }
                        if(speed == 150){
                            launchRandomFunction()
                            delay((700..750).random().toLong())
                            launchRandomFunctionTwo()
                            delay(650)
                            launchRandomFunction()
                            delay((500..700).random().toLong())
                            launchRandomFunctionTwo()
                            delay(2100)
                        }
                        if(speed == 140){
                            launchRandomFunction()
                            delay((500..650).random().toLong())
                            launchRandomFunctionTwo()
                            delay(700)
                            launchRandomFunction()
                            delay((500..650).random().toLong())
                            launchRandomFunctionTwo()
                            delay(2000)
                        }
                        if(speed == 130){
                            launchRandomFunction()
                            delay((500..600).random().toLong())
                            launchRandomFunctionTwo()
                            delay(700)
                            launchRandomFunction()
                            delay((500..600).random().toLong())
                            launchRandomFunctionTwo()
                            delay(1900)
                        }
                        if(speed == 120){
                            launchRandomFunction()
                            delay((500..600).random().toLong())
                            launchRandomFunctionTwo()
                            delay(600)
                            launchRandomFunction()
                            delay((500..600).random().toLong())
                            launchRandomFunctionTwo()
                            delay(1800)
                        }
                        if(speed == 110){
                            launchRandomFunction()
                            delay((500..550).random().toLong())
                            launchRandomFunctionTwo()
                            delay(600)
                            launchRandomFunction()
                            delay((500..550).random().toLong())
                            launchRandomFunctionTwo()
                            delay(1700)
                        }
                    }
                }
            } else {
                if(count > bestB){
                    bestB = count
                    binding.tvBestRecordB.text = "Game B : $bestB"
                }
                sharedPrefs = getSharedPreferences("SaveBestCount", MODE_PRIVATE)
                val editor = sharedPrefs.edit()
                editor?.putInt("savebestB", bestB)
                editor?.apply()
                count = 0
                binding.tvCount.text = count.toString()
                binding.ivPlayB.setImageResource(R.drawable.baseline_play_arrow_24)
                binding.tvPlay.text = " play"
                press2 = true
                binding.ivSmashEggRight.visibility = View.GONE
                binding.ivSmashEggLeft.visibility = View.GONE
                binding.ivEggRightDown.translationX = 0f
                binding.ivEggRightDown.translationY = 0f
                binding.ivEggRightUp.translationX = 0f
                binding.ivEggRightUp.translationY = 0f
                binding.ivEggLeftDown.translationX = 0f
                binding.ivEggLeftDown.translationY = 0f
                binding.ivEggLeftUp.translationX = 0f
                binding.ivEggLeftUp.translationY = 0f
                binding.ivEggRightDownTwo.translationX = 0f
                binding.ivEggRightDownTwo.translationY = 0f
                binding.ivEggRightUpTwo.translationX = 0f
                binding.ivEggRightUpTwo.translationY = 0f
                binding.ivEggLeftDownTwo.translationX = 0f
                binding.ivEggLeftDownTwo.translationY = 0f
                binding.ivEggLeftUpTwo.translationX = 0f
                binding.ivEggLeftUpTwo.translationY = 0f
                coroutine?.cancel()
                coroutine2?.cancel()
                coroutineRightDownFall?.cancel()
                coroutineRightUpFall?.cancel()
                coroutineLeftDownFall?.cancel()
                coroutineLeftUpFall?.cancel()
                coroutineRightDownFallTwo?.cancel()
                coroutineRightUpFallTwo?.cancel()
                coroutineLeftDownFallTwo?.cancel()
                coroutineLeftUpFallTwo?.cancel()
            }
        }
        binding.btRightDown.setOnClickListener {
            binding.ivWolfRightDown.visibility = View.VISIBLE
            binding.ivWolfRightUp.visibility = View.GONE
            binding.ivWolfLeftDown.visibility = View.GONE
            binding.ivWolfLeftUp.visibility = View.GONE
            corRDB = coroutineScope.launch {
                binding.cvRightDownButton.setCardBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.white))
                delay(150)
                binding.cvRightDownButton.setCardBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.yellowlight))
                corRDB?.cancel()
            }
        }
        binding.btRightUp.setOnClickListener {
            binding.ivWolfRightDown.visibility = View.GONE
            binding.ivWolfRightUp.visibility = View.VISIBLE
            binding.ivWolfLeftDown.visibility = View.GONE
            binding.ivWolfLeftUp.visibility = View.GONE
            corRUB = coroutineScope.launch {
                binding.cvRightUpButton.setCardBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.white))
                delay(150)
                binding.cvRightUpButton.setCardBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.yellowlight))
                corRUB?.cancel()
            }
        }
        binding.btLeftDown.setOnClickListener {
            binding.ivWolfRightDown.visibility = View.GONE
            binding.ivWolfRightUp.visibility = View.GONE
            binding.ivWolfLeftDown.visibility = View.VISIBLE
            binding.ivWolfLeftUp.visibility = View.GONE
            corLDB = coroutineScope.launch {
                binding.cvLeftDownButton.setCardBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.white))
                delay(150)
                binding.cvLeftDownButton.setCardBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.yellowlight))
                corLDB?.cancel()
            }
        }
        binding.btLeftUp.setOnClickListener {
            binding.ivWolfRightDown.visibility = View.GONE
            binding.ivWolfRightUp.visibility = View.GONE
            binding.ivWolfLeftDown.visibility = View.GONE
            binding.ivWolfLeftUp.visibility = View.VISIBLE
            corLUB = coroutineScope.launch {
                binding.cvLeftUpButton.setCardBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.white))
                delay(150)
                binding.cvLeftUpButton.setCardBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.yellowlight))
                corLUB?.cancel()
            }
        }

        binding.tvTrack1.setOnClickListener {
            binding.tvTrack1.setBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.white))
            binding.tvTrack2.background = null
            binding.tvTrack3.background = null
            binding.tvTrack4.background = null
            binding.tvTrack5.background = null
            binding.tvTrack6.background = null
            binding.tvTrack7.background = null
            showTableBack()
            binding.cvTracks.visibility = View.GONE
            stopMusic()
            playMusic(::track1)
            pressListTracks = false
        }
        binding.tvTrack2.setOnClickListener {
            binding.tvTrack2.setBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.white))
            binding.tvTrack1.background = null
            binding.tvTrack3.background = null
            binding.tvTrack4.background = null
            binding.tvTrack5.background = null
            binding.tvTrack6.background = null
            binding.tvTrack7.background = null
            showTableBack()
            binding.cvTracks.visibility = View.GONE
            stopMusic()
            playMusic(::track2)
            pressListTracks = false
        }
        binding.tvTrack3.setOnClickListener {
            binding.tvTrack3.setBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.white))
            binding.tvTrack2.background = null
            binding.tvTrack1.background = null
            binding.tvTrack4.background = null
            binding.tvTrack5.background = null
            binding.tvTrack6.background = null
            binding.tvTrack7.background = null
            showTableBack()
            binding.cvTracks.visibility = View.GONE
            stopMusic()
            playMusic(::track3)
            pressListTracks = false
        }
        binding.tvTrack4.setOnClickListener {
            binding.tvTrack4.setBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.white))
            binding.tvTrack2.background = null
            binding.tvTrack3.background = null
            binding.tvTrack1.background = null
            binding.tvTrack5.background = null
            binding.tvTrack6.background = null
            binding.tvTrack7.background = null
            showTableBack()
            binding.cvTracks.visibility = View.GONE
            stopMusic()
            playMusic(::track4)
            pressListTracks = false
        }
        binding.tvTrack5.setOnClickListener {
            binding.tvTrack5.setBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.white))
            binding.tvTrack2.background = null
            binding.tvTrack3.background = null
            binding.tvTrack4.background = null
            binding.tvTrack1.background = null
            binding.tvTrack6.background = null
            binding.tvTrack7.background = null
            showTableBack()
            binding.cvTracks.visibility = View.GONE
            stopMusic()
            playMusic(::track5)
            pressListTracks = false
        }
        binding.tvTrack6.setOnClickListener {
            binding.tvTrack6.setBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.white))
            binding.tvTrack2.background = null
            binding.tvTrack3.background = null
            binding.tvTrack4.background = null
            binding.tvTrack5.background = null
            binding.tvTrack1.background = null
            binding.tvTrack7.background = null
            showTableBack()
            binding.cvTracks.visibility = View.GONE
            stopMusic()
            playMusic(::track6)
            pressListTracks = false
        }
        binding.tvTrack7.setOnClickListener {
            binding.tvTrack7.setBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.white))
            binding.tvTrack2.background = null
            binding.tvTrack3.background = null
            binding.tvTrack4.background = null
            binding.tvTrack5.background = null
            binding.tvTrack6.background = null
            binding.tvTrack1.background = null
            showTableBack()
            binding.cvTracks.visibility = View.GONE
            stopMusic()
            playMusic(::track7)
            pressListTracks = false
        }
    }
    private fun seekbar(){
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvSpeedSeekLabel.text = "$progress"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }
    @SuppressLint("SetTextI18n")
    private fun seekspeed() : Int {
        if(binding.tvSpeedSeekLabel.text == "1"){
            speed = 300
            binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
        }
        if(binding.tvSpeedSeekLabel.text == "2"){
            speed = 290
            binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
        }
        if(binding.tvSpeedSeekLabel.text == "3"){
            speed = 280
            binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
        }
        if(binding.tvSpeedSeekLabel.text == "4"){
            speed = 270
            binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
        }
        if(binding.tvSpeedSeekLabel.text == "5"){
            speed = 260
            binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
        }
        if(binding.tvSpeedSeekLabel.text == "6"){
            speed = 250
            binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
        }
        if(binding.tvSpeedSeekLabel.text == "7"){
            speed = 240
            binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
        }
        if(binding.tvSpeedSeekLabel.text == "8"){
            speed = 230
            binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
        }
        if(binding.tvSpeedSeekLabel.text == "9"){
            speed = 220
            binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
        }
        if(binding.tvSpeedSeekLabel.text == "10"){
            speed = 210
            binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
        }
        if(binding.tvSpeedSeekLabel.text == "11"){
            speed = 200
            binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
        }
        if(binding.tvSpeedSeekLabel.text == "12"){
            speed = 190
            binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
        }
        if(binding.tvSpeedSeekLabel.text == "13"){
            speed = 180
            binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
        }
        if(binding.tvSpeedSeekLabel.text == "14"){
            speed = 170
            binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
        }
        if(binding.tvSpeedSeekLabel.text == "15"){
            speed = 160
            binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
        }
        if(binding.tvSpeedSeekLabel.text == "16"){
            speed = 150
            binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
        }
        if(binding.tvSpeedSeekLabel.text == "17"){
            speed = 140
            binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
        }
        if(binding.tvSpeedSeekLabel.text == "18"){
            speed = 130
            binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
        }
        if(binding.tvSpeedSeekLabel.text == "19"){
            speed = 120
            binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
        }
        if(binding.tvSpeedSeekLabel.text == "20"){
            speed = 110
            binding.tvSpeed.text = "Speed : ${binding.seekBar.progress}"
        }
        return binding.seekBar.progress
    }
    @SuppressLint("SetTextI18n")
    private fun coroutineRightDownFall(){
        coroutineRightDownFall?.cancel()
        coroutineRightDownFall = coroutineScope.launch {
                binding.ivEggRightDown.visibility = View.VISIBLE
                binding.ivSmashEggRight.visibility = View.GONE
                binding.ivEggRightDown.translationX = 0f
                binding.ivEggRightDown.translationY = 0f
                eggToLeft()
                delay(speed.toLong())
                eggToLeft()
                delay(speed.toLong())
                eggToLeft()
                delay(speed.toLong())
                eggToLeft()
                delay(speed.toLong())
                eggToLeftDown()
                delay(speed.toLong())
                eggToLeftDown()
                delay(speed.toLong())
                eggToLeftDown()
                delay(speed.toLong())
                eggToLeftDown()
            delay(speed.toLong())
            eggToLeftDown()
            delay(speed.toLong())
            eggToLeftDown()
                if (binding.ivWolfRightDown.visibility == View.VISIBLE) {
                    rightDownCollision()
                } else {
                    delay(speed.toLong())
                    eggToLeftDownSmashUp()
                    delay(speed.toLong())
                    binding.ivEggRightDown.visibility = View.GONE
                    binding.ivSmashEggRight.visibility = View.VISIBLE
                    if (press2 == false && press == true){
                        if(count > bestB){
                            bestB = count
                            binding.tvBestRecordB.text = "Game B : $bestB"
                        }
                        sharedPrefs = getSharedPreferences("SaveBestCount", MODE_PRIVATE)
                        val editor = sharedPrefs.edit()
                        editor?.putInt("savebestB", bestB)
                        editor?.apply()
                    }
                    if(press == false && press2 == true){
                        if(count > bestA){
                            bestA = count
                            binding.tvBestRecordA.text = "Game A : $bestA"
                        }
                        sharedPrefs = getSharedPreferences("SaveBestCount", MODE_PRIVATE)
                        val editor = sharedPrefs.edit()
                        editor?.putInt("savebestA", bestA)
                        editor?.apply()
                    }
                    delay(speed.toLong())
                    count = 0
                    binding.tvCount.text = count.toString()
                    binding.ivPlayA.setImageResource(R.drawable.baseline_play_arrow_24)
                    binding.ivPlayB.setImageResource(R.drawable.baseline_play_arrow_24)
                    binding.tvPlay.text = " play"
                    press = true
                    binding.ivSmashEggRight.visibility = View.GONE
                    binding.ivEggRightDown.translationX = 0f
                    binding.ivEggRightDown.translationY = 0f
                    binding.ivEggRightDown.visibility = View.GONE
                    binding.ivEggRightUp.visibility = View.GONE
                    binding.ivEggRightDownTwo.visibility = View.GONE
                    binding.ivEggRightUpTwo.visibility = View.GONE
                    binding.ivEggLeftDown.visibility = View.GONE
                    binding.ivEggLeftUp.visibility = View.GONE
                    binding.ivEggLeftDownTwo.visibility = View.GONE
                    binding.ivEggLeftUpTwo.visibility = View.GONE
                    coroutineRightUpFall?.cancel()
                    coroutineRightDownFall?.cancel()
                    coroutineLeftUpFall?.cancel()
                    coroutineLeftDownFall?.cancel()
                    coroutineRightUpFallTwo?.cancel()
                    coroutineRightDownFallTwo?.cancel()
                    coroutineLeftUpFallTwo?.cancel()
                    coroutineLeftDownFallTwo?.cancel()
                    coroutine?.cancel()
                    coroutine2?.cancel()
                }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun coroutineLeftDownFall(){
        coroutineLeftDownFall?.cancel()
        coroutineLeftDownFall = coroutineScope.launch {
                binding.ivEggLeftDown.visibility = View.VISIBLE
                binding.ivSmashEggLeft.visibility = View.GONE
                binding.ivEggLeftDown.translationX = 0f
                binding.ivEggLeftDown.translationY = 0f
                eggToRight()
                delay(speed.toLong())
                eggToRight()
                delay(speed.toLong())
                eggToRight()
                delay(speed.toLong())
                eggToRight()
                delay(speed.toLong())
                eggToRightDown()
                delay(speed.toLong())
                eggToRightDown()
                delay(speed.toLong())
                eggToRightDown()
                delay(speed.toLong())
                eggToRightDown()
            delay(speed.toLong())
            eggToRightDown()
            delay(speed.toLong())
            eggToRightDown()
                if (binding.ivWolfLeftDown.visibility == View.VISIBLE) {
                    leftDownCollision()
                } else {
                    delay(speed.toLong())
                    eggToRightDownSmashUp()
                    delay(speed.toLong())
                    binding.ivEggLeftDown.visibility = View.GONE
                    binding.ivSmashEggLeft.visibility = View.VISIBLE
                    if (press2 == false && press == true){
                        if(count > bestB){
                            bestB = count
                            binding.tvBestRecordB.text = "Game B : $bestB"
                        }
                        sharedPrefs = getSharedPreferences("SaveBestCount", MODE_PRIVATE)
                        val editor = sharedPrefs.edit()
                        editor?.putInt("savebestB", bestB)
                        editor?.apply()
                    }
                    if(press == false && press2 == true){
                        if(count > bestA){
                            bestA = count
                            binding.tvBestRecordA.text = "Game A : $bestA"
                        }
                        sharedPrefs = getSharedPreferences("SaveBestCount", MODE_PRIVATE)
                        val editor = sharedPrefs.edit()
                        editor?.putInt("savebestA", bestA)
                        editor?.apply()
                    }
                    delay(speed.toLong())
                    count = 0
                    binding.tvCount.text = count.toString()
                    binding.ivPlayA.setImageResource(R.drawable.baseline_play_arrow_24)
                    binding.ivPlayB.setImageResource(R.drawable.baseline_play_arrow_24)
                    binding.tvPlay.text = " play"
                    press = true
                    binding.ivSmashEggLeft.visibility = View.GONE
                    binding.ivEggLeftDown.translationX = 0f
                    binding.ivEggLeftDown.translationY = 0f
                    binding.ivEggRightDown.visibility = View.GONE
                    binding.ivEggRightUp.visibility = View.GONE
                    binding.ivEggRightDownTwo.visibility = View.GONE
                    binding.ivEggRightUpTwo.visibility = View.GONE
                    binding.ivEggLeftDown.visibility = View.GONE
                    binding.ivEggLeftUp.visibility = View.GONE
                    binding.ivEggLeftDownTwo.visibility = View.GONE
                    binding.ivEggLeftUpTwo.visibility = View.GONE
                    coroutineRightUpFall?.cancel()
                    coroutineRightDownFall?.cancel()
                    coroutineLeftUpFall?.cancel()
                    coroutineLeftDownFall?.cancel()
                    coroutineRightUpFallTwo?.cancel()
                    coroutineRightDownFallTwo?.cancel()
                    coroutineLeftUpFallTwo?.cancel()
                    coroutineLeftDownFallTwo?.cancel()
                    coroutine?.cancel()
                    coroutine2?.cancel()
                }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun coroutineRightUpFall(){
        coroutineRightUpFall?.cancel()
        coroutineRightUpFall = coroutineScope.launch {
                binding.ivEggRightUp.visibility = View.VISIBLE
                binding.ivSmashEggRight.visibility = View.GONE
                binding.ivEggRightUp.translationX = 0f
                binding.ivEggRightUp.translationY = 0f
                eggToLeftUps()
                delay(speed.toLong())
                eggToLeftUps()
                delay(speed.toLong())
                eggToLeftUps()
                delay(speed.toLong())
                eggToLeftUps()
                delay(speed.toLong())
                eggToLeftUp()
                delay(speed.toLong())
                eggToLeftUp()
                delay(speed.toLong())
                eggToLeftUp()
                delay(speed.toLong())
                eggToLeftUp()
                delay(speed.toLong())
                eggToLeftUp()
                delay(speed.toLong())
                eggToLeftUp()
                if (binding.ivWolfRightUp.visibility == View.VISIBLE) {
                    rightUpCollision()
                } else {
                    delay(speed.toLong())
                    eggToLeftUpSmashUp()
                    delay(speed.toLong())
                    binding.ivEggRightUp.visibility = View.GONE
                    binding.ivSmashEggRight.visibility = View.VISIBLE
                    if (press2 == false && press == true){
                        if(count > bestB){
                            bestB = count
                            binding.tvBestRecordB.text = "Game B : $bestB"
                        }
                        sharedPrefs = getSharedPreferences("SaveBestCount", MODE_PRIVATE)
                        val editor = sharedPrefs.edit()
                        editor?.putInt("savebestB", bestB)
                        editor?.apply()
                    }
                    if(press == false && press2 == true){
                        if(count > bestA){
                            bestA = count
                            binding.tvBestRecordA.text = "Game A : $bestA"
                        }
                        sharedPrefs = getSharedPreferences("SaveBestCount", MODE_PRIVATE)
                        val editor = sharedPrefs.edit()
                        editor?.putInt("savebestA", bestA)
                        editor?.apply()
                    }
                    delay(speed.toLong())
                    count = 0
                    binding.tvCount.text = count.toString()
                    binding.ivPlayA.setImageResource(R.drawable.baseline_play_arrow_24)
                    binding.ivPlayB.setImageResource(R.drawable.baseline_play_arrow_24)
                    binding.tvPlay.text = " play"
                    press = true
                    binding.ivEggRightUp.translationX = 0f
                    binding.ivEggRightUp.translationY = 0f
                    binding.ivEggRightDown.visibility = View.GONE
                    binding.ivEggRightUp.visibility = View.GONE
                    binding.ivEggRightDownTwo.visibility = View.GONE
                    binding.ivEggRightUpTwo.visibility = View.GONE
                    binding.ivEggLeftDown.visibility = View.GONE
                    binding.ivEggLeftUp.visibility = View.GONE
                    binding.ivEggLeftDownTwo.visibility = View.GONE
                    binding.ivEggLeftUpTwo.visibility = View.GONE
                    coroutineRightUpFall?.cancel()
                    coroutineRightDownFall?.cancel()
                    coroutineLeftUpFall?.cancel()
                    coroutineLeftDownFall?.cancel()
                    coroutineRightUpFallTwo?.cancel()
                    coroutineRightDownFallTwo?.cancel()
                    coroutineLeftUpFallTwo?.cancel()
                    coroutineLeftDownFallTwo?.cancel()
                    coroutine?.cancel()
                    coroutine2?.cancel()
                }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun coroutineLeftUpFall(){
        coroutineLeftUpFall?.cancel()
        coroutineLeftUpFall = coroutineScope.launch {
                binding.ivEggLeftUp.visibility = View.VISIBLE
                binding.ivSmashEggLeft.visibility = View.GONE
                binding.ivEggLeftUp.translationX = 0f
                binding.ivEggLeftUp.translationY = 0f
                eggToRightUps()
                delay(speed.toLong())
                eggToRightUps()
                delay(speed.toLong())
                eggToRightUps()
                delay(speed.toLong())
                eggToRightUps()
                delay(speed.toLong())
                eggToRightUp()
                delay(speed.toLong())
                eggToRightUp()
                delay(speed.toLong())
                eggToRightUp()
                delay(speed.toLong())
                eggToRightUp()
                delay(speed.toLong())
                eggToRightUp()
                delay(speed.toLong())
                eggToRightUp()
                if (binding.ivWolfLeftUp.visibility == View.VISIBLE) {
                    leftUpCollision()
                } else {
                    delay(speed.toLong())
                    eggToRightUpSmashUp()
                    delay(speed.toLong())
                    binding.ivEggLeftUp.visibility = View.GONE
                    binding.ivSmashEggLeft.visibility = View.VISIBLE
                    if (press2 == false && press == true){
                        if(count > bestB){
                            bestB = count
                            binding.tvBestRecordB.text = "Game B : $bestB"
                        }
                        sharedPrefs = getSharedPreferences("SaveBestCount", MODE_PRIVATE)
                        val editor = sharedPrefs.edit()
                        editor?.putInt("savebestB", bestB)
                        editor?.apply()
                    }
                    if(press == false && press2 == true){
                        if(count > bestA){
                            bestA = count
                            binding.tvBestRecordA.text = "Game A : $bestA"
                        }
                        sharedPrefs = getSharedPreferences("SaveBestCount", MODE_PRIVATE)
                        val editor = sharedPrefs.edit()
                        editor?.putInt("savebestA", bestA)
                        editor?.apply()
                    }
                    delay(speed.toLong())
                    count = 0
                    binding.tvCount.text = count.toString()
                    binding.ivPlayA.setImageResource(R.drawable.baseline_play_arrow_24)
                    binding.ivPlayB.setImageResource(R.drawable.baseline_play_arrow_24)
                    binding.tvPlay.text = " play"
                    press = true
                    binding.ivEggLeftUp.translationX = 0f
                    binding.ivEggLeftUp.translationY = 0f
                    binding.ivEggRightDown.visibility = View.GONE
                    binding.ivEggRightUp.visibility = View.GONE
                    binding.ivEggRightDownTwo.visibility = View.GONE
                    binding.ivEggRightUpTwo.visibility = View.GONE
                    binding.ivEggLeftDown.visibility = View.GONE
                    binding.ivEggLeftUp.visibility = View.GONE
                    binding.ivEggLeftDownTwo.visibility = View.GONE
                    binding.ivEggLeftUpTwo.visibility = View.GONE
                    coroutineRightUpFall?.cancel()
                    coroutineRightDownFall?.cancel()
                    coroutineLeftUpFall?.cancel()
                    coroutineLeftDownFall?.cancel()
                    coroutineRightUpFallTwo?.cancel()
                    coroutineRightDownFallTwo?.cancel()
                    coroutineLeftUpFallTwo?.cancel()
                    coroutineLeftDownFallTwo?.cancel()
                    coroutine?.cancel()
                    coroutine2?.cancel()
                }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun coroutineRightDownFallTwo(){
        coroutineRightDownFallTwo?.cancel()
        coroutineRightDownFallTwo = coroutineScope.launch {
            binding.ivEggRightDownTwo.visibility = View.VISIBLE
            binding.ivSmashEggRight.visibility = View.GONE
            binding.ivEggRightDownTwo.translationX = 0f
            binding.ivEggRightDownTwo.translationY = 0f
            eggToLeftTwo()
            delay(speed.toLong())
            eggToLeftTwo()
            delay(speed.toLong())
            eggToLeftTwo()
            delay(speed.toLong())
            eggToLeftTwo()
            delay(speed.toLong())
            eggToLeftDownTwo()
            delay(speed.toLong())
            eggToLeftDownTwo()
            delay(speed.toLong())
            eggToLeftDownTwo()
            delay(speed.toLong())
            eggToLeftDownTwo()
            delay(speed.toLong())
            eggToLeftDownTwo()
            delay(speed.toLong())
            eggToLeftDownTwo()
            if (binding.ivWolfRightDown.visibility == View.VISIBLE) {
                rightDownCollisionTwo()
            } else {
                delay(speed.toLong())
                eggToLeftDownSmashUpTwo()
                delay(speed.toLong())
                binding.ivEggRightDownTwo.visibility = View.GONE
                binding.ivSmashEggRight.visibility = View.VISIBLE
                if(count > bestB){
                    bestB = count
                    binding.tvBestRecordB.text = "Game B : $bestB"
                }
                sharedPrefs = getSharedPreferences("SaveBestCount", MODE_PRIVATE)
                val editor = sharedPrefs.edit()
                editor?.putInt("savebestB", bestB)
                editor?.apply()
                delay(speed.toLong())
                count = 0
                binding.tvCount.text = count.toString()
                binding.ivPlayB.setImageResource(R.drawable.baseline_play_arrow_24)
                binding.tvPlay.text = " play"
                press2 = true
                binding.ivSmashEggRight.visibility = View.GONE
                binding.ivEggRightDownTwo.translationX = 0f
                binding.ivEggRightDownTwo.translationY = 0f
                binding.ivEggRightDown.visibility = View.GONE
                binding.ivEggRightUp.visibility = View.GONE
                binding.ivEggRightDownTwo.visibility = View.GONE
                binding.ivEggRightUpTwo.visibility = View.GONE
                binding.ivEggLeftDown.visibility = View.GONE
                binding.ivEggLeftUp.visibility = View.GONE
                binding.ivEggLeftDownTwo.visibility = View.GONE
                binding.ivEggLeftUpTwo.visibility = View.GONE
                coroutineRightUpFall?.cancel()
                coroutineRightDownFall?.cancel()
                coroutineLeftUpFall?.cancel()
                coroutineLeftDownFall?.cancel()
                coroutineRightUpFallTwo?.cancel()
                coroutineRightDownFallTwo?.cancel()
                coroutineLeftUpFallTwo?.cancel()
                coroutineLeftDownFallTwo?.cancel()
                coroutine?.cancel()
                coroutine2?.cancel()
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun coroutineLeftDownFallTwo(){
        coroutineLeftDownFallTwo?.cancel()
        coroutineLeftDownFallTwo = coroutineScope.launch {
            binding.ivEggLeftDownTwo.visibility = View.VISIBLE
            binding.ivSmashEggLeft.visibility = View.GONE
            binding.ivEggLeftDownTwo.translationX = 0f
            binding.ivEggLeftDownTwo.translationY = 0f
            eggToRightTwo()
            delay(speed.toLong())
            eggToRightTwo()
            delay(speed.toLong())
            eggToRightTwo()
            delay(speed.toLong())
            eggToRightTwo()
            delay(speed.toLong())
            eggToRightDownTwo()
            delay(speed.toLong())
            eggToRightDownTwo()
            delay(speed.toLong())
            eggToRightDownTwo()
            delay(speed.toLong())
            eggToRightDownTwo()
            delay(speed.toLong())
            eggToRightDownTwo()
            delay(speed.toLong())
            eggToRightDownTwo()
            if (binding.ivWolfLeftDown.visibility == View.VISIBLE) {
                leftDownCollisionTwo()
            } else {
                delay(speed.toLong())
                eggToRightDownSmashUpTwo()
                delay(speed.toLong())
                binding.ivEggLeftDownTwo.visibility = View.GONE
                binding.ivSmashEggLeft.visibility = View.VISIBLE
                if(count > bestB){
                    bestB = count
                    binding.tvBestRecordB.text = "Game B : $bestB"
                }
                sharedPrefs = getSharedPreferences("SaveBestCount", MODE_PRIVATE)
                val editor = sharedPrefs.edit()
                editor?.putInt("savebestB", bestB)
                editor?.apply()
                delay(speed.toLong())
                count = 0
                binding.tvCount.text = count.toString()
                binding.ivPlayB.setImageResource(R.drawable.baseline_play_arrow_24)
                binding.tvPlay.text = " play"
                press2 = true
                binding.ivSmashEggLeft.visibility = View.GONE
                binding.ivEggLeftDownTwo.translationX = 0f
                binding.ivEggLeftDownTwo.translationY = 0f
                binding.ivEggRightDown.visibility = View.GONE
                binding.ivEggRightUp.visibility = View.GONE
                binding.ivEggRightDownTwo.visibility = View.GONE
                binding.ivEggRightUpTwo.visibility = View.GONE
                binding.ivEggLeftDown.visibility = View.GONE
                binding.ivEggLeftUp.visibility = View.GONE
                binding.ivEggLeftDownTwo.visibility = View.GONE
                binding.ivEggLeftUpTwo.visibility = View.GONE
                coroutineRightUpFall?.cancel()
                coroutineRightDownFall?.cancel()
                coroutineLeftUpFall?.cancel()
                coroutineLeftDownFall?.cancel()
                coroutineRightUpFallTwo?.cancel()
                coroutineRightDownFallTwo?.cancel()
                coroutineLeftUpFallTwo?.cancel()
                coroutineLeftDownFallTwo?.cancel()
                coroutine?.cancel()
                coroutine2?.cancel()
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun coroutineRightUpFallTwo(){
        coroutineRightUpFallTwo?.cancel()
        coroutineRightUpFallTwo = coroutineScope.launch {
            binding.ivEggRightUpTwo.visibility = View.VISIBLE
            binding.ivSmashEggRight.visibility = View.GONE
            binding.ivEggRightUpTwo.translationX = 0f
            binding.ivEggRightUpTwo.translationY = 0f
            eggToLeftUpsTwo()
            delay(speed.toLong())
            eggToLeftUpsTwo()
            delay(speed.toLong())
            eggToLeftUpsTwo()
            delay(speed.toLong())
            eggToLeftUpsTwo()
            delay(speed.toLong())
            eggToLeftUpTwo()
            delay(speed.toLong())
            eggToLeftUpTwo()
            delay(speed.toLong())
            eggToLeftUpTwo()
            delay(speed.toLong())
            eggToLeftUpTwo()
            delay(speed.toLong())
            eggToLeftUpTwo()
            delay(speed.toLong())
            eggToLeftUpTwo()
            if (binding.ivWolfRightUp.visibility == View.VISIBLE) {
                rightUpCollisionTwo()
            } else {
                delay(speed.toLong())
                eggToLeftUpSmashUpTwo()
                delay(speed.toLong())
                binding.ivEggRightUp.visibility = View.GONE
                binding.ivSmashEggRight.visibility = View.VISIBLE
                if(count > bestB){
                    bestB = count
                    binding.tvBestRecordB.text = "Game B : $bestB"
                }
                sharedPrefs = getSharedPreferences("SaveBestCount", MODE_PRIVATE)
                val editor = sharedPrefs.edit()
                editor?.putInt("savebestB", bestB)
                editor?.apply()
                delay(speed.toLong())
                count = 0
                binding.tvCount.text = count.toString()
                binding.ivPlayB.setImageResource(R.drawable.baseline_play_arrow_24)
                binding.tvPlay.text = " play"
                press2 = true
                binding.ivEggRightUpTwo.translationX = 0f
                binding.ivEggRightUpTwo.translationY = 0f
                binding.ivEggRightDown.visibility = View.GONE
                binding.ivEggRightUp.visibility = View.GONE
                binding.ivEggRightDownTwo.visibility = View.GONE
                binding.ivEggRightUpTwo.visibility = View.GONE
                binding.ivEggLeftDown.visibility = View.GONE
                binding.ivEggLeftUp.visibility = View.GONE
                binding.ivEggLeftDownTwo.visibility = View.GONE
                binding.ivEggLeftUpTwo.visibility = View.GONE
                coroutineRightUpFall?.cancel()
                coroutineRightDownFall?.cancel()
                coroutineLeftUpFall?.cancel()
                coroutineLeftDownFall?.cancel()
                coroutineRightUpFallTwo?.cancel()
                coroutineRightDownFallTwo?.cancel()
                coroutineLeftUpFallTwo?.cancel()
                coroutineLeftDownFallTwo?.cancel()
                coroutine?.cancel()
                coroutine2?.cancel()
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun coroutineLeftUpFallTwo(){
        coroutineLeftUpFallTwo?.cancel()
        coroutineLeftUpFallTwo = coroutineScope.launch {
            binding.ivEggLeftUpTwo.visibility = View.VISIBLE
            binding.ivSmashEggLeft.visibility = View.GONE
            binding.ivEggLeftUpTwo.translationX = 0f
            binding.ivEggLeftUpTwo.translationY = 0f
            eggToRightUpsTwo()
            delay(speed.toLong())
            eggToRightUpsTwo()
            delay(speed.toLong())
            eggToRightUpsTwo()
            delay(speed.toLong())
            eggToRightUpsTwo()
            delay(speed.toLong())
            eggToRightUpTwo()
            delay(speed.toLong())
            eggToRightUpTwo()
            delay(speed.toLong())
            eggToRightUpTwo()
            delay(speed.toLong())
            eggToRightUpTwo()
            delay(speed.toLong())
            eggToRightUpTwo()
            delay(speed.toLong())
            eggToRightUpTwo()
            if (binding.ivWolfLeftUp.visibility == View.VISIBLE) {
                leftUpCollisionTwo()
            } else {
                delay(speed.toLong())
                eggToRightUpSmashUpTwo()
                delay(speed.toLong())
                binding.ivEggLeftUpTwo.visibility = View.GONE
                binding.ivSmashEggLeft.visibility = View.VISIBLE
                if(count > bestB){
                    bestB = count
                    binding.tvBestRecordB.text = "Game B : $bestB"
                }
                sharedPrefs = getSharedPreferences("SaveBestCount", MODE_PRIVATE)
                val editor = sharedPrefs.edit()
                editor?.putInt("savebestB", bestB)
                editor?.apply()
                delay(speed.toLong())
                count = 0
                binding.tvCount.text = count.toString()
                binding.ivPlayB.setImageResource(R.drawable.baseline_play_arrow_24)
                binding.tvPlay.text = " play"
                press2 = true
                binding.ivEggLeftUpTwo.translationX = 0f
                binding.ivEggLeftUpTwo.translationY = 0f
                binding.ivEggRightDown.visibility = View.GONE
                binding.ivEggRightUp.visibility = View.GONE
                binding.ivEggRightDownTwo.visibility = View.GONE
                binding.ivEggRightUpTwo.visibility = View.GONE
                binding.ivEggLeftDown.visibility = View.GONE
                binding.ivEggLeftUp.visibility = View.GONE
                binding.ivEggLeftDownTwo.visibility = View.GONE
                binding.ivEggLeftUpTwo.visibility = View.GONE
                coroutineRightUpFall?.cancel()
                coroutineRightDownFall?.cancel()
                coroutineLeftUpFall?.cancel()
                coroutineLeftDownFall?.cancel()
                coroutineRightUpFallTwo?.cancel()
                coroutineRightDownFallTwo?.cancel()
                coroutineLeftUpFallTwo?.cancel()
                coroutineLeftDownFallTwo?.cancel()
                coroutine?.cancel()
                coroutine2?.cancel()
            }
        }
    }
    private fun CoroutineScope.launchRandomFunction() = launch {
        do {
            newFunction = functions.random()
        } while (newFunction == lastFunction)
        lastFunction = newFunction
        newFunction?.invoke()
}
    private fun CoroutineScope.launchRandomFunctionTwo() = launch {
        do {
            newFunction2 = functions2.random()
        } while (newFunction2 == lastFunction2)
        lastFunction2 = newFunction2
        newFunction2?.invoke()
    }
    private fun showTable(){
        val animation = ScaleAnimation(1f, 1f, 0f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0f)
        animation.duration = 500
        binding.cvTracks.startAnimation(animation)
    }
    private fun showTableBack(){
        val animation = ScaleAnimation(1f, 1f, 1f, 0f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0f)
        animation.duration = 500
        binding.cvTracks.startAnimation(animation)
    }
    private fun showSpeed(){
        val animation = ScaleAnimation(1f, 1f, 0f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0f)
        animation.duration = 500
        binding.cvSeekSpeed.startAnimation(animation)
    }
    private fun showSpeedBack(){
        val animation = ScaleAnimation(1f, 1f, 1f, 0f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0f)
        animation.duration = 500
        binding.cvSeekSpeed.startAnimation(animation)
    }
    private fun track1() : MediaPlayer {
       return MediaPlayer.create(this, R.raw.montazh)
    }
    private fun track2() : MediaPlayer {
       return MediaPlayer.create(this, R.raw.chyotkaja)
    }
    private fun track3() : MediaPlayer {
       return MediaPlayer.create(this, R.raw.knigkaskazokeight)
    }
    private fun track4() : MediaPlayer {
       return MediaPlayer.create(this, R.raw.knigkaskazokfour)
    }
    private fun track5() : MediaPlayer {
       return MediaPlayer.create(this, R.raw.knigkaskazokone)
    }
    private fun track6() : MediaPlayer {
       return MediaPlayer.create(this, R.raw.knigkaskazoktwo)
    }
    private fun track7() : MediaPlayer {
       return MediaPlayer.create(this, R.raw.veselaja)
    }
    private fun playMusic(track: () -> MediaPlayer) {
        if (::musicPlay.isInitialized && musicPlay.isPlaying) {
            musicPlay.stop()
            musicPlay.release()
        }
        musicPlay = track()
        musicPlay.isLooping = true
        musicPlay.start()
    }
    private fun stopMusic() {
        if (::musicPlay.isInitialized) {
            if (musicPlay.isPlaying) {
                musicPlay.stop()
            }
        }
    }
    private fun releaseMusicPlay() {
        if (::musicPlay.isInitialized) {
            if (musicPlay.isPlaying) {
                musicPlay.stop()
            }
            musicPlay.release()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        stopMusic()
        releaseMusicPlay()
        coroutineButterfly?.cancel()
    }
    private fun butterflyOne(){
        binding.ivButterflyOne.visibility = View.VISIBLE
        corButOne = coroutineScope.launch {
            ObjectAnimator.ofFloat(binding.ivButterflyOne, "translationX", 200f).apply {
                duration = 3000
                start()
            }
            ObjectAnimator.ofFloat(binding.ivButterflyOne, "translationY", -200f).apply {
                duration = 3000
                start()
            }
            delay(200)
            ObjectAnimator.ofFloat(binding.ivButterflyOne, "translationX", 2200f).apply {
                duration = 9000
                start()
            }
            ObjectAnimator.ofFloat(binding.ivButterflyOne, "translationY", -800f).apply {
                duration = 9000
                start()
            }
            delay(200)
            corButOne?.cancel()
        }
    }
    private fun butterflyTwo(){
        binding.ivButterflyTwo.visibility = View.VISIBLE
        corButTwo = coroutineScope.launch {
            ObjectAnimator.ofFloat(binding.ivButterflyTwo, "translationX", 300f).apply {
                duration = 4000
                start()
            }
            ObjectAnimator.ofFloat(binding.ivButterflyTwo, "translationY", 300f).apply {
                duration = 4000
                start()
            }
            delay(200)
            ObjectAnimator.ofFloat(binding.ivButterflyTwo, "translationX", 2200f).apply {
                duration = 11000
                start()
            }
            ObjectAnimator.ofFloat(binding.ivButterflyTwo, "translationY", 600f).apply {
                duration = 11000
                start()
            }
            delay(200)
            corButTwo?.cancel()
        }
    }
    private fun butterflyThree(){
        binding.ivButterflyThree.visibility = View.VISIBLE
        corButThree = coroutineScope.launch {
            ObjectAnimator.ofFloat(binding.ivButterflyThree, "translationX", -500f).apply {
                duration = 6000
                start()
            }
            ObjectAnimator.ofFloat(binding.ivButterflyThree, "translationY", 200f).apply {
                duration = 6000
                start()
            }
            delay(200)
            ObjectAnimator.ofFloat(binding.ivButterflyThree, "translationX", -2500f).apply {
                duration = 13000
                start()
            }
            ObjectAnimator.ofFloat(binding.ivButterflyThree, "translationY", 700f).apply {
                duration = 13000
                start()
            }
            delay(200)
            corButThree?.cancel()
        }
    }
    private fun butterflyFour(){
        binding.ivButterflyFour.visibility = View.VISIBLE
        corButFour = coroutineScope.launch {
            ObjectAnimator.ofFloat(binding.ivButterflyFour, "translationX", -150f).apply {
                duration = 4000
                start()
            }
            ObjectAnimator.ofFloat(binding.ivButterflyFour, "translationY", -400f).apply {
                duration = 4000
                start()
            }
            delay(200)
            ObjectAnimator.ofFloat(binding.ivButterflyFour, "translationX", -2200f).apply {
                duration = 15000
                start()
            }
            ObjectAnimator.ofFloat(binding.ivButterflyFour, "translationY", -700f).apply {
                duration = 15000
                start()
            }
            delay(200)
            corButFour?.cancel()
        }
    }
    private fun butterflies(){
       coroutineButterfly =  coroutineScope.launch{
            while(true){
                butterflyOne()
                delay(11000)
                binding.ivButterflyOne.visibility = View.GONE
                binding.ivButterflyOne.translationX = 0f
                binding.ivButterflyOne.translationY = 0f
                butterflyThree()
                delay(20000)
                binding.ivButterflyThree.visibility = View.GONE
                binding.ivButterflyThree.translationX = 0f
                binding.ivButterflyThree.translationY = 0f
                butterflyTwo()
                delay(25000)
                binding.ivButterflyTwo.visibility = View.GONE
                binding.ivButterflyTwo.translationX = 0f
                binding.ivButterflyTwo.translationY = 0f
                butterflyFour()
                delay(30000)
                binding.ivButterflyFour.visibility = View.GONE
                binding.ivButterflyFour.translationX = 0f
                binding.ivButterflyFour.translationY = 0f
                butterflyThree()
                delay(15000)
                binding.ivButterflyThree.visibility = View.GONE
                binding.ivButterflyThree.translationX = 0f
                binding.ivButterflyThree.translationY = 0f
                butterflyOne()
                delay(37000)
                binding.ivButterflyOne.visibility = View.GONE
                binding.ivButterflyOne.translationX = 0f
                binding.ivButterflyOne.translationY = 0f
                butterflyTwo()
                delay(12000)
                binding.ivButterflyTwo.visibility = View.GONE
                binding.ivButterflyTwo.translationX = 0f
                binding.ivButterflyTwo.translationY = 0f
                butterflyFour()
                delay(20000)
                binding.ivButterflyFour.visibility = View.GONE
                binding.ivButterflyFour.translationX = 0f
                binding.ivButterflyFour.translationY = 0f
            }
        }
    }

    @SuppressLint("Recycle")
    private fun eggToLeft(){
        currentRotation = binding.ivEggRightDown.rotation
        currentTranslationX = binding.ivEggRightDown.translationX
            val translationXAnimator = ObjectAnimator.ofFloat(binding.ivEggRightDown, "translationX", currentTranslationX, (currentTranslationX - 25f))
            translationXAnimator.duration = speed.toLong()
            translationXAnimator.start()

            val rotationAnimator = ObjectAnimator.ofFloat(binding.ivEggRightDown, "rotation", currentRotation, (currentRotation - 90f))
            rotationAnimator.duration = speed.toLong()
            rotationAnimator.start()

            currentRotation = binding.ivEggRightDown.rotation
            currentTranslationX = binding.ivEggRightDown.translationX
    }
    private fun eggToLeftUps(){
        currentRotation = binding.ivEggRightUp.rotation
        currentTranslationX = binding.ivEggRightUp.translationX
        val translationXAnimator = ObjectAnimator.ofFloat(binding.ivEggRightUp, "translationX", currentTranslationX, (currentTranslationX - 25f))
        translationXAnimator.duration = speed.toLong()
        translationXAnimator.start()

        val rotationAnimator = ObjectAnimator.ofFloat(binding.ivEggRightUp, "rotation", currentRotation, (currentRotation - 90f))
        rotationAnimator.duration = speed.toLong()
        rotationAnimator.start()

        currentRotation = binding.ivEggRightUp.rotation
        currentTranslationX = binding.ivEggRightUp.translationX
    }
    @SuppressLint("Recycle")
    private fun eggToRight(){
        currentRotation = binding.ivEggLeftDown.rotation
        currentTranslationX = binding.ivEggLeftDown.translationX
        val translationXAnimator = ObjectAnimator.ofFloat(binding.ivEggLeftDown, "translationX", currentTranslationX, (currentTranslationX + 25f))
        translationXAnimator.duration = speed.toLong()
        translationXAnimator.start()

        val rotationAnimator = ObjectAnimator.ofFloat(binding.ivEggLeftDown, "rotation", currentRotation, (currentRotation + 90f))
        rotationAnimator.duration = speed.toLong()
        rotationAnimator.start()

        currentRotation = binding.ivEggLeftDown.rotation
        currentTranslationX = binding.ivEggLeftDown.translationX
    }
    private fun eggToRightUps(){
        currentRotation = binding.ivEggLeftUp.rotation
        currentTranslationX = binding.ivEggLeftUp.translationX
        val translationXAnimator = ObjectAnimator.ofFloat(binding.ivEggLeftUp, "translationX", currentTranslationX, (currentTranslationX + 25f))
        translationXAnimator.duration = speed.toLong()
        translationXAnimator.start()

        val rotationAnimator = ObjectAnimator.ofFloat(binding.ivEggLeftUp, "rotation", currentRotation, (currentRotation + 90f))
        rotationAnimator.duration = speed.toLong()
        rotationAnimator.start()

        currentRotation = binding.ivEggLeftUp.rotation
        currentTranslationX = binding.ivEggLeftUp.translationX
    }
    private fun eggToLeftDown(){
        currentRotation = binding.ivEggRightDown.rotation
        currentTranslationX = binding.ivEggRightDown.translationX
        currentTranslationY = binding.ivEggRightDown.translationY
        ObjectAnimator.ofFloat(binding.ivEggRightDown, "translationX", currentTranslationX, (currentTranslationX - 25f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightDown, "translationY", currentTranslationY, (currentTranslationY + 15f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightDown, "rotation", currentRotation, (currentRotation - 90f)).apply {
            duration = speed.toLong()
            start()
        }
        currentRotation = binding.ivEggRightDown.rotation
        currentTranslationX = binding.ivEggRightDown.translationX
        currentTranslationY = binding.ivEggRightDown.translationY
    }
    private fun eggToLeftUp(){
        currentRotation = binding.ivEggRightUp.rotation
        currentTranslationX = binding.ivEggRightUp.translationX
        currentTranslationY = binding.ivEggRightUp.translationY
        ObjectAnimator.ofFloat(binding.ivEggRightUp, "translationX", currentTranslationX, (currentTranslationX - 25f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightUp, "translationY", currentTranslationY, (currentTranslationY + 15f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightUp, "rotation", currentRotation, (currentRotation - 90f)).apply {
            duration = speed.toLong()
            start()
        }
        currentRotation = binding.ivEggRightUp.rotation
        currentTranslationX = binding.ivEggRightUp.translationX
        currentTranslationY = binding.ivEggRightUp.translationY
    }
    private fun eggToRightDown(){
        currentRotation = binding.ivEggLeftDown.rotation
        currentTranslationX = binding.ivEggLeftDown.translationX
        currentTranslationY = binding.ivEggLeftDown.translationY
        ObjectAnimator.ofFloat(binding.ivEggLeftDown, "translationX", currentTranslationX, (currentTranslationX + 25f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftDown, "translationY", currentTranslationY, (currentTranslationY + 15f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftDown, "rotation", currentRotation, (currentRotation + 90f)).apply {
            duration = speed.toLong()
            start()
        }
        currentRotation = binding.ivEggLeftDown.rotation
        currentTranslationX = binding.ivEggLeftDown.translationX
        currentTranslationY = binding.ivEggLeftDown.translationY
    }
    private fun eggToRightUp(){
        currentRotation = binding.ivEggLeftUp.rotation
        currentTranslationX = binding.ivEggLeftUp.translationX
        currentTranslationY = binding.ivEggLeftUp.translationY
        ObjectAnimator.ofFloat(binding.ivEggLeftUp, "translationX", currentTranslationX, (currentTranslationX + 25f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftUp, "translationY", currentTranslationY, (currentTranslationY + 15f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftUp, "rotation", currentRotation, (currentRotation + 90f)).apply {
            duration = speed.toLong()
            start()
        }
        currentRotation = binding.ivEggLeftUp.rotation
        currentTranslationX = binding.ivEggLeftUp.translationX
        currentTranslationY = binding.ivEggLeftUp.translationY
    }
    @SuppressLint("Recycle")
    private fun eggToLeftTwo(){
        currentRotation = binding.ivEggRightDownTwo.rotation
        currentTranslationX = binding.ivEggRightDownTwo.translationX
        val translationXAnimator = ObjectAnimator.ofFloat(binding.ivEggRightDownTwo, "translationX", currentTranslationX, (currentTranslationX - 25f))
        translationXAnimator.duration = speed.toLong()
        translationXAnimator.start()

        val rotationAnimator = ObjectAnimator.ofFloat(binding.ivEggRightDownTwo, "rotation", currentRotation, (currentRotation - 90f))
        rotationAnimator.duration = speed.toLong()
        rotationAnimator.start()

        currentRotation = binding.ivEggRightDownTwo.rotation
        currentTranslationX = binding.ivEggRightDownTwo.translationX
    }
    private fun eggToLeftUpsTwo(){
        currentRotation = binding.ivEggRightUpTwo.rotation
        currentTranslationX = binding.ivEggRightUpTwo.translationX
        val translationXAnimator = ObjectAnimator.ofFloat(binding.ivEggRightUpTwo, "translationX", currentTranslationX, (currentTranslationX - 25f))
        translationXAnimator.duration = speed.toLong()
        translationXAnimator.start()

        val rotationAnimator = ObjectAnimator.ofFloat(binding.ivEggRightUpTwo, "rotation", currentRotation, (currentRotation - 90f))
        rotationAnimator.duration = speed.toLong()
        rotationAnimator.start()

        currentRotation = binding.ivEggRightUpTwo.rotation
        currentTranslationX = binding.ivEggRightUpTwo.translationX
    }
    @SuppressLint("Recycle")
    private fun eggToRightTwo(){
        currentRotation = binding.ivEggLeftDownTwo.rotation
        currentTranslationX = binding.ivEggLeftDownTwo.translationX
        val translationXAnimator = ObjectAnimator.ofFloat(binding.ivEggLeftDownTwo, "translationX", currentTranslationX, (currentTranslationX + 25f))
        translationXAnimator.duration = speed.toLong()
        translationXAnimator.start()

        val rotationAnimator = ObjectAnimator.ofFloat(binding.ivEggLeftDownTwo, "rotation", currentRotation, (currentRotation + 90f))
        rotationAnimator.duration = speed.toLong()
        rotationAnimator.start()

        currentRotation = binding.ivEggLeftDownTwo.rotation
        currentTranslationX = binding.ivEggLeftDownTwo.translationX
    }
    private fun eggToRightUpsTwo(){
        currentRotation = binding.ivEggLeftUpTwo.rotation
        currentTranslationX = binding.ivEggLeftUpTwo.translationX
        val translationXAnimator = ObjectAnimator.ofFloat(binding.ivEggLeftUpTwo, "translationX", currentTranslationX, (currentTranslationX + 25f))
        translationXAnimator.duration = speed.toLong()
        translationXAnimator.start()

        val rotationAnimator = ObjectAnimator.ofFloat(binding.ivEggLeftUpTwo, "rotation", currentRotation, (currentRotation + 90f))
        rotationAnimator.duration = speed.toLong()
        rotationAnimator.start()

        currentRotation = binding.ivEggLeftUpTwo.rotation
        currentTranslationX = binding.ivEggLeftUpTwo.translationX
    }
    private fun eggToLeftDownTwo(){
        currentRotation = binding.ivEggRightDownTwo.rotation
        currentTranslationX = binding.ivEggRightDownTwo.translationX
        currentTranslationY = binding.ivEggRightDownTwo.translationY
        ObjectAnimator.ofFloat(binding.ivEggRightDownTwo, "translationX", currentTranslationX, (currentTranslationX - 25f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightDownTwo, "translationY", currentTranslationY, (currentTranslationY + 15f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightDownTwo, "rotation", currentRotation, (currentRotation - 90f)).apply {
            duration = speed.toLong()
            start()
        }
        currentRotation = binding.ivEggRightDownTwo.rotation
        currentTranslationX = binding.ivEggRightDownTwo.translationX
        currentTranslationY = binding.ivEggRightDownTwo.translationY
    }
    private fun eggToLeftUpTwo(){
        currentRotation = binding.ivEggRightUpTwo.rotation
        currentTranslationX = binding.ivEggRightUpTwo.translationX
        currentTranslationY = binding.ivEggRightUpTwo.translationY
        ObjectAnimator.ofFloat(binding.ivEggRightUpTwo, "translationX", currentTranslationX, (currentTranslationX - 25f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightUpTwo, "translationY", currentTranslationY, (currentTranslationY + 15f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightUpTwo, "rotation", currentRotation, (currentRotation - 90f)).apply {
            duration = speed.toLong()
            start()
        }
        currentRotation = binding.ivEggRightUpTwo.rotation
        currentTranslationX = binding.ivEggRightUpTwo.translationX
        currentTranslationY = binding.ivEggRightUpTwo.translationY
    }
    private fun eggToRightDownTwo(){
        currentRotation = binding.ivEggLeftDownTwo.rotation
        currentTranslationX = binding.ivEggLeftDownTwo.translationX
        currentTranslationY = binding.ivEggLeftDownTwo.translationY
        ObjectAnimator.ofFloat(binding.ivEggLeftDownTwo, "translationX", currentTranslationX, (currentTranslationX + 25f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftDownTwo, "translationY", currentTranslationY, (currentTranslationY + 15f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftDownTwo, "rotation", currentRotation, (currentRotation + 90f)).apply {
            duration = speed.toLong()
            start()
        }
        currentRotation = binding.ivEggLeftDownTwo.rotation
        currentTranslationX = binding.ivEggLeftDownTwo.translationX
        currentTranslationY = binding.ivEggLeftDownTwo.translationY
    }
    private fun eggToRightUpTwo(){
        currentRotation = binding.ivEggLeftUpTwo.rotation
        currentTranslationX = binding.ivEggLeftUpTwo.translationX
        currentTranslationY = binding.ivEggLeftUpTwo.translationY
        ObjectAnimator.ofFloat(binding.ivEggLeftUpTwo, "translationX", currentTranslationX, (currentTranslationX + 25f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftUpTwo, "translationY", currentTranslationY, (currentTranslationY + 15f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftUpTwo, "rotation", currentRotation, (currentRotation + 90f)).apply {
            duration = speed.toLong()
            start()
        }
        currentRotation = binding.ivEggLeftUpTwo.rotation
        currentTranslationX = binding.ivEggLeftUpTwo.translationX
        currentTranslationY = binding.ivEggLeftUpTwo.translationY
    }
    private fun eggToLeftDownSmashUp(){
        currentRotation = binding.ivEggRightDown.rotation
        currentTranslationX = binding.ivEggRightDown.translationX
        currentTranslationY = binding.ivEggRightDown.translationY
        ObjectAnimator.ofFloat(binding.ivEggRightDown, "translationX", currentTranslationX, (currentTranslationX - 25f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightDown, "translationY", currentTranslationY, (currentTranslationY + 100f)).apply{
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightDown, "rotation", currentRotation, (currentRotation - 90f)).apply{
            duration = speed.toLong()
            start()
        }
        currentRotation = binding.ivEggRightDown.rotation
        currentTranslationX = binding.ivEggRightDown.translationX
        currentTranslationY = binding.ivEggRightDown.translationY
    }
    private fun eggToLeftUpSmashUp(){
        currentRotation = binding.ivEggRightUp.rotation
        currentTranslationX = binding.ivEggRightUp.translationX
        currentTranslationY = binding.ivEggRightUp.translationY
        ObjectAnimator.ofFloat(binding.ivEggRightUp, "translationX", currentTranslationX, (currentTranslationX - 25f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightUp, "translationY", currentTranslationY, (currentTranslationY + 100f)).apply{
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightUp, "rotation", currentRotation, (currentRotation - 90f)).apply{
            duration = speed.toLong()
            start()
        }
        currentRotation = binding.ivEggRightUp.rotation
        currentTranslationX = binding.ivEggRightUp.translationX
        currentTranslationY = binding.ivEggRightUp.translationY
    }
    private fun eggToRightDownSmashUp() {
        currentRotation = binding.ivEggLeftDown.rotation
        currentTranslationX = binding.ivEggLeftDown.translationX
        currentTranslationY = binding.ivEggLeftDown.translationY
        ObjectAnimator.ofFloat(binding.ivEggLeftDown, "translationX", currentTranslationX, (currentTranslationX + 25f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftDown, "translationY", currentTranslationY, (currentTranslationY + 100f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftDown, "rotation", currentRotation, (currentRotation + 90f)).apply {
            duration = speed.toLong()
            start()
        }
        currentRotation = binding.ivEggLeftDown.rotation
        currentTranslationX = binding.ivEggLeftDown.translationX
        currentTranslationY = binding.ivEggLeftDown.translationY
    }
    private fun eggToRightUpSmashUp(){
        currentRotation = binding.ivEggLeftUp.rotation
        currentTranslationX = binding.ivEggLeftUp.translationX
        currentTranslationY = binding.ivEggLeftUp.translationY
        ObjectAnimator.ofFloat(binding.ivEggLeftUp, "translationX", currentTranslationX, (currentTranslationX + 25f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftUp, "translationY", currentTranslationY, (currentTranslationY + 100f)).apply{
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftUp, "rotation", currentRotation, (currentRotation + 90f)).apply{
            duration = speed.toLong()
            start()
        }
        currentRotation = binding.ivEggLeftUp.rotation
        currentTranslationX = binding.ivEggLeftUp.translationX
        currentTranslationY = binding.ivEggLeftUp.translationY
    }
    private fun eggToLeftDownSmashUpTwo(){
        currentRotation = binding.ivEggRightDownTwo.rotation
        currentTranslationX = binding.ivEggRightDownTwo.translationX
        currentTranslationY = binding.ivEggRightDownTwo.translationY
        ObjectAnimator.ofFloat(binding.ivEggRightDownTwo, "translationX", currentTranslationX, (currentTranslationX - 25f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightDownTwo, "translationY", currentTranslationY, (currentTranslationY + 100f)).apply{
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightDownTwo, "rotation", currentRotation, (currentRotation - 90f)).apply{
            duration = speed.toLong()
            start()
        }
        currentRotation = binding.ivEggRightDownTwo.rotation
        currentTranslationX = binding.ivEggRightDownTwo.translationX
        currentTranslationY = binding.ivEggRightDownTwo.translationY
    }
    private fun eggToLeftUpSmashUpTwo(){
        currentRotation = binding.ivEggRightUpTwo.rotation
        currentTranslationX = binding.ivEggRightUpTwo.translationX
        currentTranslationY = binding.ivEggRightUpTwo.translationY
        ObjectAnimator.ofFloat(binding.ivEggRightUpTwo, "translationX", currentTranslationX, (currentTranslationX - 25f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightUpTwo, "translationY", currentTranslationY, (currentTranslationY + 100f)).apply{
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightUpTwo, "rotation", currentRotation, (currentRotation - 90f)).apply{
            duration = speed.toLong()
            start()
        }
        currentRotation = binding.ivEggRightUpTwo.rotation
        currentTranslationX = binding.ivEggRightUpTwo.translationX
        currentTranslationY = binding.ivEggRightUpTwo.translationY
    }
    private fun eggToRightDownSmashUpTwo() {
        currentRotation = binding.ivEggLeftDownTwo.rotation
        currentTranslationX = binding.ivEggLeftDownTwo.translationX
        currentTranslationY = binding.ivEggLeftDownTwo.translationY
        ObjectAnimator.ofFloat(binding.ivEggLeftDownTwo, "translationX", currentTranslationX, (currentTranslationX + 25f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftDownTwo, "translationY", currentTranslationY, (currentTranslationY + 100f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftDownTwo, "rotation", currentRotation, (currentRotation + 90f)).apply {
            duration = speed.toLong()
            start()
        }
        currentRotation = binding.ivEggLeftDownTwo.rotation
        currentTranslationX = binding.ivEggLeftDownTwo.translationX
        currentTranslationY = binding.ivEggLeftDownTwo.translationY
    }
    private fun eggToRightUpSmashUpTwo(){
        currentRotation = binding.ivEggLeftUpTwo.rotation
        currentTranslationX = binding.ivEggLeftUpTwo.translationX
        currentTranslationY = binding.ivEggLeftUpTwo.translationY
        ObjectAnimator.ofFloat(binding.ivEggLeftUpTwo, "translationX", currentTranslationX, (currentTranslationX + 25f)).apply {
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftUpTwo, "translationY", currentTranslationY, (currentTranslationY + 100f)).apply{
            duration = speed.toLong()
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftUpTwo, "rotation", currentRotation, (currentRotation + 90f)).apply{
            duration = speed.toLong()
            start()
        }
        currentRotation = binding.ivEggLeftUpTwo.rotation
        currentTranslationX = binding.ivEggLeftUpTwo.translationX
        currentTranslationY = binding.ivEggLeftUpTwo.translationY
    }
    @SuppressLint("SetTextI18n")
    private fun rightDownCollision(){
        val rect1 = Rect()
        binding.ivEggRightDown.getHitRect(rect1)
        val rect2 = Rect()
        binding.ivWolfRightDown.getHitRect(rect2)
        val collision = rect1.intersect(rect2)
        if (collision) {
            count++
            if(count in 51..100 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 101..150 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 151..200 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 201..250 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 251..300 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 301..350 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 351..400 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 401..450 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 451..500 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 501..550 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 551..600 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 601..650 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 651..700 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 701..750 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 751..800 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 801..850 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 851..900 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 901..950 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 951..1000 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count > 1000){
                speed = 110
            }
            binding.tvCount.text = count.toString()
            binding.ivEggRightDown.visibility = View.GONE
            binding.ivSmashEggRight.visibility = View.GONE
            binding.ivEggRightDown.translationX = 0f
            binding.ivEggRightDown.translationY = 0f
        }
    }
    @SuppressLint("SetTextI18n")
    private fun rightUpCollision(){
        val rect1 = Rect()
        binding.ivEggRightUp.getHitRect(rect1)
        val rect2 = Rect()
        binding.ivWolfRightUp.getHitRect(rect2)
        val collision = rect1.intersect(rect2)
        if (collision) {
            count++
            if(count in 51..100 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 101..150 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 151..200 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 201..250 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 251..300 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 301..350 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 351..400 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 401..450 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 451..500 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 501..550 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 551..600 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 601..650 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 651..700 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 701..750 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 751..800 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 801..850 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 851..900 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 901..950 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 951..1000 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count > 1000){
                speed = 110
            }
            binding.tvCount.text = count.toString()
            binding.ivEggRightUp.visibility = View.GONE
            binding.ivSmashEggRight.visibility = View.GONE
            binding.ivEggRightUp.translationX = 0f
            binding.ivEggRightUp.translationY = 0f
        }
    }
    @SuppressLint("SetTextI18n")
    private fun leftDownCollision(){
        val rect1 = Rect()
        binding.ivEggLeftDown.getHitRect(rect1)
        val rect2 = Rect()
        binding.ivWolfLeftDown.getHitRect(rect2)
        val collision = rect1.intersect(rect2)
        if (collision) {
            count++
            if(count in 51..100 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 101..150 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 151..200 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 201..250 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 251..300 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 301..350 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 351..400 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 401..450 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 451..500 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 501..550 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 551..600 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 601..650 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 651..700 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 701..750 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 751..800 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 801..850 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 851..900 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 901..950 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 951..1000 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count > 1000){
                speed = 110
            }
            binding.tvCount.text = count.toString()
            binding.ivEggLeftDown.visibility = View.GONE
            binding.ivSmashEggLeft.visibility = View.GONE
            binding.ivEggLeftDown.translationX = 0f
            binding.ivEggLeftDown.translationY = 0f
        }
    }
    @SuppressLint("SetTextI18n")
    private fun leftUpCollision(){
        val rect1 = Rect()
        binding.ivEggLeftUp.getHitRect(rect1)
        val rect2 = Rect()
        binding.ivWolfLeftUp.getHitRect(rect2)
        val collision = rect1.intersect(rect2)
        if (collision) {
            count++
            if(count in 51..100 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 101..150 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 151..200 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 201..250 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 251..300 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 301..350 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 351..400 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 401..450 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 451..500 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 501..550 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 551..600 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 601..650 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 651..700 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 701..750 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 751..800 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 801..850 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 851..900 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 901..950 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 951..1000 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count > 1000){
                speed = 110
            }
            binding.tvCount.text = count.toString()
            binding.ivEggLeftUp.visibility = View.GONE
            binding.ivSmashEggLeft.visibility = View.GONE
            binding.ivEggLeftUp.translationX = 0f
            binding.ivEggLeftUp.translationY = 0f

        }
    }
    @SuppressLint("SetTextI18n")
    private fun rightDownCollisionTwo(){
        val rect1 = Rect()
        binding.ivEggRightDownTwo.getHitRect(rect1)
        val rect2 = Rect()
        binding.ivWolfRightDown.getHitRect(rect2)
        val collision = rect1.intersect(rect2)
        if (collision) {
            count++
            if(count in 51..100 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 101..150 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 151..200 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 201..250 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 251..300 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 301..350 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 351..400 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 401..450 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 451..500 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 501..550 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 551..600 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 601..650 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 651..700 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 701..750 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 751..800 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 801..850 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 851..900 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 901..950 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 951..1000 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count > 1000){
                speed = 110
            }
            binding.tvCount.text = count.toString()
            binding.ivEggRightDownTwo.visibility = View.GONE
            binding.ivSmashEggRight.visibility = View.GONE
            binding.ivEggRightDownTwo.translationX = 0f
            binding.ivEggRightDownTwo.translationY = 0f
        }
    }
    @SuppressLint("SetTextI18n")
    private fun rightUpCollisionTwo(){
        val rect1 = Rect()
        binding.ivEggRightUpTwo.getHitRect(rect1)
        val rect2 = Rect()
        binding.ivWolfRightUp.getHitRect(rect2)
        val collision = rect1.intersect(rect2)
        if (collision) {
            count++
            if(count in 51..100 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 101..150 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 151..200 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 201..250 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 251..300 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 301..350 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 351..400 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 401..450 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 451..500 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 501..550 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 551..600 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 601..650 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 651..700 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 701..750 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 751..800 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 801..850 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 851..900 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 901..950 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 951..1000 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count > 1000){
                speed = 110
            }
            binding.tvCount.text = count.toString()
            binding.ivEggRightUpTwo.visibility = View.GONE
            binding.ivSmashEggRight.visibility = View.GONE
            binding.ivEggRightUpTwo.translationX = 0f
            binding.ivEggRightUpTwo.translationY = 0f
        }
    }
    @SuppressLint("SetTextI18n")
    private fun leftDownCollisionTwo(){
        val rect1 = Rect()
        binding.ivEggLeftDownTwo.getHitRect(rect1)
        val rect2 = Rect()
        binding.ivWolfLeftDown.getHitRect(rect2)
        val collision = rect1.intersect(rect2)
        if (collision) {
            count++
            if(count in 51..100 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 101..150 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 151..200 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 201..250 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 251..300 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 301..350 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 351..400 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 401..450 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 451..500 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 501..550 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 551..600 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 601..650 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 651..700 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 701..750 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 751..800 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 801..850 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 851..900 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 901..950 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 951..1000 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count > 1000){
                speed = 110
            }
            binding.tvCount.text = count.toString()
            binding.ivEggLeftDownTwo.visibility = View.GONE
            binding.ivSmashEggLeft.visibility = View.GONE
            binding.ivEggLeftDownTwo.translationX = 0f
            binding.ivEggLeftDownTwo.translationY = 0f
        }
    }
    @SuppressLint("SetTextI18n")
    private fun leftUpCollisionTwo(){
        val rect1 = Rect()
        binding.ivEggLeftUpTwo.getHitRect(rect1)
        val rect2 = Rect()
        binding.ivWolfLeftUp.getHitRect(rect2)
        val collision = rect1.intersect(rect2)
        if (collision) {
            count++
            if(count in 51..100 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 101..150 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 151..200 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 201..250 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 251..300 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 301..350 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 351..400 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 401..450 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 451..500 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 501..550 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 551..600 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 601..650 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 651..700 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 701..750 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 751..800 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 801..850 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 851..900 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 901..950 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count in 951..1000 && (count-1) % 50 == 0){
                if(binding.seekBar.progress == 20){
                    speed = 110
                } else{
                    speed -= 10
                }
            }
            if(count > 1000){
                speed = 110
            }
            binding.tvCount.text = count.toString()
            binding.ivEggLeftUpTwo.visibility = View.GONE
            binding.ivSmashEggLeft.visibility = View.GONE
            binding.ivEggLeftUpTwo.translationX = 0f
            binding.ivEggLeftUpTwo.translationY = 0f

        }
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