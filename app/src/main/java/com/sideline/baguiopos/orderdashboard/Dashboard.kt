package com.sideline.baguiopos.orderdashboard

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.sideline.baguiopos.R
import com.sideline.baguiopos.apiservice.POSAPiService
import com.sideline.baguiopos.util.OrderList
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class Dashboard : AppCompatActivity(){

    companion object {
        const val ACTION_ADDORDER_RECEIVED = "ACTION_ADDORDER_RECEIVED"
    }

    private lateinit var presenter: DashboardPresenter
    private lateinit var  view: DashboardView
    private lateinit var  provider: DashboardProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        view = DashboardView(this)
        provider = DashboardProvider(this)
        setContentView(view)

        presenter = DashboardPresenter(view,provider)
        presenter.onCreate()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(resultCode, resultCode, data)
        presenter.onActivityResult(requestCode,resultCode,data)
//        OrderView.refreshOrderList()
    }

    fun showList(orderItem : ArrayList<OrderList>){
        view.showOrderItem(orderItem)
    }

    fun getOrderItemAdapter(): OrderItemsAdapter{
        return view.getOrderItemAdapter()
    }
}