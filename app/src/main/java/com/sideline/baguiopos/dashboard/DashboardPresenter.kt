package com.sideline.baguiopos.dashboard

import android.util.MalformedJsonException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sideline.baguiopos.util.AlertDialogX
import com.sideline.baguiopos.util.AppConfigPreference
import com.sideline.baguiopos.util.OrderList
import com.sideline.baguiopos.util.getCashierID
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Response
import java.io.IOException
import java.util.*

class DashboardPresenter(val view: DashboardContract.View, val provider: DashboardContract.Provider): DashboardContract.Presenter {


    override fun addOrder() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    init {
        view.presenter = this
    }

    var orderCount = 0


    override fun onClick(id: Int?) {
        when(id){
            provider.getBtnPayment() -> validateNextScreen(view.getTotal())
            provider.getBtnViewOrder() -> validateViewOrder()
            provider.getBtnLogout() -> view.logoutDialog(msg = "Are you sure you want to log out?")
        }

    }

    fun validateViewOrder(){

        if(orderCount > 0){
            provider.gotoOrders()
        }else{
            view.showMessage("No orders yet!")
        }
    }


    override fun validateNextScreen(total: String) = if(total.toFloat() > 0){
//        provider.nextScreen(total,view.getOrderAdapter()?.productList)
        view.inputTableNumber()
    }else{
        view.showPromptMessage("No orders yet!")
    }

    override fun submitOrder(tableNum: String, orderType: String) {
        val payloadDetails = HashMap<String, Any>()
//todo
        val cashierId = AppConfigPreference.create.getCashierID()
        val gson = Gson()
        val element = gson.toJson(view.getOrderAdapter()?.productList,object : TypeToken<ArrayList<OrderList>>() {}.type)
        val orders = JSONArray(element)
        var finalOrders = orders.toString()
        finalOrders = finalOrders.replace("\\","")

        payloadDetails["cmd"] = "postOrder"
        payloadDetails["tableNumber"] = tableNum
        payloadDetails["orderType"] = orderType
        payloadDetails["orderTotal"] = view.getTotal()
        payloadDetails["orderDate"] = "03/23/2019 13:30:59"
//        payloadDetails["paymentMethod"] = paymentMethod
//        payloadDetails["amountTendered"] = amountTendered
//        payloadDetails["change"] = change
        payloadDetails["cashierId"] = cashierId
        payloadDetails["orderList"] = finalOrders
        var code = 0
        view.showProgress()
        provider.submitOrder(payloadDetails){result ->
            when(result){
                is Response<*> ->{
                    code = result.code()
                    if(result.isSuccessful){
                        view.hideProgress()
                        view.showSuccessMsg("Success")
                        view.clearOrders()
//                        provider.gotoReceipt(orderTotal,orderList)
                    }else{
                        view.hideProgress()
                        view.showFailedConnectionMsg("Unsuccesful. Please try again! Error code $code")
                    }
                }
                is IOException -> {
                    view.hideProgress()
                    view.showFailedConnectionMsg("Unable to connect to server. Please try again!")
                    view.clearOrders()
                }
            }

        }

    }

    override fun setProductData() {
//        view.setTabCategory(StringData.PRODUCT_LIST)


        var code = 0
        view.showProgress()
        provider.getProducts(){ result ->
            when(result) {
                is Response<*> -> {
                    code = result.code()
                    if(result.isSuccessful){
                        view.hideProgress()
                        val productList = result.body() as ResponseBody
                        view.setTabCategory(productList.string().toString())
                    }else{
                        view.hideProgress()
                        view.showFailedConnectionMsg("Something went wrong. Please try again. Error code $code")
                    }
                }
                is Exception -> {
                    view.hideProgress()
                    view.showFailedConnectionMsg("Unable to connect to the server. Please try again.")
                }
                is IOException -> {
                    view.hideProgress()
                    view.showFailedConnectionMsg("Connection Timeout. Please try again.")
                }

        }

        }

    }

    override fun addOrderItem(productId: String, productName: String, productPrice: String, productQty: String, productTotal: String) {
        val adapter = view.getOrderAdapter()
        adapter?.productList?.add(OrderList(productId,productName,productPrice,productQty,productTotal))
        view.showEmptyOrder(adapter?.productList)
        adapter?.notifyDataSetChanged()
    }

    override fun updateOrderItem(orderId: Int, productQty: String, productTotal: String) {
        val adapter = view.getOrderAdapter()

        adapter!!.productList[orderId].productQty = productQty
        adapter.productList[orderId].productTotal = productTotal
        view.showEmptyOrder(adapter.productList)
        adapter.notifyDataSetChanged()
    }

    override fun reOrderItem(orderId: Int) {
        val adapter = view.getOrderAdapter()
        view.showEmptyOrder(adapter?.productList)
        adapter?.notifyDataSetChanged()
    }

    override fun removeOrderItem(orderId: Int) {
        val adapter = view.getOrderAdapter()

        adapter!!.productList.drop(orderId)
        view.showEmptyOrder(adapter.productList)
        adapter.notifyDataSetChanged()
    }

    override fun registerReceiver() {
        view.registerReceiver()
    }

    override fun unRegisterReceiver() {
        view.unRegisterReceiver()
    }

    override fun setOrders() {
        val orders : ArrayList<OrderList> = ArrayList<OrderList>()
//        orders.add(OrderList("adas","asd","Asd","asd","asd"))
        view.showOrders(orders)
    }

    override fun sendAddOrderBroadcast(orderList: OrderList) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getOrderData() {
//        view.setTabCategory(StringData.PRODUCT_LIST)
//        view.showOrders(StringData.ORDERLIST)

        view.showProgress()
        var code = 0
        provider.getOrders(){ result ->
            when(result) {
                is Response<*> -> {
                    view.hideProgress()
                    code = result.code()
                    if(result.isSuccessful){
                        val productList = result.body() as ResponseBody
                        orderCount = provider.showOrders(productList.string())
                    }else{
                        val errorBody = result.errorBody().toString()
                    }
                }
                is Exception -> {
                    view.hideProgress()
                }
                is MalformedJsonException -> {
                    view.hideProgress()
                }

            }

        }

    }


}