package com.y.hookdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class ProxyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("ProxyActivity", "代理Activity")
    }
}
