package com.jingtian.androidexp

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jingtian.androidexp.Utils.DBHelper
import com.jingtian.androidexp.Utils.msmHelper
import org.jetbrains.anko.alert
import org.jetbrains.anko.customView
import org.jetbrains.anko.editText
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.support.v4.toast
import java.util.ArrayList

class exp2_fragment : Fragment(R.layout.fragment_exp2) {
    companion object {
        val dbName = "mesg.db"
    }
    class messgs(var name:String, var mesg:String) {}
    private var datas = arrayListOf<messgs>()
    class rv_holder(inflater: LayoutInflater, itemView: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_rv_exp2, itemView, false)) {
            fun bind(data:messgs) {
                itemView.findViewById<TextView>(R.id.tv_name_exp2).text = data.name
                itemView.findViewById<TextView>(R.id.tv_text_exp2).text = data.mesg
            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        toast(requestCode.toString() + ", " + grantResults[0].toString())
        if ((requestCode==1) and (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            datas.clear()
            load()
        }
    }


    fun load() {
        msmHelper.read(this.activity!!, datas)
        val db = DBHelper.get_db(this@exp2_fragment.context!!, dbName).readableDatabase
        val res = db.query("mesg", null, null, null, null, null, "name")
        while (res.moveToNext()){
            datas.add(messgs(
                res.getString(res.getColumnIndex("name")),
                res.getString(res.getColumnIndex("content"))
            ))
        }
        rv?.adapter?.notifyItemRangeChanged(0,datas.size)
    }
    var rv:RecyclerView? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv = view.findViewById(R.id.rv_exp2)

        rv!!.layoutManager = LinearLayoutManager(this.context)
        rv!!.adapter = object : RecyclerView.Adapter<rv_holder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): rv_holder {

                return rv_holder(activity!!.layoutInflater, parent)
            }

            override fun onBindViewHolder(holder: rv_holder, position: Int) {
                holder.bind(datas[position])
                holder.itemView.findViewById<Button>(R.id.bt_delete_exp2).setOnClickListener {
                    val db = DBHelper.get_db(holder.itemView.context!!, dbName).writableDatabase
                    db.execSQL("delete from mesg where name='${datas[position].name}' and content='${datas[position].mesg}'")
                    datas.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, datas.size-position)
                }

                holder.itemView.findViewById<Button>(R.id.bt_edit_exp2).setOnClickListener {
                    holder.itemView.context.alert {
                        title = "修改信息"
                        customView {
                            linearLayout {
                                orientation=LinearLayout.VERTICAL
                                val name = editText {
                                    hint = "名字"
                                }
                                val content = editText {
                                    hint = "内容"
                                }
                                positiveButton("确认") {
                                    val db = DBHelper.get_db(holder.itemView.context!!, dbName).writableDatabase
                                    db.execSQL("delete from mesg where name='${datas[position].name}' and content='${datas[position].mesg}'")
                                    db.execSQL(
                                        "insert into mesg(name, content) values('${name.text}', '${content.text}')"
                                    )
                                    datas[position].name=name.text.toString()
                                    datas[position].mesg=content.text.toString()
                                    notifyItemChanged(position)
                                }
                                negativeButton("取消") {
                                    it.dismiss()
                                }
                            }
                        }
                    }.show()

                }

            }

            override fun getItemCount(): Int = datas.size


        }
        var et_mesg = view.findViewById<EditText>(R.id.tv_mesg_exp2)
        view.findViewById<Button>(R.id.add_exp2).setOnClickListener {
            if (!et_mesg.text.isEmpty()) {

                val db = DBHelper.get_db(this@exp2_fragment.context!!, dbName)
                db.writableDatabase.execSQL(
                    "insert into mesg(name, content) values('tianer', '${et_mesg.text}')"
                )

                datas.add(0,messgs("tianer", et_mesg.text.toString()))
                et_mesg.setText("")
                rv!!.adapter!!.notifyItemInserted(0)
                rv!!.scrollToPosition(0)

            }
        }

        if ((activity!!.checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
        or (activity!!.checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)) {
            activity!!.requestPermissions(arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS), 1)
        } else {
            load()
        }
        //注册广播
        val filter = IntentFilter()
        filter.addAction("android.provider.Telephony.SMS_RECEIVED")
        filter.addAction("com.jingtian.androidExp.MESG_SENT")
        activity!!.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                val action = p1?.action
                if(action!!.equals("android.provider.Telephony.SMS_RECEIVED") or action!!.equals("com.jingtian.androidExp.MESG_SENT")) {
                    datas.clear()
                    load()
                }
            }

        }, filter)


    }
}