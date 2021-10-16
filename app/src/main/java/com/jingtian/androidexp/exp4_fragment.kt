package com.jingtian.androidexp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.marginLeft
import androidx.core.view.marginStart
import androidx.fragment.app.Fragment
import org.jetbrains.anko.customView
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.textView

class exp4_fragment : Fragment(R.layout.fragment_exp4) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //注册广播
        val filter = IntentFilter()
        filter.addAction("com.jingtian.androidExp.MESG_SENT")
        activity!!.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                val action = p1?.action
                if(action!!.equals("com.jingtian.androidExp.MESG_SENT")) {
                    alert {
                        title="接收到广播"
                        customView {
                            linearLayout {
                                orientation=LinearLayout.VERTICAL
                                this.setPadding(10,0,20,0)
                                textView {
                                    text = action
                                }
                                textView{
                                    text = "内容：" + p1.extras["mesg"]
                                }
                            }
                        }
                        positiveButton("关闭") {}
                    }.show()
                }
            }

        }, filter)
    }
}