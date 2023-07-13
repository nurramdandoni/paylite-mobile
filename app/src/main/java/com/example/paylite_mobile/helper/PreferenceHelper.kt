package com.example.paylite_mobile.helper

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(context: Context) {
    private val PREF_NAME = "sharedPreferencePaylite"
    private val sharedPreferences : SharedPreferences

    val editor : SharedPreferences.Editor

    init {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun setString(key:String, value: String){
        editor.putString(key, value)
            .apply()
    }
    fun setBoolean(key:String, value: Boolean){
        editor.putBoolean(key, value)
            .apply()
    }
    fun getString(key:String): String?{
        return sharedPreferences.getString(key, null)
    }
    fun getBoolean(key:String): Boolean{
        return sharedPreferences.getBoolean(key, false)
    }

    fun clear(){
        editor.clear()
            .apply()
    }
}