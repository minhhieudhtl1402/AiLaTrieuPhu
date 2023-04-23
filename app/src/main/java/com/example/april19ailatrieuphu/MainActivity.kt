package com.example.april19ailatrieuphu

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(){
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var btnPlay: Button
    private lateinit var ivCircle: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MediaManager.start(this, R.raw.bgmusic)
        btnPlay = findViewById(R.id.btn_play)
        ivCircle = findViewById(R.id.iv_circle)

        val animation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.spin_around)
        ivCircle.animation = animation
        ivCircle.startAnimation(animation)

        btnPlay.setOnClickListener {
            val intent = Intent(this, PlayActivity::class.java)
            MediaManager.stop()
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("vmhieu", "On destroy call")
    }

    override fun onResume() {
        super.onResume()
        Log.d("vmhieu", "On resume call")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("vmhieu", "On restart call")
        MediaManager.start(this, R.raw.bgmusic)
//
    }


}