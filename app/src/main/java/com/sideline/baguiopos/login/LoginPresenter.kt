package com.sideline.baguiopos.login

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.sideline.baguiopos.apiservice.POSAPiService
import com.sideline.baguiopos.util.AppConfigPreference
import com.sideline.baguiopos.util.setCashierID
import com.sideline.baguiopos.util.setCashierName
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

class LoginPresenter(val view: LoginContract.View, val provider: LoginContract.Provider): LoginContract.Presenter{
    override fun onClick(id: Int?) {
        when(id){
            provider.getBtnSignin() -> signInSubmit(view.getUserName(),view.getPassword())
        }
    }



    override fun setConfig(){
        provider.fetchConfig()
    }

    override fun validateSignIn() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signInSubmit(username: String, password: String) {


        val conf = FirebaseRemoteConfig.getInstance().getString("allowed_device")
        val array = conf.split(",")

        if( ! array.contains(view.getIMEI())){
            view.showMessage("Device not allowed. Please contact admin!")
        }else if(username.isNullOrEmpty() || password.isNullOrEmpty()){
            view.showMessage("Please enter your valid log in credentials")
        }else{
            val payLoad = HashMap<String, Any>()
            payLoad.put("username", username)
            payLoad.put("password",password)
            view.showProgress()
            provider.signIn(payLoad){result ->
                when(result){
                    is Response<*> ->{

                        val code = result.code()
                        if(result.isSuccessful){
                            val response = result.body() as POSAPiService.Response.Login
                            view.hideProgress()
                            if(response.success){
                                response.cashierName?.let { AppConfigPreference.create.setCashierName(it) }
                                response.cashierId?.let { AppConfigPreference.create.setCashierID(it) }
                                provider.gotoDashboard()
                            }else{
                                view.showMessage("Log in failed! Please try again.")
                            }
                        }else{
                            view.hideProgress()
                            view.showMessage("Something went wrong! Please try again. Code $code")
                        }

                    }
                    is IOException -> {
                        view.hideProgress()
                        view.showMessage("Connectiopn Time out! Please try again")

                    }
                    is Exception -> {
                        view.hideProgress()
                        view.showMessage("Something went wrong! Please try again")
                    }
                }

            }
        }
    }

}