package com.example.paylite_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.paylite_mobile.helper.Constant
import com.example.paylite_mobile.helper.PreferenceHelper
import com.google.android.gms.auth.api.signin.GoogleSignInClient

class DashboardActivity : AppCompatActivity() {
    private lateinit var sharedpref: PreferenceHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        sharedpref = PreferenceHelper(this)

        val name = intent.getStringExtra("fullName")
        val email = intent.getStringExtra("email")
        val profilePictureUrl = intent.getStringExtra("profilePicture")

        val imageViewProfile = findViewById<ImageView>(R.id.imageView_profile)
        val textViewName = findViewById<TextView>(R.id.textView_name)
        val textViewEmail = findViewById<TextView>(R.id.textView_email)
        if(intent.getStringExtra("fullName") != null){
            textViewName.text = name
            textViewEmail.text = email

            // Tampilkan foto profil menggunakan library pihak ketiga, seperti Glide atau Picasso
            Glide.with(this)
                .load(profilePictureUrl)
                .placeholder(R.drawable.default_profile_image_foreground)
                .error(R.drawable.default_profile_image_foreground)
                .into(imageViewProfile)
        }else{
            textViewName.text = sharedpref.getString(Constant.PREF_NAME)
            textViewEmail.text = sharedpref.getString(Constant.PREF_EMAIL)

            // Tampilkan foto profil menggunakan library pihak ketiga, seperti Glide atau Picasso
            Glide.with(this)
                .load(sharedpref.getString(Constant.PREF_PICTURE))
                .placeholder(R.drawable.default_profile_image_foreground)
                .error(R.drawable.default_profile_image_foreground)
                .into(imageViewProfile)
        }

    }

    fun logout(view: View) {
        sharedpref.clear()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtra("signOut", true)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}