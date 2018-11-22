package com.y.plug

import android.app.Application

class CatApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        init(this)
        init()
    }

    private fun init(){

    }
}