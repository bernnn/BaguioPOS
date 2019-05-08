package com.sideline.baguiopos.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.sideline.baguiopos.R
import com.sideline.baguiopos.dashboard.Dashboard
import com.sideline.baguiopos.util.BroadcastType

class InputView : DialogFragment, View.OnClickListener {

    var tvQuantity: TextView? = null
    private lateinit var prodId : String
    private lateinit var prodName : String
    private lateinit var prodPrice : String
    private lateinit var inputType : String
    private lateinit var productQty : String
    private lateinit var orderItem : Intent
    private var position : Int = 0
    private var rgOrderType : RadioGroup? = null
    private var radioOrderType : RadioButton? = null

    companion object {
        val dialogFragmentLifecycle: FragmentManager.FragmentLifecycleCallbacks by lazy { Lifecycle() }
    }

    constructor()

    @SuppressLint("ValidFragment")
    constructor(prodId: String,
                prodName: String,
                prodPrice: String,
                inputType: String){
        this.prodId = prodId
        this.prodName = prodName
        this.prodPrice = prodPrice
        this.inputType = inputType
    }

    @SuppressLint("ValidFragment")
    constructor(
            position: Int,
            productQty: String,
            productPrice: String,
            inputType: String){
        this.position = position
        this.productQty = productQty
        this.prodPrice = productPrice
        this.inputType = inputType
    }

    @SuppressLint("ValidFragment")
    constructor(
            inputType: String){
        this.inputType = inputType
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(this.activity!!, R.style.Base_Theme_AppCompat_Dialog)

        val v = activity!!.layoutInflater.inflate(R.layout.input_amount_layout, null)
        tvQuantity = v.findViewById(R.id.tvQuantity)
        val tvZero: TextView = v.findViewById(R.id.tvZero)
        val tvOne: TextView = v.findViewById(R.id.tvOne)
        val tvTwo: TextView = v.findViewById(R.id.tvTwo)
        val tvThree: TextView = v.findViewById(R.id.tvThree)
        val tvFour: TextView = v.findViewById(R.id.tvFour)
        val tvFive: TextView = v.findViewById(R.id.tvFive)
        val tvSix: TextView = v.findViewById(R.id.tvSix)
        val tvSeven: TextView = v.findViewById(R.id.tvSeven)
        val tvEight: TextView = v.findViewById(R.id.tvEight)
        val tvNine: TextView = v.findViewById(R.id.tvNine)
        val tvQty: TextView = v.findViewById(R.id.tvQty)
        val tvDisc: TextView = v.findViewById(R.id.tvDisc)
        val tvPrice: TextView = v.findViewById(R.id.tvPrice)
        val tvDelete: TextView = v.findViewById(R.id.tvDelete)
        val tvDecimal: TextView = v.findViewById(R.id.tvDecimal)
        val tvPlusMinus: TextView = v.findViewById(R.id.tvPlusMinus)
        val dineInWrapper: LinearLayout = v.findViewById(R.id.dineInWrapper)
        rgOrderType = v.findViewById(R.id.rgOrderType)


        if(inputType == "updateOrder")tvQuantity!!.text = "$productQty"

        if(inputType == "inputTableNumber"){
            tvQuantity!!.hint = "Enter Table #"
            dineInWrapper.visibility = View.VISIBLE
        }

        tvQty.text = "DEL"
        tvDisc.text = "C"
        tvPrice.text = "OK"

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
        tvDisc.setOnClickListener(this)
        tvPrice.setOnClickListener(this)
        tvQty.setOnClickListener(this)

        tvDecimal.isEnabled = false
        tvPlusMinus.isEnabled = false
        tvDelete.isEnabled = false

        builder.setView(v)
        return builder.create()
    }

    override fun onClick(v: View?) {

        activity?.runOnUiThread {
            var getQty = "${tvQuantity?.text}"
            var prodTotal = 0f

            if(v is TextView){

                if(v.text == "DEL"){
                    val new = getQty.dropLast(1)
                    tvQuantity?.text = new
                }else if(v.text == "C"){
                    dismiss()
                }else if(v.text == "OK"){
                    if(getQty.isNullOrEmpty() || getQty.equals("0")){
                        if(inputType == "inputTableNumber"){
                            Toast.makeText(context,"Please enter table number!",Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(context,"Please enter valid quantity!",Toast.LENGTH_LONG).show()
                        }

                    }else{

                        orderItem = Intent(Dashboard.ACTION_ADDORDER_RECEIVED)

                        if(inputType.equals("addOrder")){
                            prodTotal = getQty.toFloat() * prodPrice.toFloat()
                            orderItem.putExtra("broadcast_action", BroadcastType.ADD)
                            orderItem.putExtra("productId", prodId)
                            orderItem.putExtra("productName", prodName)
                            orderItem.putExtra("productPrice", prodPrice)
                            orderItem.putExtra("productQty", getQty)
                            orderItem.putExtra("productTotal", prodTotal.toString())

                        }else if(inputType.equals("updateOrder")){
                            prodTotal = getQty.toFloat() * prodPrice.toFloat()
                            orderItem.putExtra("broadcast_action", BroadcastType.EDIT)
                            orderItem.putExtra("productId", position)
                            orderItem.putExtra("productQty", getQty)
                            orderItem.putExtra("productTotal", prodTotal.toString())

                        }else if(inputType.equals("inputTableNumber")){

                            val orderTypeValue = rgOrderType?.checkedRadioButtonId
//                            radioOrderType = orderTypeValue?.let { v.findViewById(it) }
                            radioOrderType = rgOrderType?.findViewById<RadioButton>(orderTypeValue!!)

                            orderItem.putExtra("broadcast_action", BroadcastType.SUBMITORDER)
                            orderItem.putExtra("tableNumber", getQty)
                            orderItem.putExtra("orderType", radioOrderType?.text.toString())
                        }
                        LocalBroadcastManager.getInstance(activity as AppCompatActivity).sendBroadcast(orderItem)
                        dismiss()
                    }
                }else{
                    tvQuantity?.text = getQty.plus("${v.text}")

                }
            }
        }
    }

    override fun show(manager: FragmentManager?, tag: String?) {
        manager?.registerFragmentLifecycleCallbacks(dialogFragmentLifecycle, true)
        super.show(manager, tag)
    }


    private class Lifecycle : FragmentManager.FragmentLifecycleCallbacks() {

        override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
            super.onFragmentAttached(fm, f, context)

            val tag = f?.tag
            fm?.fragments?.forEach f@{

                /**
                 * Check if tag is null or empty
                 */
                if (tag.isNullOrEmpty()) return@f

                /**
                 * Filter the tag, since there will be another fragment that will be registered but different tag name
                 */
                if (!tag.equals(it.tag, true)) return@f

                /**
                 * Remove the fragment that is added.
                 * Avoid multiple fragment since we want to show only 1 fragment at a time.
                 * If there is an existing fragment, it will be closed/dismissed.
                 */
                if (!it.toString().equals(f.toString(), true)) {
                    fm.beginTransaction().remove(it).commitAllowingStateLoss()
                }
            }
        }
    }
}