package com.sideline.baguiopos.common

import android.app.Application
import android.content.Context

class POSApplication : Application() {

    private var appContext: Context? = null
    var instance: POSApplication? = null

    override fun onCreate() {
        super.onCreate()
        appContext = getAppContext()
        instance = this

        /* If you has other classes that need context object to initialize when application is created,
         you can use the appContext here to process. */
    }


    fun getAppContext(): Context? {
        return appContext
    }

}