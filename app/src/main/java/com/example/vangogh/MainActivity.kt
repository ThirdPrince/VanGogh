package com.example.vangogh

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vangogh.media.ui.activity.SelectMediaActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SelectMediaActivity.actionStart(this)
        finish()
    }
}