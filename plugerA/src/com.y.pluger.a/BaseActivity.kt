package com.y.pluger.a

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import com.y.plug.rooter.PluginInterface

open class BaseActivity : AppCompatActivity(), PluginInterface {

    protected var mActivity: Activity? = null

    override fun attach(activity: Activity) {
        this.mActivity = activity
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        if(mActivity == null){
            super.onSaveInstanceState(outState)
        }else{

        }
    }

    override fun getClassLoader(): ClassLoader {
        return if (mActivity == null) {
            super.getClassLoader()
        } else {
            mActivity!!.classLoader
        }
    }

    override fun getLayoutInflater(): LayoutInflater {
        return if (mActivity == null) {
            super.getLayoutInflater()
        } else {
            mActivity!!.layoutInflater
        }
    }


    override fun getWindowManager(): WindowManager {
        return if (mActivity == null) {
            super.getWindowManager()
        } else {
            mActivity!!.windowManager
        }
    }

    override fun getWindow(): Window {
        return if (mActivity == null) {
            super.getWindow()
        } else {
            mActivity!!.window
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (mActivity == null) {
            super.onCreate(savedInstanceState)
        }
    }

    override fun onStart() {
        if (mActivity == null) {
            super.onStart()
        }
    }

    override fun onDestroy() {
        if (mActivity == null) {
            super.onDestroy()
        }
    }

    override fun onPause() {
        if (mActivity == null) {
            super.onPause()
        }
    }

    override fun onResume() {
        if (mActivity == null) {
            super.onResume()
        }
    }

    override fun onRestart() {
        if (mActivity == null) {
            super.onRestart()
        }
    }

    override fun onStop() {
        if (mActivity == null) {
            super.onStop()
        }
    }
}
