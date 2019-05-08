package com.sideline.baguiopos.dashboard

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sideline.baguiopos.R
import com.sideline.baguiopos.apiservice.POSAPiService
import com.sideline.baguiopos.common.RequestCode
import com.sideline.baguiopos.orderdashboard.Dashboard
import com.sideline.baguiopos.payment.PaymentActivity
import com.sideline.baguiopos.util.OrderList
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.util.ArrayList

class DashboardProvider(val activity: AppCompatActivity) : DashboardContract.Provider {


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

    override fun gotoOrders() {
        val intent = Intent(activity,Dashboard::class.java)
        activity.startActivity(intent)
    }

    override fun getBtnViewOrder(): Int = R.id.tvViewOrder
    override fun getBtnPayment(): Int = R.id.tvPay

    override fun getProducts(result: (response: Any) -> Unit) {

        Observable.fromCallable {
            try {
                val response = POSAPiService.create().getProducts().execute()
                result(response)
            } catch (e: Exception) {
                e.printStackTrace()
                result(e)
            }
        }.subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe()
    }

    override fun showOrders(orderList: String): Int {
        val jsonArray = JSONObject(orderList)
        val jsonArrayCategory = jsonArray.getJSONArray("table")
        return jsonArrayCategory.length()
    }

    override fun nextScreen(total: String, orderList: ArrayList<OrderList>?) {
        val gson = Gson()
        val element = gson.toJson(orderList,object : TypeToken<ArrayList<OrderList>>() {}.type)
        val orders = JSONArray(element)

        val intent = Intent(activity, PaymentActivity::class.java)
        intent.putExtra("orderList", orders.toString())
        intent.putExtra("total",total)
        activity.startActivityForResult(intent, RequestCode.REQUEST_SUCCESS_CODE)
    }
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

    override fun getBtnLogout(): Int = R.id.action_logout

}