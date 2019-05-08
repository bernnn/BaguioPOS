package com.sideline.baguiopos.product

import android.content.Intent
import android.os.Parcelable
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import com.sideline.baguiopos.apiservice.POSAPiService
import com.sideline.baguiopos.dashboard.Dashboard
import com.sideline.baguiopos.util.OrderList
import java.util.ArrayList

class ProductProvider(val activity: AppCompatActivity, val prodcutFragment: ProductItemFragment): ProductContract.Provider {
    override fun broadcastAddItem(serviceCategory: OrderList) {

    }

    override fun setUpdateItems(updatedItems: ArrayList<OrderList>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUpdateItems(): ArrayList<OrderList> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPaybillsServicesDefault(): ArrayList<OrderList> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getProductArguments(): ArrayList<POSAPiService.Response.ProductItems> {

        var data = ArrayList<POSAPiService.Response.ProductItems>()
        if (prodcutFragment.arguments != null) {
            if (prodcutFragment.arguments?.getParcelableArrayList<Parcelable>("productList") != null) {
                data = prodcutFragment.arguments?.getParcelableArrayList<Parcelable>("productList") as ArrayList<POSAPiService.Response.ProductItems>
            }
        }
        return data
    }


}
