package com.y.hookdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_noregister.*

class NoRegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noregister)

        if (intent != null && intent.hasExtra("desc")) {
            tvNoRegisterDesc.text = intent.getStringExtra("desc")
        } else {
            tvNoRegisterDesc.text = "无参数"
        }

    }
}
