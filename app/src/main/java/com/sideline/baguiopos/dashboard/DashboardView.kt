package com.sideline.baguiopos.dashboard

import android.app.ProgressDialog
import android.content.*
import android.graphics.Color
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sideline.baguiopos.R
import com.sideline.baguiopos.apiservice.POSAPiService
import com.sideline.baguiopos.login.LoginActivity
import com.sideline.baguiopos.product.ProductItemFragment
import com.sideline.baguiopos.util.*
import com.sideline.baguiopos.view.BaseWrapper
import com.sideline.baguiopos.view.InputView
import com.sideline.baguiopos.view.ViewPagerAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import org.json.JSONObject

import kotlinx.android.synthetic.main.activity_main.view.*
//import org.json.JSONObject

class DashboardView(private val activity: AppCompatActivity): BaseWrapper(activity), DashboardContract.View {



    override lateinit var presenter: DashboardContract.Presenter

    init {
        initialized()
    }

    private var tvTotal : TextView? = null
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout : DynamicTab

    var orderListView: RecyclerView? = null
    var adapter: OrderAdapter? = null
    var tvEmptyOrder: TextView? = null
    var toolbar: Toolbar? = null
    var tvPay: TextView? = null
    var tvViewOrder: TextView? = null
        var tvStaffName: TextView? = null
    var totalOrder : Float = 0f
    var coordinatorLayout: CoordinatorLayout? = null
    val intentFilter = IntentFilter()
    var tableNumber = ""
    var orderType = ""

    private lateinit var progressDialog: ProgressDialog

    override fun initialized() {
        val view = View.inflate(context, R.layout.activity_main,this)

        toolbar = view.findViewById(R.id.toolbar)
        tvTotal = view.findViewById(R.id.tvTotal)
        viewPager = view.findViewById(R.id.viewpager)
        tabLayout = view.findViewById(R.id.tabs)

        activity.setSupportActionBar(toolbar)
        toolbar!!.title = "BaguioPOS"


        orderListView = view.findViewById<RecyclerView>(com.sideline.baguiopos.R.id.orderList) as RecyclerView
        tvEmptyOrder = view.findViewById<TextView>(R.id.tvEmptyOrder) as TextView
        tvPay = view.findViewById<TextView>(R.id.tvPay) as TextView
        tvViewOrder = view.findViewById<TextView>(R.id.tvViewOrder) as TextView
        tvStaffName = view.findViewById<TextView>(R.id.tvStaffName) as TextView
        tvTotal = view.findViewById<TextView>(R.id.tvTotal) as TextView
        coordinatorLayout = view.findViewById(R.id.coordinator_layout) as CoordinatorLayout

        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Processing. . .")
        progressDialog.setCancelable(false)
        tvStaffName!!.text = AppConfigPreference.create.getCashierName()

        enableSwipeToDeleteAndUndo()

    }

    override fun onBackPressed() {
        activity.onBackPressed()
    }

    override fun setTabCategory(productList: String) {

        var tabname = ""

        Observable.just(productList)
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnNext{

                    val viewPagerAdpter = ViewPagerAdapter(activity.supportFragmentManager)
                    val productItemList = object : TypeToken<ArrayList<POSAPiService.Response.ProductItems>>(){}.type

                    val jsonArray = JSONObject(productList)
                    val jsonArrayCategory = jsonArray.getJSONArray("products")
                    var categoryItem: ArrayList<POSAPiService.Response.ProductItems> = ArrayList()

                    val arraySize = jsonArrayCategory.length()

                    if (arraySize > 0){

                        for(index in 0 until arraySize) {

                            val iterator = jsonArrayCategory.getJSONObject(index).keys()
                            while(iterator.hasNext()){
                                tabname = iterator.next()
                                val value = jsonArrayCategory.getJSONObject(index).getString(tabname)

                                Log.e("asdasd", jsonArrayCategory.getJSONObject(index).getString(tabname))
                                Log.e("asdasd", jsonArrayCategory.getJSONObject(index).getString(tabname))
                                Log.e("asdasd", jsonArrayCategory.getJSONObject(index).getString(tabname))

                                val catItemSize = jsonArrayCategory.getJSONObject(index).getJSONArray(tabname).length()

                                for(catIndex in 0 until catItemSize) {
                                    categoryItem = Gson().fromJson(value.toString(),productItemList)
                                }
                            }
                            viewPagerAdpter.addFragment(ProductItemFragment.newInstance(categoryItem),tabname)
                        }
                    }





                    if(viewPagerAdpter.count > 0){


                        viewpager.adapter = viewPagerAdpter
                        tabLayout?.setTabNumbers(viewPagerAdpter.count)
                        if(viewPagerAdpter.count == 1){
                            tabLayout?.setSelectedTabIndicatorColor(Color.WHITE)
                        }else{
                            tabLayout?.setSelectedTabIndicatorColor(Color.CYAN)
                        }

                        tabLayout.setupWithViewPager(viewPager)

                    }else{

                    }
                }.subscribe()
    }

    private fun enableSwipeToDeleteAndUndo(){

        val swipeToDeleteCallback = object : SwipeToDeleteCallback(context){
            override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
                val position = p0.adapterPosition
                val item = adapter!!.productList.get(position)

                adapter?.removeItem(position)


                val snackbar = Snackbar
                        .make(coordinatorLayout as View, "Item was removed from the list.", Snackbar.LENGTH_LONG)
                snackbar.setAction("UNDO") {
                    adapter?.restoreItem(item, position)
                    orderListView?.scrollToPosition(position)
                }

                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()
            }

        }

        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(orderListView)
    }

    override fun getTotal(): String = "$totalOrder"

    override fun showTotal(total: Float) {
        activity.runOnUiThread{
            tvTotal?.text = "$total"
        }
    }

    override fun showPromptMessage(messge: String) {
        Toast.makeText(context,messge, Toast.LENGTH_SHORT).show()
    }

    override fun refreshOrderList() {
        activity.runOnUiThread{
            orderListView?.adapter = null
            adapter?.notifyDataSetChanged()
        }
    }


    override fun unRegisterReceiver() {
        activity.let { LocalBroadcastManager.getInstance(it).unregisterReceiver(orderReceiver) }
    }

    private fun getIFilter(): IntentFilter {
        intentFilter.addAction(Dashboard.ACTION_ADDORDER_RECEIVED)
        return intentFilter
    }

    override fun registerReceiver() {
        activity.let { LocalBroadcastManager.getInstance(it).registerReceiver(orderReceiver, getIFilter()) }
    }

    override fun showOrders(products: java.util.ArrayList<OrderList>) {
        activity.runOnUiThread{
            adapter = OrderAdapter(activity, context, products)

            val mLayoutManager = LinearLayoutManager(activity)
            orderListView?.layoutManager = mLayoutManager
            orderListView?.itemAnimator = DefaultItemAnimator()
            orderListView?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            orderListView?.adapter = adapter
            adapter?.notifyDataSetChanged()
        }

    }

    override fun loadExistingOrder(item: String) {
        activity.runOnUiThread{
//            adapter = OrderAdapter(activity,context,products)


            val mLayoutManager = LinearLayoutManager(activity)
            orderListView?.layoutManager = mLayoutManager
            orderListView?.itemAnimator = DefaultItemAnimator()
            orderListView?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            orderListView?.adapter = adapter
            adapter?.notifyDataSetChanged()
        }
    }


    override fun inputTableNumber() {
        val inputDialog : InputView?
        // todo appropriate dialog
        inputDialog = InputView("inputTableNumber")
        if(inputDialog != null){
            inputDialog.isCancelable = false
            inputDialog.show(activity?.supportFragmentManager,"input")
        }

    }

    override fun showEmptyOrder(orders: java.util.ArrayList<OrderList>?) {
        activity.runOnUiThread{
            if (orders!!.size <= 0) {
                tvEmptyOrder?.visibility = View.VISIBLE
                totalOrder = 0.00f
                showTotal(totalOrder)
            } else {
                totalOrder = 0f
                for(i in orders.indices){
                    var total = orders[i].productTotal.toFloat()
                    totalOrder += total
                }
                showTotal(totalOrder)
                tvEmptyOrder?.visibility = View.GONE
            }
        }
    }

    override fun showFailedConnectionMsg(msg: String) {
        activity.runOnUiThread{
            val dialog = AlertDialogX(message = msg)

            dialog.isCancelable = false
            dialog.dialogShowManager(activity.supportFragmentManager!!, dialog, "brd")
        }
    }

    override fun logoutDialog(msg: String){
        activity.runOnUiThread{
            val dialog = AlertDialogX(message = msg, okBtnTitle = "OK", okClickListener = {_,_-> exitPOS()},
                    cancelBtnTitle = "CANCEL")

            dialog.isCancelable = false
            dialog.dialogShowManager(activity.supportFragmentManager!!, dialog, "brd")
        }
    }
    fun exitPOS(){
        AppConfigPreference.create.clear()
        activity.finish()
        val intent = Intent(activity,LoginActivity::class.java)
        activity.startActivity(intent)
    }

    override fun clearOrders() {
        activity.runOnUiThread{
            getOrderAdapter()?.productList?.clear()
            getOrderAdapter()?.notifyDataSetChanged()
        }
        showEmptyOrder(ArrayList<OrderList>())
    }

    override fun getOrderAdapter(): OrderAdapter? = adapter

    private val orderReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Dashboard.ACTION_ADDORDER_RECEIVED) {
                val broadCastAction = intent.getSerializableExtra("broadcast_action")
                if(broadCastAction == BroadcastType.ADD){
                    val productId = intent.getStringExtra("productId")
                    val productName = intent.getStringExtra("productName")
                    val productPrice = intent.getStringExtra("productPrice")
                    val productQty = intent.getStringExtra("productQty")
                    val productTotal = intent.getStringExtra("productTotal")
                    presenter.addOrderItem(productId, productName, productPrice, productQty, productTotal)
                }else if(broadCastAction == BroadcastType.EDIT){
                    val orderId = intent.getIntExtra("productId",0)
                    val productQty = intent.getStringExtra("productQty")
                    val productTotal = intent.getStringExtra("productTotal")
                    presenter.updateOrderItem(orderId, productQty, productTotal)
                }else if(broadCastAction == BroadcastType.REMOVE){
                    val orderId = intent.getIntExtra("productId",0)
                    presenter.removeOrderItem(orderId)
                }else if(broadCastAction == BroadcastType.REORDER){
                    val orderId = intent.getIntExtra("productId",0)
                    presenter.removeOrderItem(orderId)
                }else if(broadCastAction == BroadcastType.SUBMITORDER){
                    tableNumber = intent.getStringExtra("tableNumber")
                    orderType = intent.getStringExtra("orderType")
                    presenter.submitOrder(tableNumber,orderType)
                }
            }

        }
    }
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

    override fun showMessage(message: String) {
        activity.runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showSuccessMsg(message: String) {
        activity.runOnUiThread{
            Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
        }
    }




}