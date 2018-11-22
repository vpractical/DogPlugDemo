package com.y.hookdemo

import android.app.Application
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * 拦截点击事件,并启动未注册的activity
 */
object HookUtil{

    private lateinit var mContext: Application

    private var mOnClickListener: View.OnClickListener? = null

    fun init(context: Application) {
        mContext = context
        hookAms()
        restore()
    }

    /**
     * hookAms,将启动未注册activity的Intent替换为代理Activity的Intent
     */
    private fun hookAms() {
        try {
            val amClazz = Class.forName("android.app.ActivityManager")
            val iamsField = amClazz.getDeclaredField("IActivityManagerSingleton")
            iamsField.isAccessible = true
            val iamsObj = iamsField.get(null)

            val insClazz = Class.forName("android.util.Singleton")
            val insField = insClazz.getDeclaredField("mInstance")
            insField.isAccessible = true

            //值是IActivityManager的代理对象,IActivityManager是远程aidl接口
            val iamObject = insField.get(iamsObj)
            val iamClazz = Class.forName("android.app.IActivityManager")
            val proxy = Proxy.newProxyInstance(mContext!!.classLoader, arrayOf(iamClazz), HookInvocationHandler(iamObject))

            insField.set(iamsObj, proxy)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * hookAms后，在启动的消息又回到app后还原intent
     */
    private fun restore() {
        try {
            //获取ActivityThread对象
            val atClazz = Class.forName("android.app.ActivityThread")
            val atField = atClazz.getDeclaredField("sCurrentActivityThread")
            atField.isAccessible = true
            val atObject = atField.get(null)
            //获取mH对象
            val mHField = atClazz.getDeclaredField("mH")
            mHField.isAccessible = true
            val hClazz = Class.forName("android.os.Handler")
            val handler = mHField.get(atObject) as Handler
            //给mH的Callback赋值，拦截消息传递
            val cbField = hClazz.getDeclaredField("mCallback")
            cbField.isAccessible = true
            cbField.set(handler, HookHandlerCallback(handler))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private class HookHandlerCallback(private val handler: Handler) : Handler.Callback {

        override fun handleMessage(msg: Message): Boolean {

            if (msg.what == 100) {
                try {
                    Log.e("Hook", "AMS远程调用ActivityThread,在mH中拦截消息，还原intent")
                    val obj = msg.obj
                    val clazz = obj.javaClass
                    val intentField = clazz.getDeclaredField("intent")
                    intentField.isAccessible = true
                    val proxyIntent = intentField.get(obj) as Intent
                    if (proxyIntent.hasExtra("intent")) {
                        val intent = proxyIntent.getParcelableExtra<Intent>("intent")
                        intentField.set(obj, intent)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            handler.handleMessage(msg)
            return true
        }
    }

    private class HookInvocationHandler(private val iam: Any): InvocationHandler {

        @Throws(Throwable::class)
        override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any? {
            if (method.name.contains("startActivity")) {
                Log.e("Hook", "IActivityManager动态代理的invoke方法,伪装intent")
                for (i in args.indices) {
                    if (args[i] is Intent) {
                        val intent = args[i] as Intent
                        val proxyIntent = Intent(mContext, ProxyActivity::class.java)
                        proxyIntent.putExtra("intent", intent)
                        args[i] = proxyIntent
                        break
                    }
                }
            }
            return if("void" == method.genericReturnType.typeName) Unit else method.invoke(iam, *args)
        }
    }

    /**
     * hook点击
     */
    fun hookClick(view: View, listener: View.OnClickListener) {
        try {
            this.mOnClickListener = listener
            val clazz = Class.forName("android.view.View")
            val getListenerInfo = clazz.getDeclaredMethod("getListenerInfo")
            getListenerInfo.isAccessible = true

            val listenerInfo = getListenerInfo.invoke(view)
            val listenerInfoClazz = listenerInfo.javaClass
            val clickListenerField = listenerInfoClazz.getDeclaredField("mOnClickListener")
            clickListenerField.isAccessible = true

            val clickListenerObject = clickListenerField.get(listenerInfo) as View.OnClickListener
            clickListenerField.set(listenerInfo, HookOnClickListener(clickListenerObject))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private class HookOnClickListener(private val onClickListener: View.OnClickListener) : View.OnClickListener {

        override fun onClick(v: View) {
            Log.e("MainActivity", "HookOnClickListener.onClick()")
            onClickListener.onClick(v)
            if (mOnClickListener != null) {
                mOnClickListener!!.onClick(v)
            }
        }
    }

}
