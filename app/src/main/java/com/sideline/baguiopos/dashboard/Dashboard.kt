package com.sideline.baguiopos.dashboard

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.sideline.baguiopos.R


class Dashboard : AppCompatActivity(), View.OnClickListener{
    override fun onClick(v: View?) {
        presenter.onClick(v?.id)
    }

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
        presenter.setOrders()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(resultCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        presenter.setProductData()
        presenter.registerReceiver()
        presenter.getOrderData()
    }

    override fun onPause() {
        presenter.unRegisterReceiver()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unRegisterReceiver()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_action_logout,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        presenter.onClick(item?.itemId)
        return true
    }
}