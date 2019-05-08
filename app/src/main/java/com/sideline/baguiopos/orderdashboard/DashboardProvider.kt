package com.sideline.baguiopos.orderdashboard

import android.support.v7.app.AppCompatActivity
import com.sideline.baguiopos.R
import com.sideline.baguiopos.apiservice.POSAPiService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class DashboardProvider(val activity: AppCompatActivity) : DashboardContract.Provider {
    override fun getBtnUpdateOrder(): Int = R.id.tvUpdateOrder


    override fun getOrders(result: (response: Any) -> Unit) {

        Observable.fromCallable {
            try {
                val response = POSAPiService.create().getOrders().execute()
                result(response)
            } catch (e: Exception) {
                e.printStackTrace()
                result(e)
            }
        }.subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe()
    }

}