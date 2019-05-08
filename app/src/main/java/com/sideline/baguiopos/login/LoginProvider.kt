package com.sideline.baguiopos.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sideline.baguiopos.BuildConfig
import com.sideline.baguiopos.R
import com.sideline.baguiopos.apiservice.POSAPiService
import com.sideline.baguiopos.dashboard.Dashboard
import com.sideline.baguiopos.util.AppConfigPreference
import com.sideline.baguiopos.util.OrderList
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.login_activity_layout.view.*
import java.io.File
import java.lang.Exception
import java.util.ArrayList

class LoginProvider(val activity: AppCompatActivity): LoginContract.Provider {
    override fun saveUserName(username: String) {
        AppConfigPreference
    }

    override fun gotoDashboard() {
        val intent = Intent(activity, Dashboard::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

    override fun signIn(payload: HashMap<String, Any>, result: (response: Any) -> Unit) {
        Observable.fromCallable {
            try {
                val response = POSAPiService.create().sigIn(payload).execute()
                result(response)
            }catch (e: Exception) {
                e.printStackTrace()
                result(e)
            }
        }.subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe()}

    override fun getBtnSignin(): Int = R.id.tvLogin

    override fun fetchConfig() :  String{

        val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        mFirebaseRemoteConfig.setConfigSettings(configSettings)

//      val defaultConfig = Gson().toJson(File("app_config.json"),object : TypeToken<Map<String, Any>>() {}.type)

        mFirebaseRemoteConfig.setDefaults(R.xml.def )

        var cacheExpiration =5.toLong()
        if (mFirebaseRemoteConfig.info.configSettings.isDeveloperModeEnabled) {
            cacheExpiration = 30.toLong()
        }

        var devices = ""

        mFirebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                try {
                    mFirebaseRemoteConfig.activateFetched()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            devices = mFirebaseRemoteConfig.getString("allowed_device")
            Log.e("allowed_device",devices)
            Log.e("allowed_device",devices)
            Log.e("allowed_device",devices)


        }
        return devices
    }


}