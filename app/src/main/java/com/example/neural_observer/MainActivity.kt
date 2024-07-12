package com.example.neural_observer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // Variables used for record timer
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var startTime: Long = 0
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find record button, record timer texts
        val toggleBtnRecord: ToggleButton = findViewById(R.id.toggleButtonRecord)
        val textRecordTimer: TextView = findViewById(R.id.textViewTimer)

        // Make timer run when record starts
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                val elapsedMillis = System.currentTimeMillis() - startTime
                val seconds = (elapsedMillis / 1000) % 60
                textRecordTimer.text = String.format("%01d s", seconds)
                handler.postDelayed(this, 1000)
            }
        }

        // Listener tracking record button toggles
        toggleBtnRecord.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startTimer()
            } else {
                stopAndResetTimer()
            }
        }
    }

    private fun startTimer() {
        if (!isRunning) {
            startTime = System.currentTimeMillis()
            handler.post(runnable)
            isRunning = true
        }
    }

    private fun stopAndResetTimer() {
        if (isRunning) {
            handler.removeCallbacks(runnable)
            val textView: TextView = findViewById(R.id.textViewTimer)
            textView.text = "0 s"
            isRunning = false
        }
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }
}