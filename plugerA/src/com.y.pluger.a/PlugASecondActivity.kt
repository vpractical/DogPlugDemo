package com.y.pluger.a

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.y.plug.a.R
import kotlinx.android.synthetic.main.pluga_activity_second.*

class PlugASecondActivity: BaseActivity() {

    companion object {
        fun start(context: Context?){
            context?.startActivity(Intent(context,PlugASecondActivity::class.java))
        }
    }


    override fun onCreate(savedBundle: Bundle?) {
        super.onCreate(savedBundle)
        setContentView(R.layout.pluga_activity_second)
        btnPlugaSecondBack.setOnClickListener { mActivity!!.finish() }
        btnPlugaSecondNext.setOnClickListener( {PlugASecondActivity.start(mActivity!!)})
    }

}