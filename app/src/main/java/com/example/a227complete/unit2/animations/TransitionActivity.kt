package com.example.a227complete.unit2.animations

import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a227complete.R

class TransitionActivity : AppCompatActivity() {
    private var isVisible = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_transition)

        val rootLayout = findViewById<LinearLayout>(R.id.main)
        val textView = findViewById<TextView>(R.id.transitionText)
        val button = findViewById<Button>(R.id.startTransition)

        button.setOnClickListener{
            val transition = Slide(Gravity.END)
            transition.duration = 600
            TransitionManager.beginDelayedTransition(rootLayout,transition)
            textView.visibility = if (isVisible) TextView.GONE else TextView.VISIBLE
            isVisible=!isVisible

        }

    }
}