package com.sideline.baguiopos.product

import com.sideline.baguiopos.apiservice.POSAPiService
import com.sideline.baguiopos.util.OrderList
import java.util.ArrayList

class ProductPresenter(val view: ProductContract.View, val provider: ProductContract.Provider) : ProductContract.Presenter {
    override fun sendAddOrderBroadcast(orderList: OrderList) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun setProducts() {
        view.showProducts(provider.getProductArguments())
     }

    override fun registerReceiver() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unRegisterReceiver() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}