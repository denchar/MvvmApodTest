package com.example.mvvmApodTest

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvmApodTest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onStart(){
        super.onStart()
        Log.d(
            this.localClassName.toString(),
            "onStart() "
        )
    }

    override fun onResume() {
        super.onResume()
        Log.d(
            this.localClassName.toString(),
            "onResume() "
        )
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(
            this.localClassName.toString(),
            "onRestart() "
        )
    }

    override fun onPause() {
        super.onPause()
        Log.d(
            this.localClassName.toString(),
            "onPause() "
        )
    }

    override fun onStop() {
        super.onStop()
        Log.d(
            this.localClassName.toString(),
            "onStop() "
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(
            this.localClassName.toString(),
            "onDestroy() "
        )
    }
}
