package com.sideline.baguiopos.payment

import com.sideline.baguiopos.util.OrderList
import retrofit2.Response
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.stream.MalformedJsonException


class PaymentPresenter(val view: PaymentContract.View, val provider: PaymentContract.Provider): PaymentContract.Presenter {


    override fun validateOrder(paymentMethod: String, orderTotal: String, orderList: String, amountTendered: String, change: String) {

        if(view.getPaymentMethod().isEmpty()){
            view.showMessage("Please choose payment method first")
        }else{
            //todo
            val cashierId = "21323"

            val orders = orderList.replace("\\","")

            if(paymentMethod.isEmpty() || change.isEmpty() || amountTendered.isEmpty()){
                view.showMessage("Please select payment method!")
            } else if(amountTendered.toFloat() >= orderTotal.toFloat()){
                val payloadDetails = HashMap<String, Any>()

                payloadDetails["cmd"] = "postOrder"
                payloadDetails["orderTotal"] = orderTotal
                payloadDetails["orderDate"] = "03/23/2019 13:30:59"
                payloadDetails["paymentMethod"] = paymentMethod
                payloadDetails["amountTendered"] = amountTendered
                payloadDetails["change"] = change
                payloadDetails["cashierId"] = cashierId
                payloadDetails["orderList"] = orders
                var code = 0
                view.showProgress()
                provider.submitOrder(payloadDetails){result ->
                    when(result){
                        is Response<*> ->{
                            code = result.code()
                            if(result.isSuccessful){
                                view.hideProgress()
                                view.showSuccessMsg("Success")
                                provider.gotoReceipt(orderTotal,orderList)
                            }else{
                                view.hideProgress()
                                view.showMessage("Unsuccesful. Please try again!")
                            }
                        }
                    }

                }

            }
        }


    }

    override fun setTotal() {
        view.showTotal(provider.getTotalOrderAmount())
    }

    override fun onClick(id: Int?) {
        when(id){
            provider.getBtnValidate() -> validateOrder(view.getPaymentMethod(), provider.getTotalOrderAmount(),
                                                        provider.getOrderList(), view.getPayAmount(),
                                                        view.getChangeAmount())
            provider.getBtnBack() -> view.onBackPressed()
            provider.getBtnCash() -> cashPayment()
        }
    }
    fun cashPayment(){
        view.showAmountWrapper("Cash(USD)")
        view.enableInput()
    }

}