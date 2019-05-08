package com.sideline.baguiopos.product

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.sideline.baguiopos.R
import com.sideline.baguiopos.apiservice.POSAPiService

class ProductItemFragment: Fragment(){


    private lateinit var  presenter: ProductPresenter


    companion object {

        const val ACTION_PRODUCT_RECEIVED = "ACTION_PRODUCT_RECEIVED"

        fun newInstance(
                    list : ArrayList<POSAPiService.Response.ProductItems>?
        ): ProductItemFragment {
                val productItemFragment = ProductItemFragment()
                val data = Bundle()
                data.putParcelableArrayList("productList", list)
                productItemFragment.arguments = data
                return productItemFragment
        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         val view = ProductView(activity as AppCompatActivity)
         val provider = ProductProvider(activity as AppCompatActivity,this)

         presenter = ProductPresenter(view,provider)
         view.presenter = presenter
         view.initialize()
         presenter.setProducts()

        return view
    }


}