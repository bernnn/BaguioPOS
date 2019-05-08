package com.sideline.baguiopos.payment

import android.support.v7.app.AppCompatActivity
import com.sideline.baguiopos.R
import com.sideline.baguiopos.apiservice.POSAPiService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class PaymentProvider(val activity: AppCompatActivity): PaymentContract.Provider{
    override fun gotoReceipt(total: String, orderList: String) {


    }

    override fun getBtnBack(): Int = R.id.btnBack

    override fun getBtnCash(): Int = R.id.tvCash

    override fun getBtnValidate(): Int = R.id.btnValidate

    override fun getTotalOrderAmount(): String = activity.intent.getStringExtra("total")

    override fun getOrderList(): String  = activity.intent.getStringExtra("orderList")

    override fun submitOrder(payload: HashMap<String, Any>, result: (response: Any) -> Unit) {


        Observable.fromCallable {
            try {
                val response = POSAPiService.create().submitOrder(payload).execute()
//                val response = POSAPiService.create()
//                        .submitOrder(payload.get("orderList").toString(),
//                                payload.get("orderTotal").toString(),
//                                payload.get("orderDate").toString(),
//                                payload.get("amountTendered").toString(),
//                                payload.get("change").toString(),
//                                payload.get("cashierId").toString()
//                        ).execute()
                result(response)
            }catch (e: Exception) {
                e.printStackTrace()
                result(e)
            }
        }.subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe()
    }

}