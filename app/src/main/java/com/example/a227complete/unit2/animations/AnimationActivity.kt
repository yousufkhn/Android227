package com.example.a227complete.unit2.animations

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a227complete.R

class AnimationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_animation)


        val image = findViewById<ImageView>(R.id.imgView)
        val fadeBtn = findViewById<Button>(R.id.fadeBtn)
        val moveBtn = findViewById<Button>(R.id.moveBtn)
        val bounceBtn = findViewById<Button>(R.id.bounceBtn)

        moveBtn.setOnClickListener{
            val anim = AnimationUtils.loadAnimation(this,R.anim.move)
            image.startAnimation(anim)
        }

        bounceBtn.setOnClickListener{
            val anim = AnimationUtils.loadAnimation(this,R.anim.bounce)
            image.startAnimation(anim)
        }

        fadeBtn.setOnClickListener{
            val anim = AnimationUtils.loadAnimation(this,R.anim.fade_out)
            image.startAnimation(anim)
        }



    }
}