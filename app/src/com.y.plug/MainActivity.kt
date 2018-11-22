package com.y.plug

import android.os.Bundle
import kotlinx.android.synthetic.main.app_activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_main)

        mActivity = this

        btnAppToPlugA.setOnClickListener { ProxyActivity.start(mActivity, load(Plugin.PLUGIN_A)) }

        btnAppToPlugB.setOnClickListener { ProxyActivity.start(mActivity, load(Plugin.PLUGIN_B)) }

    }

}
