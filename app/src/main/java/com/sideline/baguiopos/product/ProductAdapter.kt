package com.sideline.baguiopos.product

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sideline.baguiopos.R
import com.sideline.baguiopos.apiservice.POSAPiService
import com.sideline.baguiopos.view.BaseRecyclerViewAdapter
import com.sideline.baguiopos.view.InputView

data class ProductAdapter(
        var activity: FragmentActivity?,
        var productList : ArrayList<POSAPiService.Response.ProductItems>
) : BaseAdapter() {

    lateinit var presenter: ProductContract.Presenter

    override fun getItem(position: Int): Any {
        return  position.toLong()
    }

    override fun getCount(): Int {
        return productList.size
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(activity).inflate(R.layout.product_item_layout, null)

        val product = productList[position]

        val productWrapper = view.findViewById<LinearLayout>(R.id.productWrapper)
        var ivProductImage = view.findViewById<ImageView>(R.id.ivProductImage)
        val tvProductName = view.findViewById<TextView>(R.id.tvProductName)
        val tvProductPrice = view.findViewById<TextView>(R.id.tvProductPrice)

        if(convertView != null){
            ivProductImage.setImageResource(R.mipmap.ic_launcher)
            tvProductName.text = "${product.productName}"
            tvProductPrice.text = "${product.productPrice}"

            var url =  product.img
            url = url.replace("\\","")
            Log.e("newUrl", url)
            Log.e("newUrl", url)
            Log.e("newUrl", url)

            Glide
                    .with(activity!!)
//                    .load("http:\\/\\/192.168.100.44:8080\\/baguio-pos\\/pages\\/products\\/assets\\/img\\/8375648836_1555297816.jpg")
                    .load(product.img)
                    .into(ivProductImage)

            productWrapper.setOnClickListener {
                val inputDialog : InputView?

                inputDialog = InputView(product.productId,product.productName,product.productPrice,"addOrder")
                if(inputDialog != null){
                    inputDialog.isCancelable = false
                    inputDialog.show(activity?.supportFragmentManager,"input")
                }
            }
        }

        return view
    }





    override fun getItemId(position: Int): Long {
        return position.toLong()
    }




}