package com.sideline.baguiopos.payment

class PaymentContract {

    interface View{
        fun initialized()
        fun showAmountWrapper(paymentMethod: String)
        fun showChange(change: String)
        fun showTotal(total: String)
        fun onBackPressed()
        fun showProgress()
        fun hideProgress()
        fun showMessage(message: String)
        fun setPayAmount(amount: String)
        fun getPayAmount(): String
        fun getChangeAmount(): String
        fun getPaymentMethod() : String
        fun showSuccessMsg(message: String)
        fun disableInput()
        fun enableInput()

    }

    interface Presenter {
        fun setTotal()
        fun onClick(id: Int?)
        fun validateOrder(paymentMethod: String, orderTotal: String, orderList: String, amountTendered: String, change: String)

    }

    interface Provider {
        fun getBtnBack(): Int
        fun getBtnCash(): Int
        fun getBtnValidate(): Int
        fun getTotalOrderAmount(): String
        fun getOrderList(): String
        fun gotoReceipt(total: String, orderList: String)
        fun submitOrder(payload: HashMap<String, Any>, result: (response: Any) -> Unit)

    }


}