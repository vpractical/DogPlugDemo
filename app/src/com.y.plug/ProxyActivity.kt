package com.y.plug

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import com.y.plug.rooter.PluginInterface

/**
 * 代理activity，所有跳转插件，都跳转这个代理，代理将生命周期传递给插件activity
 */
class ProxyActivity: BaseActivity() {

    companion object {
        fun start(activity: Activity,entryName:String){
            val intent = Intent(activity, ProxyActivity::class.java)
            intent.putExtra("entryName",entryName)
            activity.startActivity(intent)
        }
    }

    /**
     * 传递生命周期上下文的接口对象
     */
    private lateinit var iPlugin: PluginInterface
    private lateinit var entryName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        entryName = intent.getStringExtra("entryName")

        val activityClass = classLoader.loadClass(entryName)
        val constructor = activityClass.getConstructor()
        val instance = constructor.newInstance()

        iPlugin = instance as PluginInterface
        iPlugin.attach(this)
        iPlugin.onCreate(savedInstanceState)
    }

    override fun getResources(): Resources {
        return resource
    }

    override fun getClassLoader(): ClassLoader {
        return dexClassLoader
    }

    override fun onRestart() {
        super.onRestart()
        iPlugin.onRestart()
    }

    override fun onStart() {
        super.onStart()
        iPlugin.onStart()
    }

    override fun onResume() {
        super.onResume()
        iPlugin.onResume()
    }

    override fun onPause() {
        super.onPause()
        iPlugin.onPause()
    }

    override fun onStop() {
        super.onStop()
        iPlugin.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        iPlugin.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        iPlugin.onSaveInstanceState(outState)
    }

}