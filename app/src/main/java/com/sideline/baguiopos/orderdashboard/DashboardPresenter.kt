package com.sideline.baguiopos.orderdashboard

import android.content.Intent
import android.util.Log
import android.util.MalformedJsonException
import com.sideline.baguiopos.apiservice.StringData
import com.sideline.baguiopos.common.RequestCode
import okhttp3.ResponseBody
import retrofit2.Response

class DashboardPresenter(val view: DashboardContract.View, val provider: DashboardContract.Provider): DashboardContract.Presenter {
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RequestCode.REQUEST_PAYMENT_SUCCESS){
            setProductData()
            Log.e("asdsdasasdsdas","faawwfeffefe")
            Log.e("asdsdasasdsdas","faawwfeffefe")
            Log.e("asdsdasasdsdas","faawwfeffefe")
        }
    }

    override fun addOrder() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    init {
        view.presenter = this
    }
    override fun onCreate() {
        setProductData()
    }

    override fun onCLick(id: Int) {
        when(id){
            provider.getBtnUpdateOrder() ->  view.onBackWithResultUpdate()
        }
    }


    override fun setProductData() {
//        view.setTabCategory(StringData.PRODUCT_LIST)
//        view.showOrders(StringData.ORDERLIST)

        view.showProgress()
        var code = 0
        provider.getOrders(){ result ->
            when(result) {
                is Response<*> -> {
                    view.hideProgress()
                    code = result.code()
                    if(result.isSuccessful){
                        val productList = result.body() as ResponseBody
                        view.showOrders(productList.string().toString())
                    }else{
                        val errorBody = result.errorBody().toString()
                    }
                }
                is Exception -> {
                    view.hideProgress()
                }
                is MalformedJsonException -> {
                    view.hideProgress()
                }

        }

        }

    }


}