package com.example.paylite_mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.VideoView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}