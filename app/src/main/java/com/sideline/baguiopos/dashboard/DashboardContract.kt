package com.sideline.baguiopos.dashboard

import com.sideline.baguiopos.util.OrderList
import com.sideline.baguiopos.view.BaseView
import java.util.ArrayList

class DashboardContract {

    interface View : BaseView<Presenter> {
        fun initialized()
        fun onBackPressed()
        fun setTabCategory(productList: String)
        fun unRegisterReceiver()
        fun registerReceiver()
        fun showOrders(products: ArrayList<OrderList>)
        fun getOrderAdapter(): OrderAdapter?
        fun showEmptyOrder(orders: ArrayList<OrderList>?)
        fun getTotal(): String
        fun showTotal(total: Float)
        fun showPromptMessage(messge: String)
        fun refreshOrderList()
        fun inputTableNumber()
        fun loadExistingOrder(item: String)
        fun clearOrders()
        fun showSuccessMsg(message: String)
        fun showProgress()
        fun hideProgress()
        fun showMessage(message: String)
        fun showFailedConnectionMsg(msg: String)
        fun logoutDialog(msg: String)
    }

    interface Presenter {
        fun setProductData()
        fun addOrder()
        fun getOrderData()
        fun registerReceiver()
        fun unRegisterReceiver()
        fun onClick(id: Int?)
        fun setOrders()
        fun addOrderItem(productId: String,productName: String,productPrice: String,productQty: String,productTotal: String)
        fun updateOrderItem(orderId: Int ,productQty: String, productTotal: String)
        fun removeOrderItem(orderId: Int)
        fun reOrderItem(orderId: Int)
        fun validateNextScreen(total: String)
        fun sendAddOrderBroadcast(orderList: OrderList)
        fun submitOrder(tableNum: String, orderType: String)

    }

    interface  Provider {
        fun getProducts(result: (response: Any) -> Unit)
        fun getBtnLogout(): Int
        fun nextScreen(total: String, orderList: ArrayList<OrderList>?)
        fun gotoOrders()
        fun submitOrder(payload: HashMap<String, Any>, result: (response: Any) -> Unit)
        fun getBtnPayment(): Int
        fun getBtnViewOrder(): Int
        fun getOrders(result: (response: Any) -> Unit)
        fun showOrders(orderList: String): Int



    }



}