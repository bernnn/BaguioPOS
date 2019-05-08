package com.sideline.baguiopos.orderdashboard

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.sideline.baguiopos.R
import com.sideline.baguiopos.util.OrderList
import com.sideline.baguiopos.view.InputView

class OrderItemsAdapter(
var activity: FragmentActivity?,
var context: Context?,
var productList : ArrayList<OrderList>
) : RecyclerView.Adapter<OrderItemsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val itemView = LayoutInflater.from(p0.getContext())
                .inflate(R.layout.order_item_layout, p0, false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        val product = productList[p1]
        val totalPrice = product.productQty.toFloat() * product.productPrice.toFloat()

        p0.tvItemName.text = "${product.productName}"
        p0.tvItemDescription.text = "${product.productQty} Unit(s) at $${product.productPrice}"
        p0.tvItemTotalPrice.text =  ""+totalPrice

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    lateinit var mItems : java.util.ArrayList<OrderList>


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemWrapper: FrameLayout
        var tvItemName: TextView
        var tvItemDescription: TextView
        var tvItemTotalPrice: TextView

        init {
            itemWrapper = view.findViewById(R.id.itemWrapper)
            tvItemName = view.findViewById(R.id.tvItemName)
            tvItemDescription = view.findViewById(R.id.tvItemDescription)
            tvItemTotalPrice = view.findViewById(R.id.tvItemTotalPrice)
        }
    }



//

}