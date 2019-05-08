package com.sideline.baguiopos.product

import com.sideline.baguiopos.apiservice.POSAPiService
import com.sideline.baguiopos.util.OrderList
import java.util.ArrayList

class ProductContract {

    interface View{
        fun initialize()
        fun unRegisterReceiver()
        fun registerReceiver()
        fun showProducts(products: ArrayList<POSAPiService.Response.ProductItems>)

    }

    interface Presenter {
        fun registerReceiver()
        fun unRegisterReceiver()
        fun setProducts()
        fun sendAddOrderBroadcast(orderList: OrderList)
    }


    interface Provider {
        fun broadcastAddItem(serviceCategory: OrderList)
        fun setUpdateItems(updatedItems: ArrayList<OrderList>)
        fun getUpdateItems(): ArrayList<OrderList>
        fun getPaybillsServicesDefault(): ArrayList<OrderList>
        fun getProductArguments() : ArrayList<POSAPiService.Response.ProductItems>
    }




}