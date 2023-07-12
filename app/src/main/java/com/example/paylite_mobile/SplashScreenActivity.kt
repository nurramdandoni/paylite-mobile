package com.example.paylite_mobile

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.VideoView

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(1)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.setStatusBarColor(Color.TRANSPARENT)
        setContentView(R.layout.activity_splash_screen)

        var videoView: VideoView = findViewById(R.id.viewVideo)

        var path: String = "android.resource://com.example.paylite_mobile/" + R.raw.paylitevidio.toString()
        var uri: Uri = Uri.parse(path)
        videoView.setVideoURI(uri)
        videoView.setOnPreparedListener(MediaPlayer.OnPreparedListener {
            it.start()
        })

        videoView.setOnCompletionListener(MediaPlayer.OnCompletionListener {
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        })

    }
}