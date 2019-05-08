package com.sideline.baguiopos.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var presenter: LoginPresenter
    private lateinit var  view: LoginView
    private lateinit var  provider: LoginProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        view = LoginView(this)
        provider = LoginProvider(this)
        setContentView(view)


        presenter = LoginPresenter(view,provider)
        presenter.setConfig()
    }

    override fun onClick(v: View?) {
        presenter.onClick(v?.id)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        view.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}