package com.sideline.baguiopos.util

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.sideline.baguiopos.BuildConfig


class RemoteConfig{


    companion object {
        fun getString(value : String) : String {


            val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

            FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(0)
            mFirebaseRemoteConfig.activateFetched()

//            val configSettings = FirebaseRemoteConfigSettings.Builder()
//                    .setDeveloperModeEnabled(BuildConfig.DEBUG)
//                    .build()
//            mFirebaseRemoteConfig.setConfigSettings(configSettings)

            val fetchedValue = mFirebaseRemoteConfig.getString(value)

            var cacheExpiration: Long = 3600 // 1 hour in seconds.
            // If in developer mode cacheExpiration is set to 0 so each fetch will retrieve values from
            // the server.
            if (mFirebaseRemoteConfig.info.configSettings.isDeveloperModeEnabled) {
                cacheExpiration = 0
            }


            return fetchedValue
        }
    }
}