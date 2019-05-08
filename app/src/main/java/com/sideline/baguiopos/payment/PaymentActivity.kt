package com.sideline.baguiopos.payment

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class PaymentActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var  presenter: PaymentPresenter
    private lateinit var view: PaymentView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = PaymentView(this)
        val provider = PaymentProvider(this)
        setContentView(view)

        presenter = PaymentPresenter(view,provider)
        view.presenter = presenter
        view.initialized()
        presenter.setTotal()
    }


    override fun onClick(v: View?) {
        presenter.onClick(v?.id)

    }


}