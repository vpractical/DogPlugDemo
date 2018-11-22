package com.y.plug.rooter

import android.app.Activity
import android.os.Bundle

interface PluginInterface {

    fun attach(attachActivity: Activity)


    fun onCreate(savedBundle: Bundle?)

    fun onStart()

    fun onRestart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onSaveInstanceState(outState: Bundle?)

    fun onDestroy()

}
