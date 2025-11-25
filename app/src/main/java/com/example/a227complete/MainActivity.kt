package com.example.a227complete

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a227complete.unit1.FirebaseAuthActivity
import com.example.a227complete.unit1.FirebaseUiActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()



        findViewById<Button>(R.id.logoutBtn).setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, FirebaseAuthActivity::class.java))
            finish()
        }

        findViewById<Button>(R.id.firebaseLogoutBtn).setOnClickListener{
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    startActivity(Intent(this, FirebaseUiActivity::class.java))
                    finish()
                }

        }

        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                startActivity(Intent(this, FirebaseUiActivity::class.java))
                finish()
            }

    }
}