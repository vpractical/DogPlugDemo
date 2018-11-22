package com.y.hookdemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

/**
 * hook点击事件启动一个未注册的activity
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnStartNoRegister.setOnClickListener { Log.e("MainActivity", "btnStartNoRegister.onClick()") }

        HookUtil.hookClick(btnStartNoRegister, View.OnClickListener { startNoRegister() })

    }

    private fun startNoRegister() {
        val intent = Intent(this, NoRegisterActivity::class.java)
        intent.putExtra("desc", "启动未注册activity的参数")
        startActivity(intent)
    }

    //    private Activity mActivity;
    //
    //    @Override
    //    protected void onCreate(Bundle savedInstanceState) {
    //        super.onCreate(savedInstanceState);
    //        setContentView(R.layout.activity_main);
    //        mActivity = this;
    //        Button btnStartNoRegister = findViewById(R.id.btnStartNoRegister);
    //        btnStartNoRegister.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View v) {
    //                Log.e("MainActivity", "btnStartNoRegister.onClick()");
    //            }
    //        });
    //
    //        hookClick(btnStartNoRegister);
    //        hookAms();
    //    }
    //
    //    private void startNoRegister() {
    //        Intent intent = new Intent(this, NoRegisterActivity.class);
    //        intent.putExtra("desc", "启动未注册activity的参数");
    //        startActivity(intent);
    //    }
    //
    //    /**
    //     * hookAms,将启动未注册activity的Intent替换为代理Activity的Intent
    //     */
    //    private void hookAms() {
    //        try {
    //            Class amClazz = Class.forName("android.app.ActivityManager");
    //            Field iamsField = amClazz.getDeclaredField("IActivityManagerSingleton");
    //            iamsField.setAccessible(true);
    //            Object iamsObj = iamsField.get(null);
    //
    //            Class insClazz = Class.forName("android.util.Singleton");
    //            Field insField = insClazz.getDeclaredField("mInstance");
    //            insField.setAccessible(true);
    //
    //            //值是IActivityManager的代理对象,IActivityManager是远程aidl接口
    //            Object iamObject = insField.get(iamsObj);
    //            Class iamClazz = Class.forName("android.app.IActivityManager");
    //            Object proxy = Proxy.newProxyInstance(getClassLoader(), new Class[]{iamClazz}, new HookInvocationHandler(iamObject));
    //
    //            insField.set(iamsObj, proxy);
    //
    //            restoreProxy();
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //    }
    //
    //    /**
    //     * hookAms后，在启动的消息又回到app后还原intent
    //     */
    //    private void restoreProxy() {
    //
    //        try {
    //            //获取ActivityThread对象
    //            Class atClazz = Class.forName("android.app.ActivityThread");
    //            Field atField = atClazz.getDeclaredField("sCurrentActivityThread");
    //            atField.setAccessible(true);
    //            Object atObject = atField.get(null);
    //
    //            //获取mH对象
    //            Field mHField = atClazz.getDeclaredField("mH");
    //            mHField.setAccessible(true);
    //            Class hClazz = Class.forName("android.os.Handler");
    //            Handler handler = (Handler) mHField.get(atObject);
    //
    //            //给mH的Callback赋值，拦截消息传递
    //            Field cbField = hClazz.getDeclaredField("mCallback");
    //            cbField.setAccessible(true);
    //            cbField.set(handler, new HookHandlerCallback(handler));
    //
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //    }
    //
    //    private class HookHandlerCallback implements Handler.Callback {
    //
    //        private Handler handler;
    //
    //        private HookHandlerCallback(Handler handler) {
    //            this.handler = handler;
    //        }
    //
    //        @Override
    //        public boolean handleMessage(Message msg) {
    //
    //            if (msg.what == 100) {
    //                try {
    //                    Log.e("Hook","AMS远程调用ActivityThread,在mH中拦截消息，还原intent");
    //                    Object obj = msg.obj;
    //                    Class clazz = obj.getClass();
    //                    Field intentField = clazz.getDeclaredField("intent");
    //                    intentField.setAccessible(true);
    //                    Intent proxyIntent = (Intent) intentField.get(obj);
    //                    if(proxyIntent.hasExtra("intent")){
    //                        Intent intent = proxyIntent.getParcelableExtra("intent");
    //                        intentField.set(obj,intent);
    //                    }
    //                } catch (Exception e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //            handler.handleMessage(msg);
    //            return true;
    //        }
    //    }
    //
    //    private class HookInvocationHandler implements InvocationHandler {
    //
    //        private Object iam;
    //
    //        private HookInvocationHandler(Object iam) {
    //            this.iam = iam;
    //        }
    //
    //        @Override
    //        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    //            if (method.getName().contains("startActivity")) {
    //                Log.e("Hook", "IActivityManager动态代理的invoke方法,伪装intent");
    //                for (int i = 0; i < args.length; i++) {
    //                    if (args[i] instanceof Intent) {
    //                        Intent intent = (Intent) args[i];
    //                        Intent proxyIntent = new Intent(mActivity, ProxyActivity.class);
    //                        proxyIntent.putExtra("intent", intent);
    //                        args[i] = proxyIntent;
    //                        break;
    //                    }
    //                }
    //            }
    //            return method.invoke(iam, args);
    //        }
    //    }
    //
    //
    //    /**
    //     * hook点击
    //     */
    //    private void hookClick(View view) {
    //        try {
    //            Class<?> clazz = Class.forName("android.view.View");
    //            Method getListenerInfo = clazz.getDeclaredMethod("getListenerInfo");
    //            getListenerInfo.setAccessible(true);
    //
    //            Object listenerInfo = getListenerInfo.invoke(view);
    //            Class<?> listenerInfoClazz = listenerInfo.getClass();
    //            Field clickListenerField = listenerInfoClazz.getDeclaredField("mOnClickListener");
    //            clickListenerField.setAccessible(true);
    //
    //            View.OnClickListener clickListenerObject = (View.OnClickListener) clickListenerField.get(listenerInfo);
    //            clickListenerField.set(listenerInfo, new HookOnClickListener(clickListenerObject));
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //    }
    //
    //    private class HookOnClickListener implements View.OnClickListener {
    //
    //        private View.OnClickListener onClickListener;
    //
    //        private HookOnClickListener(View.OnClickListener onClickListener) {
    //            this.onClickListener = onClickListener;
    //        }
    //
    //        @Override
    //        public void onClick(View v) {
    //            Log.e("MainActivity", "HookOnClickListener.onClick()");
    //            onClickListener.onClick(v);
    //            startNoRegister();
    //        }
    //    }


}
