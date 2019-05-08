package com.sideline.baguiopos.dashboard

import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.sideline.baguiopos.R
import com.sideline.baguiopos.util.BroadcastType
import com.sideline.baguiopos.util.OrderList
import com.sideline.baguiopos.util.StringAmountFormatter
import com.sideline.baguiopos.view.InputView


data class OrderAdapter(
        var activity: FragmentActivity?,
        var context: Context?,
        var productList : ArrayList<OrderList>
) : RecyclerView.Adapter<OrderAdapter.MyViewHolder>() {




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
        p0.tvItemTotalPrice.text =  StringAmountFormatter.amountFormat(totalPrice.toString())


        p0.itemWrapper.setOnClickListener {
            val inputDialog : InputView?
            // todo appropriate dialog
            Log.e("position",""+p1)
            Log.e("position",""+p1)
            Log.e("position",""+p1)
            inputDialog = InputView(p1,product.productQty,product.productPrice,"updateOrder")
            if(inputDialog != null){
                inputDialog.isCancelable = false
                inputDialog.show(activity?.supportFragmentManager,"input")
            }
        }
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
        var viewBackground: RelativeLayout
        var viewForeground:RelativeLayout

        init {
            itemWrapper = view.findViewById(R.id.itemWrapper)
            tvItemName = view.findViewById(R.id.tvItemName)
            tvItemDescription = view.findViewById(R.id.tvItemDescription)
            tvItemTotalPrice = view.findViewById(R.id.tvItemTotalPrice)
            viewBackground = view.findViewById(R.id.view_background)
            viewForeground = view.findViewById(R.id.view_foreground)
        }
    }



    fun restoreItem(item: OrderList, position: Int) {
        productList.add(position, item)
        // notify item added by position
        val orderItem = Intent(Dashboard.ACTION_ADDORDER_RECEIVED)
        orderItem.putExtra("broadcast_action", BroadcastType.REORDER)
        orderItem.putExtra("orderId", position)
        context?.let { LocalBroadcastManager.getInstance(it).sendBroadcast(orderItem) }
        notifyItemInserted(position)
    }

    fun removeItem(position: Int) {
        productList.removeAt(position)
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        val orderItem = Intent(Dashboard.ACTION_ADDORDER_RECEIVED)
        orderItem.putExtra("broadcast_action", BroadcastType.REMOVE)
        orderItem.putExtra("orderId", position)
        context?.let { LocalBroadcastManager.getInstance(it).sendBroadcast(orderItem) }
        notifyItemRemoved(position)
    }

}