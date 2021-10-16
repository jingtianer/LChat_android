package com.jingtian.androidexp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class exp1_fragment : Fragment(R.layout.fragment_exp1) {
    private var datas = arrayListOf<dialog_data>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fun update(text: String, orient: Int) {
            datas.add(dialog_data(text, orient))
        }
//        Toast.makeText(this.activity,
//            (activity == null).toString(), Toast.LENGTH_SHORT).show()
        var rv = view.findViewById<RecyclerView>(R.id.rv_exp1)
        rv.layoutManager = LinearLayoutManager(this.context)
        rv.adapter = object : RecyclerView.Adapter<rv_holder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): rv_holder {
                return rv_holder(activity!!.layoutInflater, parent)
            }

            override fun onBindViewHolder(holder: rv_holder, position: Int) {
                holder.bind(datas[position])
            }

            override fun getItemCount(): Int = datas.size


        }
        var et_mesg = view.findViewById<EditText>(R.id.tv_mesg)
        view.findViewById<Button>(R.id.sent_left).setOnClickListener {
            if (!et_mesg.text.isEmpty()) {
                update(et_mesg.text.toString(), dialog_data.left)
                et_mesg.setText("")
                rv.adapter!!.notifyItemInserted(rv.adapter!!.itemCount - 1)
                
            }
        }
        view.findViewById<Button>(R.id.send_right).setOnClickListener {
            if (!et_mesg.text.isEmpty()) {
                update(et_mesg.text.toString(), dialog_data.right)
                et_mesg.setText("")
                rv.adapter!!.notifyItemInserted(rv.adapter!!.itemCount - 1)

            }
        }
    }
    class rv_holder(inflater: LayoutInflater, itemView: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_rv_exp1, itemView, false)) {
            fun bind(data: dialog_data) {

                var textview = this.itemView.findViewById<TextView>(R.id.tv_item_exp1)
                textview.text = data.text

                var dab = BitmapFactory.decodeResource(itemView.resources, R.drawable.dog)
                var mat = Matrix()
                mat.postScale(0.2f,0.2f)
                var pic = Bitmap.createBitmap(dab, 0,0,dab.width, dab.height, mat,true)
                this.itemView.findViewById<ImageView>(R.id.iv_dog).setImageBitmap(pic)


                dab = BitmapFactory.decodeResource(itemView.resources, R.drawable.cat)
                mat = Matrix()
                mat.postScale(0.2f,0.2f)
                pic = Bitmap.createBitmap(dab, 0,0,dab.width, dab.height, mat,true)
                itemView.findViewById<ImageView>(R.id.iv_cat).setImageBitmap(pic)

                if (data.orient == dialog_data.left) {
                    itemView.findViewById<LinearLayout>(R.id.item_exp1_ll).gravity = Gravity.START
//                    (itemView.findViewById<LinearLayout>(R.id.item_exp1_ll).layoutParams as LinearLayout.LayoutParams).gravity = Gravity.START
                    this.itemView.findViewById<ImageView>(R.id.iv_cat).visibility = View.GONE
                    textview.setBackgroundResource(R.drawable.dialog_left)
                } else if (data.orient ==  dialog_data.right) {
                    itemView.findViewById<LinearLayout>(R.id.item_exp1_ll).gravity = Gravity.END
//                    (itemView.findViewById<LinearLayout>(R.id.item_exp1_ll).layoutParams as LinearLayout.LayoutParams).gravity = Gravity.END
                    this.itemView.findViewById<ImageView>(R.id.iv_dog).visibility = View.GONE
                    textview.setBackgroundResource(R.drawable.dialog_right)
                } else {
                    this.itemView.findViewById<ImageView>(R.id.iv_cat).visibility = View.GONE
                    this.itemView.findViewById<ImageView>(R.id.iv_dog).visibility = View.GONE
                }
            }
    }
    class dialog_data(var text: String, var orient: Int) {
        companion object {
            val left = 1
            val right = 0
        }
    }
}