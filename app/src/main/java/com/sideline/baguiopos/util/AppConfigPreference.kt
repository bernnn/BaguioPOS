package com.sideline.baguiopos.util

import android.content.Context
import com.sideline.baguiopos.apiservice.ContextProvider

class AppConfigPreference {

    internal val sharedPreference by lazy {
        ContextProvider.context.getSharedPreferences("pos_app_config", Context.MODE_PRIVATE)
    }


    companion object {
        val create by lazy { AppConfigPreference() }
        //
        val PREF_ORDER_LIST = "PREF_ORDER_LIST"
        val PREF_ORDER_LIST_CLEAR = "PREF_ORDER_LIST_CLEAR"
        val PREF_CASHIERNAME = "PREF_CASHIERNAME"
        val PREF_CASHIERID = "PREF_CASHIERID"
    }
}

fun AppConfigPreference.getPrefOrderList(): String {
    return sharedPreference.getString(AppConfigPreference.PREF_ORDER_LIST,"")
}

fun AppConfigPreference.setPrefOrderList(list : String) {
    sharedPreference.edit().putString(AppConfigPreference.PREF_ORDER_LIST, list).apply()
}

fun AppConfigPreference.isListClear(): Boolean {
    return sharedPreference.getBoolean(AppConfigPreference.PREF_ORDER_LIST_CLEAR,false)
}

fun AppConfigPreference.setListClear(clear : Boolean) {
    sharedPreference.edit().putBoolean(AppConfigPreference.PREF_ORDER_LIST_CLEAR, clear).apply()
}

fun AppConfigPreference.getCashierName(): String {
    return sharedPreference.getString(AppConfigPreference.PREF_CASHIERNAME, "")
}


fun AppConfigPreference.setCashierName(username : String) {
    sharedPreference.edit().putString(AppConfigPreference.PREF_CASHIERNAME, username).apply()
}

fun AppConfigPreference.getCashierID(): String {
    return sharedPreference.getString(AppConfigPreference.PREF_CASHIERID, "")
}


fun AppConfigPreference.setCashierID(cashierId : String) {
    sharedPreference.edit().putString(AppConfigPreference.PREF_CASHIERID, cashierId).apply()
}

fun AppConfigPreference.clear(){
    sharedPreference.edit().apply{
        remove(AppConfigPreference.PREF_CASHIERNAME)
        remove(AppConfigPreference.PREF_CASHIERID)
        apply()
    }
}


