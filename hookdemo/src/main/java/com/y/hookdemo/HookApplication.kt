package com.y.hookdemo

import android.app.Application

class HookApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        HookUtil.init(this)

    }

}
