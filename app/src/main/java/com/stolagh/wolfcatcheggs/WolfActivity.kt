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
import androidx.core.content.ContextCompat
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
    private var corLUB : Job? = null
    private var corLDB : Job? = null
    private var corRUB : Job? = null
    private var corRDB : Job? = null
    private var corListMusic : Job? = null
    private var corMusic : Job? = null
    private var coroutineRightDownFall : Job? = null
    private var coroutineRightUpFall : Job? = null
    private var coroutineLeftUpFall : Job? = null
    private var coroutineLeftDownFall : Job? = null
    private var press = true
    private var pressListTracks = false
    private var pressMusic = false
    private var track = false
    private var currentRotation = 0f
    private var currentTranslationX = 0f
    private var currentTranslationY = 0f
    private var count = 0
    private var bestA = 0
    private var speed = 400
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var musicPlay: MediaPlayer
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWolfBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemUI()
        binding.cvListMusic.isEnabled = false
        binding.cvListMusic.alpha = 0.7f

        sharedPrefs = getSharedPreferences("SaveBestCountA", MODE_PRIVATE)
        val savebestA = sharedPrefs.getInt("savebestA", 0)
        if(savebestA != 0){
            binding.tvBestRecordA.text = "Game A : $savebestA"
        } else {
            binding.tvBestRecordA.text = "Game A : $savebestA"
        }
        binding.cvMusic.setOnClickListener {
//            corMusic = coroutineScope.launch {
//                binding.cvMusic.setCardBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.white))
//                delay(150)
//                binding.cvMusic.setCardBackgroundColor(ContextCompat.getColor(this@WolfActivity, R.color.yellowlight))
//                corMusic?.cancel()
  //      }
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
                    binding.cvListMusic.alpha = 0.7f
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
            binding.tvPlay.visibility = View.VISIBLE
            binding.btGameA.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.btGameB.setBackgroundColor(ContextCompat.getColor(this, R.color.yellowlight))
            binding.ivPlayB.visibility = View.GONE
        }
        binding.btGameB.setOnClickListener {
            binding.ivPlayB.visibility = View.VISIBLE
            binding.tvPlay.visibility = View.VISIBLE
            binding.btGameB.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.btGameA.setBackgroundColor(ContextCompat.getColor(this, R.color.yellowlight))
            binding.ivPlayA.visibility = View.GONE
        }
        binding.ivPlayA.setOnClickListener {
            if (press) {
                coroutine?.cancel()
                coroutineRightDownFall?.cancel()
                coroutineRightUpFall?.cancel()
                coroutineLeftDownFall?.cancel()
                coroutineLeftUpFall?.cancel()
                binding.ivEggRightDown.visibility = View.VISIBLE
                binding.ivEggRightDown.translationX = 0f
                binding.ivEggRightDown.translationY = 0f
                binding.ivEggRightUp.visibility = View.VISIBLE
                binding.ivEggRightUp.translationX = 0f
                binding.ivEggRightUp.translationY = 0f
                binding.ivEggLeftDown.visibility = View.VISIBLE
                binding.ivEggLeftDown.translationX = 0f
                binding.ivEggLeftDown.translationY = 0f
                binding.ivEggLeftUp.visibility = View.VISIBLE
                binding.ivEggLeftUp.translationX = 0f
                binding.ivEggLeftUp.translationY = 0f
                count = 0
                binding.tvCount.text = count.toString()
                binding.ivPlayA.setImageResource(R.drawable.baseline_stop_24)
                binding.tvPlay.text = "stop"
                press = false
                coroutine = coroutineScope.launch {
                    while (true) {
                        if(speed == 400){
                            delay((600..1900).random().toLong())
                            coroutineRightDownFall()
                            delay((500..1800).random().toLong())
                            coroutineLeftUpFall()
                            delay((800..2200).random().toLong())
                            coroutineRightUpFall()
                            delay((600..1500).random().toLong())
                            coroutineLeftDownFall()
                            delay(500)
                        }
                        if(speed == 380){
                            delay((900..1900).random().toLong())
                            coroutineRightDownFall()
                            delay((400..2100).random().toLong())
                            coroutineLeftDownFall()
                            delay((500..1800).random().toLong())
                            coroutineRightUpFall()
                            delay((600..1500).random().toLong())
                            coroutineLeftUpFall()
                            delay(500)
                        }
                        if(speed == 360){
                            delay((600..1800).random().toLong())
                            coroutineRightDownFall()
                            delay((500..2000).random().toLong())
                            coroutineLeftUpFall()
                            delay((550..1900).random().toLong())
                            coroutineRightUpFall()
                            delay((600..1600).random().toLong())
                            coroutineLeftDownFall()
                            delay(500)
                        }
                        if(speed == 340){
                            delay((700..1700).random().toLong())
                            coroutineRightDownFall()
                            delay((600..2000).random().toLong())
                            coroutineLeftDownFall()
                            delay((500..1800).random().toLong())
                            coroutineRightUpFall()
                            delay((650..1900).random().toLong())
                            coroutineLeftUpFall()
                            delay(500)
                        }
                        if(speed == 320){
                            delay((600..1600).random().toLong())
                            coroutineRightDownFall()
                            delay((500..1900).random().toLong())
                            coroutineLeftDownFall()
                            delay((600..2000).random().toLong())
                            coroutineRightUpFall()
                            delay((500..1800).random().toLong())
                            coroutineLeftUpFall()
                            delay(500)
                        }
                        if(speed == 300){
                            delay((500..1700).random().toLong())
                            coroutineRightDownFall()
                            delay((600..1800).random().toLong())
                            coroutineLeftDownFall()
                            delay((550..2000).random().toLong())
                            coroutineRightUpFall()
                            delay((500..1900).random().toLong())
                            coroutineLeftUpFall()
                            delay(500)
                        }
                        if(speed == 280){
                            delay((400..1400).random().toLong())
                            coroutineRightDownFall()
                            delay((450..1400).random().toLong())
                            coroutineLeftDownFall()
                            delay((500..1600).random().toLong())
                            coroutineRightUpFall()
                            delay((400..1500).random().toLong())
                            coroutineLeftUpFall()
                            delay(500)
                        }
                        if(speed == 260){
                            delay((300..1300).random().toLong())
                            coroutineRightDownFall()
                            delay((350..1400).random().toLong())
                            coroutineLeftDownFall()
                            delay((400..1500).random().toLong())
                            coroutineRightUpFall()
                            delay((350..1300).random().toLong())
                            coroutineLeftUpFall()
                            delay(500)
                        }
                        if(speed == 240){
                            delay((300..1300).random().toLong())
                            coroutineRightDownFall()
                            delay((400..1500).random().toLong())
                            coroutineLeftDownFall()
                            delay((450..1600).random().toLong())
                            coroutineRightUpFall()
                            delay((350..1400).random().toLong())
                            coroutineLeftUpFall()
                            delay(500)
                        }
                        if(speed == 220){
                            delay((300..1200).random().toLong())
                            coroutineRightDownFall()
                            delay((400..1700).random().toLong())
                            coroutineLeftDownFall()
                            delay((300..1400).random().toLong())
                            coroutineRightUpFall()
                            delay((300..1300).random().toLong())
                            coroutineLeftUpFall()
                            delay(500)
                        }
                    }
                }
            } else {
                if(count > bestA){
                    bestA = count
                    binding.tvBestRecordA.text = "Game A : $bestA"
                }
                sharedPrefs = getSharedPreferences("SaveBestCountA", MODE_PRIVATE)
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
                binding.ivEggRightDown.visibility = View.VISIBLE
                binding.ivEggRightDown.translationX = 0f
                binding.ivEggRightDown.translationY = 0f
                binding.ivEggRightUp.visibility = View.VISIBLE
                binding.ivEggRightUp.translationX = 0f
                binding.ivEggRightUp.translationY = 0f
                binding.ivEggLeftDown.visibility = View.VISIBLE
                binding.ivEggLeftDown.translationX = 0f
                binding.ivEggLeftDown.translationY = 0f
                binding.ivEggLeftUp.visibility = View.VISIBLE
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
        if (musicPlay.isPlaying) {
            musicPlay.stop()
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

    override fun onStop() {
        super.onStop()
//        sharedPrefs = getSharedPreferences("SaveMusic", MODE_PRIVATE)
//        val editor = sharedPrefs.edit()
//        editor?.putBoolean("savemusic", track)
//        editor?.putInt("int", mainCount)
//        editor?.apply()
    }
    override fun onDestroy() {
        super.onDestroy()
        stopMusic()
        releaseMusicPlay()
    }

    @SuppressLint("SetTextI18n")
    private fun coroutineRightDownFall(){
        coroutineRightDownFall?.cancel()
        coroutineRightDownFall = coroutineScope.launch {
            while (true){
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
                    delay(200)
                    eggToLeftDownSmashUp()
                    delay(200)
                    binding.ivEggRightDown.visibility = View.GONE
                    binding.ivSmashEggRight.visibility = View.VISIBLE
                    if(count > bestA){
                        bestA = count
                        binding.tvBestRecordA.text = "Game A : $bestA"
                    }
                    sharedPrefs = getSharedPreferences("SaveBestCountA", MODE_PRIVATE)
                    val editor = sharedPrefs.edit()
                    editor?.putInt("savebestA", bestA)
                    editor?.apply()
                    delay(500)
                    count = 0
                    binding.tvCount.text = count.toString()
                    binding.ivPlayA.setImageResource(R.drawable.baseline_play_arrow_24)
                    binding.tvPlay.text = " play"
                    press = true
                    binding.ivEggRightDown.visibility = View.VISIBLE
                    binding.ivSmashEggRight.visibility = View.GONE
                    binding.ivEggRightDown.translationX = 0f
                    binding.ivEggRightDown.translationY = 0f
                    coroutineRightUpFall?.cancel()
                    coroutineRightDownFall?.cancel()
                    coroutineLeftUpFall?.cancel()
                    coroutineLeftDownFall?.cancel()
                    coroutine?.cancel()
                }
                delay((6000..7000).random().toLong())
//                if(speed == 400){
//                    delay((6000..7000).random().toLong())
//                }
//                if(speed == 380){
//                    delay((6000..7000).random().toLong())
//                }
//                if(speed == 360){
//                    delay((6000..7000).random().toLong())
//                }
//                if(speed == 340){
//                    delay((6000..7000).random().toLong())
//                }
//                if(speed == 320){
//                    delay((6000..7000).random().toLong())
//                }
//                if(speed == 300){
//                    delay((6000..7000).random().toLong())
//                }
//                if(speed == 280){
//                    delay((6000..7000).random().toLong())
//                }
//                if(speed == 260){
//                    delay((6000..7000).random().toLong())
//                }
//                if(speed == 240){
//                    delay((6000..7000).random().toLong())
//                }
//                if(speed == 220){
//                    delay((5000..6000).random().toLong())
//                }
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun coroutineLeftDownFall(){
        coroutineLeftDownFall?.cancel()
        coroutineLeftDownFall = coroutineScope.launch {
            while (true){
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
                    delay(200)
                    eggToRightDownSmashUp()
                    delay(200)
                    binding.ivEggLeftDown.visibility = View.GONE
                    binding.ivSmashEggLeft.visibility = View.VISIBLE
                    if(count > bestA){
                        bestA = count
                        binding.tvBestRecordA.text = "Game A : $bestA"
                    }
                    sharedPrefs = getSharedPreferences("SaveBestCountA", MODE_PRIVATE)
                    val editor = sharedPrefs.edit()
                    editor?.putInt("savebestA", bestA)
                    editor?.apply()
                    delay(500)
                    count = 0
                    binding.tvCount.text = count.toString()
                    binding.ivPlayA.setImageResource(R.drawable.baseline_play_arrow_24)
                    binding.tvPlay.text = " play"
                    press = true
                    binding.ivEggLeftDown.visibility = View.VISIBLE
                    binding.ivSmashEggLeft.visibility = View.GONE
                    binding.ivEggLeftDown.translationX = 0f
                    binding.ivEggLeftDown.translationY = 0f
                    coroutineRightUpFall?.cancel()
                    coroutineRightDownFall?.cancel()
                    coroutineLeftUpFall?.cancel()
                    coroutineLeftDownFall?.cancel()
                    coroutine?.cancel()
                }
                delay((6000..7000).random().toLong())
//                if(speed == 400){
//                    delay((6000..7000).random().toLong())
//                }
//                if(speed == 380){
//                    delay((6000..7000).random().toLong())
//                }
//                if(speed == 360){
//                    delay((2100..2800).random().toLong())
//                }
//                if(speed == 340){
//                    delay((1900..2600).random().toLong())
//                }
//                if(speed == 320){
//                    delay((1700..2400).random().toLong())
//                }
//                if(speed == 300){
//                    delay((1500..2200).random().toLong())
//                }
//                if(speed == 280){
//                    delay((1300..2000).random().toLong())
//                }
//                if(speed == 260){
//                    delay((1100..1800).random().toLong())
//                }
//                if(speed == 240){
//                    delay((900..1600).random().toLong())
//                }
//                if(speed == 220){
//                    delay((700..1400).random().toLong())
//                }
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun coroutineRightUpFall(){
        coroutineRightUpFall?.cancel()
        coroutineRightUpFall = coroutineScope.launch {
            while (true){
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
                delay(speed.toLong())
                eggToLeftUp()
                if (binding.ivWolfRightUp.visibility == View.VISIBLE) {
                    rightUpCollision()
                } else {
                    delay(200)
                    eggToLeftUpSmashUp()
                    delay(200)
                    binding.ivEggRightUp.visibility = View.GONE
                    binding.ivSmashEggRight.visibility = View.VISIBLE
                    if(count > bestA){
                        bestA = count
                        binding.tvBestRecordA.text = "Game A : $bestA"
                    }
                    sharedPrefs = getSharedPreferences("SaveBestCountA", MODE_PRIVATE)
                    val editor = sharedPrefs.edit()
                    editor?.putInt("savebestA", bestA)
                    editor?.apply()
                    count = 0
                    binding.tvCount.text = count.toString()
                    binding.ivPlayA.setImageResource(R.drawable.baseline_play_arrow_24)
                    binding.tvPlay.text = " play"
                    press = true
                    binding.ivEggRightUp.visibility = View.VISIBLE
                    binding.ivSmashEggRight.visibility = View.GONE
                    binding.ivEggRightUp.translationX = 0f
                    binding.ivEggRightUp.translationY = 0f
                    coroutineRightUpFall?.cancel()
                    coroutineRightDownFall?.cancel()
                    coroutineLeftUpFall?.cancel()
                    coroutineLeftDownFall?.cancel()
                    coroutine?.cancel()
                }
                delay((6000..7000).random().toLong())
//                if(speed == 400){
//                    delay((6000..7000).random().toLong())
//                }
//                if(speed == 380){
//                    delay((6000..7000).random().toLong())
//                }
//                if(speed == 360){
//                    delay((3200..3900).random().toLong())
//                }
//                if(speed == 340){
//                    delay((3000..3700).random().toLong())
//                }
//                if(speed == 320){
//                    delay((2800..3500).random().toLong())
//                }
//                if(speed == 300){
//                    delay((2600..3300).random().toLong())
//                }
//                if(speed == 280){
//                    delay((2400..3100).random().toLong())
//                }
//                if(speed == 260){
//                    delay((2200..2900).random().toLong())
//                }
//                if(speed == 240){
//                    delay((2000..2700).random().toLong())
//                }
//                if(speed == 220){
//                    delay((1800..2500).random().toLong())
//                }
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun coroutineLeftUpFall(){
        coroutineLeftUpFall?.cancel()
        coroutineLeftUpFall = coroutineScope.launch {
            while (true){
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
                delay(speed.toLong())
                eggToRightUp()
                if (binding.ivWolfLeftUp.visibility == View.VISIBLE) {
                    leftUpCollision()
                } else {
                    delay(200)
                    eggToRightUpSmashUp()
                    delay(200)
                    binding.ivEggLeftUp.visibility = View.GONE
                    binding.ivSmashEggLeft.visibility = View.VISIBLE
                    if(count > bestA){
                        bestA = count
                        binding.tvBestRecordA.text = "Game A : $bestA"
                    }
                    sharedPrefs = getSharedPreferences("SaveBestCountA", MODE_PRIVATE)
                    val editor = sharedPrefs.edit()
                    editor?.putInt("savebestA", bestA)
                    editor?.apply()
                    delay(500)
                    count = 0
                    binding.tvCount.text = count.toString()
                    binding.ivPlayA.setImageResource(R.drawable.baseline_play_arrow_24)
                    binding.tvPlay.text = " play"
                    press = true
                    binding.ivEggLeftUp.visibility = View.VISIBLE
                    binding.ivSmashEggLeft.visibility = View.GONE
                    binding.ivEggLeftUp.translationX = 0f
                    binding.ivEggLeftUp.translationY = 0f
                    coroutineRightUpFall?.cancel()
                    coroutineRightDownFall?.cancel()
                    coroutineLeftUpFall?.cancel()
                    coroutineLeftDownFall?.cancel()
                    coroutine?.cancel()
                }
                delay((6000..7000).random().toLong())
//                if(speed == 400){
//                    delay((6000..7000).random().toLong())
//                }
//                if(speed == 380){
//                    delay((6000..7000).random().toLong())
//                }
//                if(speed == 360){
//                    delay((4100..4800).random().toLong())
//                }
//                if(speed == 340){
//                    delay((3900..4600).random().toLong())
//                }
//                if(speed == 320){
//                    delay((3700..4400).random().toLong())
//                }
//                if(speed == 300){
//                    delay((3500..4200).random().toLong())
//                }
//                if(speed == 280){
//                    delay((3300..4000).random().toLong())
//                }
//                if(speed == 260){
//                    delay((3100..3800).random().toLong())
//                }
//                if(speed == 240){
//                    delay((2900..3600).random().toLong())
//                }
//                if(speed == 220){
//                    delay((2700..3400).random().toLong())
//                }
            }
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
    private fun eggToLeftUps(){
        currentRotation = binding.ivEggRightUp.rotation
        currentTranslationX = binding.ivEggRightUp.translationX
        val translationXAnimator = ObjectAnimator.ofFloat(binding.ivEggRightUp, "translationX", currentTranslationX, (currentTranslationX - 25f))
        translationXAnimator.duration = 200
        translationXAnimator.start()

        val rotationAnimator = ObjectAnimator.ofFloat(binding.ivEggRightUp, "rotation", currentRotation, (currentRotation - 90f))
        rotationAnimator.duration = 200
        rotationAnimator.start()

        currentRotation = binding.ivEggRightUp.rotation
        currentTranslationX = binding.ivEggRightUp.translationX
    }
    @SuppressLint("Recycle")
    private fun eggToRight(){
        currentRotation = binding.ivEggLeftDown.rotation
        currentTranslationX = binding.ivEggLeftDown.translationX
        val translationXAnimator = ObjectAnimator.ofFloat(binding.ivEggLeftDown, "translationX", currentTranslationX, (currentTranslationX + 25f))
        translationXAnimator.duration = 200
        translationXAnimator.start()

        val rotationAnimator = ObjectAnimator.ofFloat(binding.ivEggLeftDown, "rotation", currentRotation, (currentRotation + 90f))
        rotationAnimator.duration = 200
        rotationAnimator.start()

        currentRotation = binding.ivEggLeftDown.rotation
        currentTranslationX = binding.ivEggLeftDown.translationX
    }
    private fun eggToRightUps(){
        currentRotation = binding.ivEggLeftUp.rotation
        currentTranslationX = binding.ivEggLeftUp.translationX
        val translationXAnimator = ObjectAnimator.ofFloat(binding.ivEggLeftUp, "translationX", currentTranslationX, (currentTranslationX + 25f))
        translationXAnimator.duration = 200
        translationXAnimator.start()

        val rotationAnimator = ObjectAnimator.ofFloat(binding.ivEggLeftUp, "rotation", currentRotation, (currentRotation + 90f))
        rotationAnimator.duration = 200
        rotationAnimator.start()

        currentRotation = binding.ivEggLeftUp.rotation
        currentTranslationX = binding.ivEggLeftUp.translationX
    }
    private fun eggToLeftDown(){
        currentRotation = binding.ivEggRightDown.rotation
        currentTranslationX = binding.ivEggRightDown.translationX
        currentTranslationY = binding.ivEggRightDown.translationY
        ObjectAnimator.ofFloat(binding.ivEggRightDown, "translationX", currentTranslationX, (currentTranslationX - 25f)).apply {
            duration = 200
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightDown, "translationY", currentTranslationY, (currentTranslationY + 15f)).apply {
            duration = 200
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightDown, "rotation", currentRotation, (currentRotation - 90f)).apply {
            duration = 200
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
            duration = 200
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightUp, "translationY", currentTranslationY, (currentTranslationY + 15f)).apply {
            duration = 200
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightUp, "rotation", currentRotation, (currentRotation - 90f)).apply {
            duration = 200
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
            duration = 200
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftDown, "translationY", currentTranslationY, (currentTranslationY + 15f)).apply {
            duration = 200
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftDown, "rotation", currentRotation, (currentRotation + 90f)).apply {
            duration = 200
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
            duration = 200
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftUp, "translationY", currentTranslationY, (currentTranslationY + 15f)).apply {
            duration = 200
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftUp, "rotation", currentRotation, (currentRotation + 90f)).apply {
            duration = 200
            start()
        }
        currentRotation = binding.ivEggLeftUp.rotation
        currentTranslationX = binding.ivEggLeftUp.translationX
        currentTranslationY = binding.ivEggLeftUp.translationY
    }
    private fun eggToLeftDownSmashUp(){
        currentRotation = binding.ivEggRightDown.rotation
        currentTranslationX = binding.ivEggRightDown.translationX
        currentTranslationY = binding.ivEggRightDown.translationY
        ObjectAnimator.ofFloat(binding.ivEggRightDown, "translationX", currentTranslationX, (currentTranslationX - 25f)).apply {
            duration = 200
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightDown, "translationY", currentTranslationY, (currentTranslationY + 100f)).apply{
            duration = 200
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightDown, "rotation", currentRotation, (currentRotation - 90f)).apply{
            duration = 200
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
            duration = 200
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightUp, "translationY", currentTranslationY, (currentTranslationY + 100f)).apply{
            duration = 200
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggRightUp, "rotation", currentRotation, (currentRotation - 90f)).apply{
            duration = 200
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
            duration = 200
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftDown, "translationY", currentTranslationY, (currentTranslationY + 100f)).apply {
            duration = 200
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftDown, "rotation", currentRotation, (currentRotation + 90f)).apply {
            duration = 200
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
            duration = 200
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftUp, "translationY", currentTranslationY, (currentTranslationY + 100f)).apply{
            duration = 200
            start()
        }
        ObjectAnimator.ofFloat(binding.ivEggLeftUp, "rotation", currentRotation, (currentRotation + 90f)).apply{
            duration = 200
            start()
        }
        currentRotation = binding.ivEggLeftUp.rotation
        currentTranslationX = binding.ivEggLeftUp.translationX
        currentTranslationY = binding.ivEggLeftUp.translationY
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
            if(count in 101..199){
                speed -= 20
                binding.tvSpeed.text = "Speed : 2"
            }
            if(count in 201..299){
                speed -= 20
                binding.tvSpeed.text = "Speed : 3"
            }
            if(count in 301..399){
                speed -= 20
                binding.tvSpeed.text = "Speed : 4"
            }
            if(count in 401..499){
                speed -= 20
                binding.tvSpeed.text = "Speed : 5"
            }
            if(count in 501..599){
                speed -= 20
                binding.tvSpeed.text = "Speed : 6"
            }
            if(count in 601..699){
                speed -= 20
                binding.tvSpeed.text = "Speed : 7"
            }
            if(count in 701..799){
                speed -= 20
                binding.tvSpeed.text = "Speed : 8"
            }
            if(count in 801..899){
                speed -= 20
                binding.tvSpeed.text = "Speed : 9"
            }
            if(count in 901..999){
                speed -= 20
                binding.tvSpeed.text = "Speed : 10"
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
            if(count in 101..199){
                speed -= 20
                binding.tvSpeed.text = "Speed : 2"
            }
            if(count in 201..299){
                speed -= 20
                binding.tvSpeed.text = "Speed : 3"
            }
            if(count in 301..399){
                speed -= 20
                binding.tvSpeed.text = "Speed : 4"
            }
            if(count in 401..499){
                speed -= 20
                binding.tvSpeed.text = "Speed : 5"
            }
            if(count in 501..599){
                speed -= 20
                binding.tvSpeed.text = "Speed : 6"
            }
            if(count in 601..699){
                speed -= 20
                binding.tvSpeed.text = "Speed : 7"
            }
            if(count in 701..799){
                speed -= 20
                binding.tvSpeed.text = "Speed : 8"
            }
            if(count in 801..899){
                speed -= 20
                binding.tvSpeed.text = "Speed : 9"
            }
            if(count in 901..999){
                speed -= 20
                binding.tvSpeed.text = "Speed : 10"
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
            if(count in 101..199){
                speed -= 20
                binding.tvSpeed.text = "Speed : 2"
            }
            if(count in 201..299){
                speed -= 20
                binding.tvSpeed.text = "Speed : 3"
            }
            if(count in 301..399){
                speed -= 20
                binding.tvSpeed.text = "Speed : 4"
            }
            if(count in 401..499){
                speed -= 20
                binding.tvSpeed.text = "Speed : 5"
            }
            if(count in 501..599){
                speed -= 20
                binding.tvSpeed.text = "Speed : 6"
            }
            if(count in 601..699){
                speed -= 20
                binding.tvSpeed.text = "Speed : 7"
            }
            if(count in 701..799){
                speed -= 20
                binding.tvSpeed.text = "Speed : 8"
            }
            if(count in 801..899){
                speed -= 20
                binding.tvSpeed.text = "Speed : 9"
            }
            if(count in 901..999){
                speed -= 20
                binding.tvSpeed.text = "Speed : 10"
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
            if(count in 101..199){
                speed -= 20
                binding.tvSpeed.text = "Speed : 2"
            }
            if(count in 201..299){
                speed -= 20
                binding.tvSpeed.text = "Speed : 3"
            }
            if(count in 301..399){
                speed -= 20
                binding.tvSpeed.text = "Speed : 4"
            }
            if(count in 401..499){
                speed -= 20
                binding.tvSpeed.text = "Speed : 5"
            }
            if(count in 501..599){
                speed -= 20
                binding.tvSpeed.text = "Speed : 6"
            }
            if(count in 601..699){
                speed -= 20
                binding.tvSpeed.text = "Speed : 7"
            }
            if(count in 701..799){
                speed -= 20
                binding.tvSpeed.text = "Speed : 8"
            }
            if(count in 801..899){
                speed -= 20
                binding.tvSpeed.text = "Speed : 9"
            }
            if(count in 901..999){
                speed -= 20
                binding.tvSpeed.text = "Speed : 10"
            }
            binding.tvCount.text = count.toString()
            binding.ivEggLeftUp.visibility = View.GONE
            binding.ivSmashEggLeft.visibility = View.GONE
            binding.ivEggLeftUp.translationX = 0f
            binding.ivEggLeftUp.translationY = 0f
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