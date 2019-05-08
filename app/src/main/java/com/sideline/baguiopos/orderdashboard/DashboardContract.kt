package com.sideline.baguiopos.orderdashboard

import android.content.Intent
import com.sideline.baguiopos.util.OrderList
import com.sideline.baguiopos.view.BaseView
import okhttp3.ResponseBody

class DashboardContract {

    interface View : BaseView<Presenter> {
        fun initialized()
        fun onBackPressed()
        fun showOrders(orderList: String)
        fun showOrderItem(itemList: ArrayList<OrderList>)
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
        fun onBackWithResultUpdate()
        fun showProgress()
        fun hideProgress()
        fun getOrderAdapter(): OrderDashboardAdapter
    }

    interface Presenter {
        fun onCreate()
        fun onCLick(id: Int)
        fun setProductData()
        fun addOrder()
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    }

    interface  Provider {
        fun getOrders(result: (response: Any) -> Unit)
        fun getBtnUpdateOrder(): Int



    }



}