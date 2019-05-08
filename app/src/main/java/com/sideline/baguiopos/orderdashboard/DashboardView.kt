package com.sideline.baguiopos.orderdashboard

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.*
import com.sideline.baguiopos.R
import com.sideline.baguiopos.apiservice.ContextProvider.context
import com.sideline.baguiopos.util.Command
import com.sideline.baguiopos.util.OrderList
import com.sideline.baguiopos.util.StringAmountFormatter
import com.sideline.baguiopos.util.TableList
import com.sideline.baguiopos.view.BaseWrapper
import org.json.JSONObject



open class DashboardView( val activity: AppCompatActivity) : BaseWrapper(activity), DashboardContract.View {

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        orderFragment.onActivityResult(requestCode,resultCode,data)
    }

    override lateinit var presenter: DashboardContract.Presenter
    private var adapter : OrderDashboardAdapter? = null
    private var orderItemAdapter : OrderItemsAdapter? = null
    private var tvUpdateOrder : TextView? = null
    private var totalWrapper : RelativeLayout? = null
    private var orderListWrapper : View? = null
    private var legendWrapper : View? = null
    private var orderListDetailWrapper : View? = null
    private lateinit var progressDialog: ProgressDialog

    init {
        initialized()
    }



    private var tvTotal : TextView? = null
    private var gvList : GridView? = null
    var orderListView: RecyclerView? = null
    var toolbar: Toolbar? = null

    override fun initialized() {
        val view = View.inflate(context, R.layout.order_dashboard_activity_layout,this)

        toolbar = view.findViewById(R.id.toolbar)
        orderListWrapper = view.findViewById(R.id.orderListWrapper)
        orderListDetailWrapper = view.findViewById(R.id.orderListDetailWrapper)
        legendWrapper = view.findViewById(R.id.legendWrapper)
        tvTotal = view.findViewById(R.id.tvTotal)
        gvList = view.findViewById<GridView>(R.id.list)
        orderListView = view.findViewById<RecyclerView>(R.id.rvOrderList)
        totalWrapper = view.findViewById<RelativeLayout>(R.id.totalWrapper)
        tvUpdateOrder = view.findViewById<TextView>(R.id.tvUpdateOrder)

        orderItemAdapter = OrderItemsAdapter(activity,context,ArrayList<OrderList>())

        activity.setSupportActionBar(toolbar)
        toolbar!!.title = "BaguioPOS"

        val mLayoutManager = LinearLayoutManager(activity)
        orderListView?.layoutManager = mLayoutManager
        orderListView?.itemAnimator = DefaultItemAnimator()
        orderListView?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Processing. . .")
        progressDialog.setCancelable(false)

    }



    fun setViewDetails(){
        val paramDetails = LinearLayout.LayoutParams(
                0,
                LayoutParams.MATCH_PARENT,
                40.0f
        )
        val paramList = LinearLayout.LayoutParams(
                0,
                LayoutParams.MATCH_PARENT,
                60.0f
        )

        activity.runOnUiThread{
            orderListDetailWrapper?.visibility = View.VISIBLE
            orderListDetailWrapper?.layoutParams = paramDetails
            orderListWrapper?.layoutParams = paramList
        }
    }

    override fun getOrderAdapter(): OrderDashboardAdapter = adapter!!


    override fun onBackWithResultUpdate() {
        val intent = Intent()
        intent.putExtra("action", "updateOrder")
        intent.putExtra("orderList", orderItemAdapter?.productList.toString())
        activity.setResult(Activity.RESULT_OK, intent)
        activity.onBackPressed()
    }

    override fun showOrders(orderList: String) {
        var tablename = ""
        val jsonArray = JSONObject(orderList)
        val jsonArrayCategory = jsonArray.getJSONArray("table")
        val jsonArrayStatus = jsonArray.getJSONArray("status")
        val tableArray = ArrayList<TableList>()
        val tableStatusArray = ArrayList<TableList>()

        val arraySize = jsonArrayCategory.length()

        if (arraySize > 0){

            for(index in 0 until jsonArrayCategory.length()) {

                val iterator = jsonArrayCategory.getJSONObject(index).keys()
                while(iterator.hasNext()){
                    tablename = iterator.next()
                    tableArray.add(TableList(tablename,jsonArrayCategory.getJSONObject(index).getString(tablename)))
                    tableStatusArray.add(TableList(tablename,jsonArrayStatus.getJSONObject(index).getString(tablename),""))
                }
            }

            activity.runOnUiThread{
                adapter = OrderDashboardAdapter(activity,tableArray,tableStatusArray, Command { setViewDetails() })
                gvList?.adapter = adapter
                adapter!!.notifyDataSetChanged()
            }
        }
    }

    override fun showOrderItem(itemList: ArrayList<OrderList>) {
        activity.runOnUiThread{

            orderItemAdapter!!.productList.clear()
            orderItemAdapter!!.notifyDataSetChanged()
            orderItemAdapter = OrderItemsAdapter(activity,context,itemList)

            orderListView?.adapter = orderItemAdapter
            adapter?.notifyDataSetChanged()

            if(orderItemAdapter!!.productList.size > 0){

                var total = 0f
                for(i in itemList.indices){
                    total += itemList[i].productTotal.toFloat()
                }
                tvTotal?.text = StringAmountFormatter.amountFormat(total.toString())
            }
        }
    }

    fun getOrderList(): View? = orderListWrapper
    fun getOrderListDetails(): View? = orderListDetailWrapper

    override fun onBackPressed() {
        activity.onBackPressed()
    }

    fun getOrderItemAdapter() : OrderItemsAdapter = orderItemAdapter!!


    override fun showProgress() {
        activity.runOnUiThread{
            progressDialog.show()
        }
    }

    override fun hideProgress() {
        activity.runOnUiThread{
            progressDialog.dismiss()
        }
    }
}