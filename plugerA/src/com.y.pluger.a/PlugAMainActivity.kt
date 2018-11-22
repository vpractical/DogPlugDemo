package com.y.plug.a

import android.os.Bundle
import com.y.pluger.a.BaseActivity
import com.y.pluger.a.PlugASecondActivity
import kotlinx.android.synthetic.main.pluga_activity_main.*

class PlugAMainActivity : BaseActivity() {

    override fun onCreate(savedBundle: Bundle?) {
        super.onCreate(savedBundle)
        mActivity?.setContentView(R.layout.pluga_activity_main)

        btnPlugaBack.setOnClickListener { mActivity?.finish() }

        btnPlugaNext.setOnClickListener( {PlugASecondActivity.start(mActivity)})

    }


}
