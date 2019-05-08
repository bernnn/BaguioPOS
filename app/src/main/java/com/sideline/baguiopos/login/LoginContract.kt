package com.sideline.baguiopos.login

import com.sideline.baguiopos.view.BaseView

interface LoginContract {

    interface View: BaseView<Presenter>{
        fun initialized()
        fun onBackPressed()
        fun showProgress()
        fun hideProgress()
        fun showMessage(message: String)
        fun getUserName(): String
        fun getPassword(): String
        fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
        fun getIMEI(): String
    }

    interface Presenter{
        fun onClick(id: Int?)
        fun signInSubmit(username: String, password: String)
        fun setConfig()
        fun validateSignIn()
    }

    interface Provider{
        fun signIn(payload: HashMap<String, Any>, result: (response: Any) -> Unit)
        fun getBtnSignin(): Int
        fun gotoDashboard()
        fun saveUserName(username: String)
        fun fetchConfig() :  String

    }

}