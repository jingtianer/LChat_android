package com.jingtian.androidexp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.jingtian.androidexp.Utils.msmHelper
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert

class exp3_fragment : Fragment(R.layout.fragment_exp3) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val send = view.findViewById<Button>(R.id.send_exp3)
        val msg = view.findViewById<EditText>(R.id.mesg_exp3)
        val phone = view.findViewById<EditText>(R.id.phone_exp3)

        send.onClick {
            if (phone.text.toString().trim().isNullOrEmpty() or (phone.text.toString().length < 11)) {
                alert {
                    title = "请输入正确的手机号"
                    positiveButton("确认"){

                    }
                }.show()
            }else if (msg.text.toString().trim().isNullOrEmpty()) {
                alert {
                    title = "请输入发送内容"
                    positiveButton("确认"){

                    }
                }.show()
            } else {
                if (activity!!.checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(Manifest.permission.SEND_SMS), 3)
                } else {
                    msmHelper.write(
                        this@exp3_fragment.activity!!,
                        phone.text.toString(),
                        msg.text.toString()
                    )
                    val intent = Intent()
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.action = "com.jingtian.androidExp.MESG_SENT"
                    intent.putExtra("mesg", msg.text.toString())
                    activity!!.sendBroadcast(intent)
                }


            }
        }
    }
}