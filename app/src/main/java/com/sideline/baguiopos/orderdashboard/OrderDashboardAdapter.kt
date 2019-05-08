package com.sideline.baguiopos.orderdashboard

import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.sideline.baguiopos.R
import com.sideline.baguiopos.apiservice.POSAPiService
import com.sideline.baguiopos.util.OrderList
import com.sideline.baguiopos.util.TableList
import com.sideline.baguiopos.view.InputView
import org.json.JSONArray
import org.json.JSONObject
import android.support.v4.os.HandlerCompat.postDelayed
import android.os.Handler
import android.support.v4.content.ContextCompat
import com.sideline.baguiopos.apiservice.ContextProvider
import com.sideline.baguiopos.util.Command


class OrderDashboardAdapter(
        var activity: AppCompatActivity?,
        var table : ArrayList<TableList>,
        var tableStatus : ArrayList<TableList>,
        var cmd : Command
) : BaseAdapter() {

    lateinit var presenter: DashboardContract.Presenter

    override fun getItem(position: Int): Any {
        return  position.toLong()
    }

    override fun getCount(): Int {
        return table.size
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(activity).inflate(R.layout.order_dashboard_item_layout, null)

        val table = table[position]
        val tableStat = tableStatus[position]

        Log.e("tableStat",tableStat.tableStatus)

        val tableWrapper = view.findViewById<LinearLayout>(R.id.tableWrapper)
        var tvTableNumber = view.findViewById<TextView>(R.id.tvTableNumber)
        val string = table.tableOrderItem

        if(convertView != null){
            tvTableNumber.text = "${table.tableName}"

            if(tableStat.tableStatus.equals("Ready to serve",true)){
                tableWrapper.background = ContextCompat.getDrawable(ContextProvider.context, R.color.colorGreen)
            }else if(tableStat.tableStatus.equals("Processing",true)){
                tableWrapper.background = ContextCompat.getDrawable(ContextProvider.context, R.color.colorYellow)
            }else if(tableStat.tableStatus.equals("On queue",true)){
                tableWrapper.background = ContextCompat.getDrawable(ContextProvider.context, R.color.colorRed)
            }else if(tableStat.tableStatus.equals("Served",true)){
                tableWrapper.background = ContextCompat.getDrawable(ContextProvider.context, R.color.btnBG)
            }

            tableWrapper.setOnClickListener {

                val jsonArray = JSONArray(table.tableOrderItem)

                var orderItem: ArrayList<OrderList> = ArrayList()


                cmd.execute()

                for(i in 0 until jsonArray.length()){
                    val jsonObject = jsonArray.getJSONObject(i)
                    orderItem.add(OrderList(jsonObject.getString("productId"),
                                        jsonObject.getString("productName"),
                                        jsonObject.getString("productPrice"),
                                        jsonObject.getString("productQty"),
                                        jsonObject.getString("productTotal")
                                        ))
                }

                val act = activity as Dashboard
                act.showList(orderItem)



            }
        }
        return view
    }





    override fun getItemId(position: Int): Long {
        return position.toLong()
    }




}


