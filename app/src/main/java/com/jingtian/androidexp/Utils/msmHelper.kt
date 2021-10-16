package com.jingtian.androidexp.Utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteException
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.telephony.SmsManager
import android.util.Log
import com.jingtian.androidexp.exp2_fragment
import java.sql.Date

class msmHelper {
    companion object {
        val SMS_URI_ALL = "content://sms/" // 所有短信

        fun read(context: Activity, datas: ArrayList<exp2_fragment.messgs>) {
            //请求权限

            try {
                val uri = Uri.parse(SMS_URI_ALL);
                var projection = arrayOf(
                    "_id", "address", "person",
                    "body", "date", "type"
                );
                var cur = context.getContentResolver().query(
                    uri, projection, null,
                    null, "date desc"
                )!! // 获取手机内部短信
                // 获取短信中最新的未读短信
                // Cursor cur = getContentResolver().query(uri, projection,
                // "read = ?", new String[]{"0"}, "date desc");
                if (cur.moveToFirst()) {
                    val index_Address = cur.getColumnIndex("address");
                    val index_Body = cur.getColumnIndex("body");

                    do {
                        val strAddress = cur.getString(index_Address);
                        val strbody = cur.getString(index_Body);



                        datas.add(exp2_fragment.messgs(strAddress, strbody))
                    } while (cur.moveToNext());

                    if (!cur.isClosed()) {
                        cur.close()
                    }
                }


            } catch (ex: SQLiteException) {
                Log.d("SQLiteException in getSmsInPhone", ex.message.toString());
            }

        }

        fun write(context: Activity, phone:String, msg:String) {
            //请求权限


            val manager = SmsManager.getDefault()
            val strs = manager.divideMessage(msg)
            for (str in strs) {
                manager.sendTextMessage(phone, null, str, null, null)
            }
        }
    }
}