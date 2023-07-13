package com.example.paylite_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.VideoView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class MainActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInButton = findViewById<SignInButton>(R.id.btn_login_google)
        signInButton.setOnClickListener {
            signOut()
            signIn()
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Login berhasil, lakukan tindakan yang sesuai
            val email = account?.email
            val fullName = account?.displayName
            val profilePicture = account?.photoUrl

            // Tampilkan informasi akun di log
            Log.d("Data Account", "Email: $email")
            Log.d("Data Account", "Full Name: $fullName")
            Log.d("Data Account", "Profile Picture: $profilePicture")

            // Lanjutkan dengan tindakan yang sesuai, seperti menyimpan informasi ke database atau menampilkan di UI

            // Contoh: Kirim informasi ke ActivityDashboard
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("fullName", fullName)
            intent.putExtra("profilePicture", profilePicture.toString())
            startActivity(intent)
            finish() // Opsional, jika Anda ingin menutup aktivitas saat kembali dari `DashboardActivity`
        } catch (e: ApiException) {
            // Login gagal, penanganan kesalahan
            Log.e("TAG", "signInResult:failed code=" + e.statusCode)
            runOnUiThread {
                Toast.makeText(this, "Login gagal. Pastikan kembali akun email anda!.", Toast.LENGTH_SHORT).show()
            }

        }
    }


    companion object
    fun signOut() {
        googleSignInClient.signOut()
            .addOnCompleteListener(this) {
                // Koneksi berhasil dilepas
            }
    }
//    private fun goToDashboardActivity() {
//        val intent = Intent(this, DashboardActivity::class.java)
//        startActivity(intent)
//        finish() // Opsional, jika Anda ingin menutup aktivitas saat kembali dari `ActivityDashboard`
//    }




}