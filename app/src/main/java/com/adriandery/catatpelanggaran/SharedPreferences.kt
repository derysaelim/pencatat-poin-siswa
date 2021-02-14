package com.adriandery.catatpelanggaran

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SharedPreferences {

    companion object {
        const val DATA_LOGIN = "status_login"
        const val DATA_AS = "as"
        const val NIP = "nip"

        private fun getSharedPreferences(context: Context): SharedPreferences? {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }

        @SuppressLint("CommitPrefEdits")
        fun setDataAs(context: Context, data: String) {
            val editor: SharedPreferences.Editor = getSharedPreferences(context)!!.edit()
            editor.putString(DATA_AS, data)
            editor.apply()
        }

        @SuppressLint("CommitPrefEdits")
        fun setNIP(context: Context, nip: String) {
            val editor: SharedPreferences.Editor = getSharedPreferences(context)!!.edit()
            editor.putString(NIP, nip)
            editor.apply()
        }

        fun getDataAs(context: Context): String? {
            return getSharedPreferences(context)!!.getString(DATA_AS, "")
        }

        fun getNIP(context: Context): String? {
            return getSharedPreferences(context)!!.getString(NIP, "")
        }

        fun setDataLogin(context: Context, status: Boolean) {
            val editor: SharedPreferences.Editor = getSharedPreferences(context)!!.edit()
            editor.putBoolean(DATA_LOGIN, status)
            editor.apply()
        }

        fun getDataLogin(context: Context): Boolean {
            return getSharedPreferences(context)!!.getBoolean(DATA_LOGIN, false)
        }

        @SuppressLint("CommitPrefEdits")
        fun clearData(context: Context) {
            val editor: SharedPreferences.Editor = getSharedPreferences(context)!!.edit()
            editor.remove(DATA_AS)
            editor.remove(DATA_LOGIN)
            editor.remove(NIP)
            editor.apply()
        }
    }
}