package com.y.plug

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Environment
import dalvik.system.DexClassLoader
import java.lang.reflect.Method

enum class Plugin {
    PLUGIN_A,
    PLUGIN_B
}

private lateinit var context: Context

lateinit var dexClassLoader: DexClassLoader
lateinit var resource: Resources

fun init(c: Context) {
    context = c.applicationContext
}

fun load(plug: Plugin): String {
    val path = path(plug)
    dexClassLoader(path)
    resource(path)
    return entry(path)
}

/**
 * 获取插件的入口activity名字
 */
private fun entry(path: String): String {
    val pm = context.packageManager
    val pi = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES)
    return pi.activities[0].name
}

private fun dexClassLoader(path: String) {
    val outDexFile = context.getDir("dex", Context.MODE_PRIVATE)
    dexClassLoader = DexClassLoader(path, outDexFile.absoluteFile.absolutePath, null, context.classLoader)
}

private fun resource(path: String) {
    val assetManager = AssetManager::class.java.newInstance()
    val addAssetPath: Method = AssetManager::class.java.getMethod("addAssetPath", String::class.java)
    addAssetPath.invoke(assetManager, path)
    resource = Resources(assetManager, context.resources.displayMetrics, context.resources.configuration)
}

/**
 * 获取插件路径
 */
private fun path(plug: Plugin): String {
    var path = "${Environment.getExternalStorageDirectory().path}/catdog/"
    path = when (plug) {
        Plugin.PLUGIN_A -> {
            path.plus("plugerA.apk")
        }
        Plugin.PLUGIN_B -> {
            path.plus("plugerB.apk")
        }
    }
    return path
}