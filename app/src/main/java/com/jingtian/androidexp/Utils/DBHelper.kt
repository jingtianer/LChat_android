package com.jingtian.androidexp.Utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.jingtian.androidexp.exp2_fragment

class DBHelper(context:Context, name:String, private val createSQL:String): SQLiteOpenHelper(context, name, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    companion object {
        val dbs:HashMap<String, DBHelper> = HashMap()
        val db_creat:Map<String,String> = mutableMapOf(
                exp2_fragment.dbName to "create table mesg(id integer primary key autoincrement, name text, content text);"
            )

        fun get_db(context: Context, name: String):DBHelper {
            if (!dbs.containsKey(name)) {
                dbs.put(name, DBHelper(context, name, db_creat[name]!!))
                Log.d("sql", dbs.containsKey(name).toString())
            }
            return dbs.get(name)!!
        }
    }
}