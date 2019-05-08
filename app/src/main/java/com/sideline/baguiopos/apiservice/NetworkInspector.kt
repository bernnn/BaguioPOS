package com.sideline.baguiopos.apiservice

import com.readystatesoftware.chuck.ChuckInterceptor
import com.sideline.baguiopos.BuildConfig

class NetworkInspector {
    companion object {
        fun getChuckNetworkInspector(): ChuckInterceptor{
            return ChuckInterceptor(ContextProvider.context)
                    .showNotification(BuildConfig.DEBUG)
        }
    }
}