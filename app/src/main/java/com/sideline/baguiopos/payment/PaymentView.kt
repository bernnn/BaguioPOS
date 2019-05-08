package com.sideline.baguiopos.payment

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.sideline.baguiopos.R
import com.sideline.baguiopos.common.RequestCode
import com.sideline.baguiopos.util.AppConfigPreference
import com.sideline.baguiopos.view.BaseWrapper

class PaymentView(val activity: AppCompatActivity): BaseWrapper(activity), PaymentContract.View, View.OnClickListener{
    lateinit var presenter: PaymentContract.Presenter
    private var tvTotalAmount: TextView? = null
    private var tvDue: TextView? = null
    private var tvTendered: TextView? = null
    private var tvChange: TextView? = null
    private var tvMethod: TextView? = null
    private var layoutKeyboard: LinearLayout? = null
    private var amountWrapper: RelativeLayout? = null
    private var inputAmountWrapper: RelativeLayout? = null
    private lateinit var progressDialog: ProgressDialog

    override fun initialized() {
        val view = View.inflate(context, R.layout.payment_layout,this)

        tvTotalAmount = view.findViewById(R.id.tvTotalAmount)
        tvDue = view.findViewById(R.id.tvDue)
        tvTendered = view.findViewById(R.id.tvTendered)
        tvChange = view.findViewById(R.id.tvChange)
        tvMethod = view.findViewById(R.id.tvMethod)
        amountWrapper = view.findViewById(R.id.amountWrapper)
        inputAmountWrapper = view.findViewById(R.id.inputAmountWrapper)
        amountWrapper = view.findViewById(R.id.amountWrapper)
        layoutKeyboard = view.findViewById(R.id.layoutKeyboard)


        val tvZero = view.findViewById<TextView>(R.id.tvZero)
        val tvOne = view.findViewById<TextView>(R.id.tvOne)
        val tvTwo = view.findViewById<TextView>(R.id.tvTwo)
        val tvThree = view.findViewById<TextView>(R.id.tvThree)
        val tvFour = view.findViewById<TextView>(R.id.tvFour)
        val tvFive = view.findViewById<TextView>(R.id.tvFive)
        val tvSix = view.findViewById<TextView>(R.id.tvSix)
        val tvSeven = view.findViewById<TextView>(R.id.tvSeven)
        val tvEight = view.findViewById<TextView>(R.id.tvEight)
        val tvNine = view.findViewById<TextView>(R.id.tvNine)
        val tvQty = view.findViewById<TextView>(R.id.tvQty)
        val tvDisc = view.findViewById<TextView>(R.id.tvDisc)
        val tvPrice = view.findViewById<TextView>(R.id.tvPrice)
        val tvDelete = view.findViewById<TextView>(R.id.tvDelete)
        val tvDecimal = view.findViewById<TextView>(R.id.tvDecimal)
        val tvPlusMinus = view.findViewById<TextView>(R.id.tvPlusMinus)

        tvQty.text = "DEL"

        tvZero.setOnClickListener(this)
        tvOne.setOnClickListener(this)
        tvTwo.setOnClickListener(this)
        tvThree.setOnClickListener(this)
        tvFour.setOnClickListener(this)
        tvFive.setOnClickListener(this)
        tvSix.setOnClickListener(this)
        tvSeven.setOnClickListener(this)
        tvEight.setOnClickListener(this)
        tvNine.setOnClickListener(this)
        tvQty.setOnClickListener(this)
        tvDecimal.setOnClickListener(this)

        tvPlusMinus.isEnabled = false
        tvPrice.isEnabled = false
        tvDisc.isEnabled = false

        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Processing. . .")
        progressDialog.setCancelable(false)

        disableInput()

    }


    override fun onClick(v: View?) {
        activity.runOnUiThread{

            if(v is TextView){
                var getTendered = "${tvTendered?.text}"
                var change = 0f
                val dueAmount = tvDue?.text.toString()

                if(v.text == "." && getTendered.isEmpty()){

                }else if(v.text == "DEL"){
                    getTendered = getTendered.dropLast(1)
                    tvTendered?.text = getTendered

                    if(getTendered.isEmpty()){
                        change = 0.00f
                    }else{
                        change = dueAmount.toFloat() - getTendered.toFloat()
                    }
                    tvChange?.text = change.toString()
                }else{
                    getTendered = getTendered.plus("${v.text}")
                    tvTendered?.text = getTendered
                    if( ! getTendered.isEmpty()){
                        change = dueAmount.toFloat() - getTendered.toFloat()
                    }
                    tvChange?.text = change.toString()
                }


            }

        }

    }

    override fun setPayAmount(amount: String) {
        tvTendered?.text = amount
    }

    override fun getPaymentMethod(): String = tvMethod?.text.toString()

    override fun getPayAmount(): String = tvTendered?.text.toString()

    override fun getChangeAmount(): String = tvChange?.text.toString()

    override fun showMessage(message: String) {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
    }

    override fun disableInput() {
        inputAmountWrapper?.isEnabled = false
    }

    override fun enableInput() {
        inputAmountWrapper?.isEnabled = true
    }

    override fun showSuccessMsg(message: String) {
        activity.runOnUiThread{
            Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
            activity.setResult(RequestCode.REQUEST_PAYMENT_SUCCESS)
            activity.onBackPressed()
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

    override fun showAmountWrapper(paymentMethod: String) {
        activity.runOnUiThread{

            if(paymentMethod.isEmpty()){
                amountWrapper?.visibility = View.VISIBLE
                inputAmountWrapper?.visibility = View.GONE
                layoutKeyboard?.isEnabled = false
            }else{
                tvMethod?.text = paymentMethod
                amountWrapper?.visibility = View.GONE
                inputAmountWrapper?.visibility = View.VISIBLE
                layoutKeyboard?.isEnabled = true
            }
        }
    }

    override fun onBackPressed() {
        activity.onBackPressed()
    }

    override fun showChange(change: String) {

    }

    override fun showTotal(total: String) {
        tvTotalAmount?.text = total
        tvDue?.text = total
    }

}