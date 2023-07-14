package com.example.paylite_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import com.example.paylite_mobile.helper.retrofit.ApiService
import com.example.paylite_mobile.helper.retrofit.RetrofitHelper
import com.example.paylite_mobile.helper.sharedPreference.Constant
import com.example.paylite_mobile.helper.sharedPreference.PreferenceHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 123
    private lateinit var sharedpref: PreferenceHelper
    private lateinit var api:ApiService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedpref = PreferenceHelper(this)
        api = RetrofitHelper.getInstance().create(ApiService::class.java)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInButton = findViewById<Button>(R.id.btn_login_google)
        signInButton.setOnClickListener {
            signOut()
            signIn()
        }

        // Teks "Terms of Service" dan "Privacy Policy"
        val termsOfServiceText = "Terms of Service"
        val privacyPolicyText = "Privacy Policy"
        val textViewTermsPrivacy = findViewById<TextView>(R.id.textView_terms_privacy)

        // Membuat teks HTML dengan link untuk "Terms of Service" dan "Privacy Policy"
        val combinedText = "By signing up, you agree to our <a href='https://paylite.co.id/tos'>$termsOfServiceText</a> and acknowledge that our <a href='https://paylite.co.id/policy'>$privacyPolicyText</a> applies to you."

        val spannableCombinedText = SpannableString(HtmlCompat.fromHtml(combinedText, HtmlCompat.FROM_HTML_MODE_LEGACY))

        // Warna teks link
        val linkColor = ContextCompat.getColor(this, R.color.paylite_original) // Mengambil warna biru dari resources

        // Menerapkan warna pada teks link
        val clickableSpans = spannableCombinedText.getSpans(0, spannableCombinedText.length, ClickableSpan::class.java)
        for (span in clickableSpans) {
            val start = spannableCombinedText.getSpanStart(span)
            val end = spannableCombinedText.getSpanEnd(span)
            spannableCombinedText.setSpan(ForegroundColorSpan(linkColor), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        textViewTermsPrivacy.text = spannableCombinedText
        textViewTermsPrivacy.movementMethod = LinkMovementMethod.getInstance()

    }

    override fun onStart() {
        super.onStart()
        if (sharedpref.getBoolean(Constant.PREF_IS_LOGIN)){
            Log.d("Ini Shared Pref", "Email:"+ sharedpref.getString(Constant.PREF_EMAIL))
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
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
            sharedpref.setBoolean(Constant.PREF_IS_LOGIN,true)
            sharedpref.setString(Constant.PREF_EMAIL,email.toString())
            sharedpref.setString(Constant.PREF_NAME,fullName.toString())
            sharedpref.setString(Constant.PREF_PICTURE,profilePicture.toString())
            callApiGlobal()
            Log.d("Data Account", "Email: $email")
            Log.d("Data Account", "Full Name: $fullName")
            Log.d("Data Account", "Profile Picture: $profilePicture")

            // Lanjutkan dengan tindakan yang sesuai, seperti menyimpan informasi ke database atau menampilkan di UI

            // Contoh: Kirim informasi ke ActivityDashboard
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("fullName", fullName)
            intent.putExtra("profilePicture", profilePicture.toString())

            runOnUiThread {
                Toast.makeText(this, "Login Berhasil!.", Toast.LENGTH_SHORT).show()
            }

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

    private fun callApiGlobal() {
        lifecycleScope.launch{
            val result = api.getApi()
            if(result.isSuccessful){
                Log.e("getApi test","res : "+result.body())
            }
        }
    }

    private fun signOut() {
        googleSignInClient.signOut()
            .addOnCompleteListener(this) {
                // Koneksi berhasil dilepas
            }
    }
}
