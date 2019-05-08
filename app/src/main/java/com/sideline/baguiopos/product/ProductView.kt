package com.sideline.baguiopos.product

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.GridView
import com.sideline.baguiopos.R
import com.sideline.baguiopos.apiservice.POSAPiService
import com.sideline.baguiopos.util.BroadcastType
import com.sideline.baguiopos.view.BaseWrapper
import java.util.ArrayList

class ProductView(val activity: AppCompatActivity) : BaseWrapper(activity),ProductContract.View{

    lateinit var presenter: ProductContract.Presenter
    private var adapter : ProductAdapter? = null
    private var gvList : GridView? = null

    override fun initialize() {
        val view = View.inflate(context, R.layout.product_layout, this)

        gvList = view.findViewById<GridView>(R.id.list)
    }

    override fun showProducts(products: ArrayList<POSAPiService.Response.ProductItems>) {
        activity.runOnUiThread{
            adapter = ProductAdapter(activity,products)
            gvList?.adapter = adapter
            adapter!!.notifyDataSetChanged()
        }
    }

    override fun unRegisterReceiver() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun registerReceiver() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val productReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == ProductItemFragment.ACTION_PRODUCT_RECEIVED) {
                val broadCastAction = intent.getSerializableExtra("broadcast_action")
                val title = intent.getStringExtra("title")

                if (broadCastAction == BroadcastType.ADD) {
//                    presenter.updateAdapterItem(title, true)
                }
            }
        }
    }

}