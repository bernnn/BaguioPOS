package com.sideline.baguiopos.login

import android.Manifest.permission.*
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.telephony.TelephonyManager
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.sideline.baguiopos.R
import com.sideline.baguiopos.view.BaseWrapper
import android.support.v4.content.ContextCompat.getSystemService
import android.annotation.SuppressLint
import android.util.Log
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.support.v4.app.ActivityCompat
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.support.v4.app.ActivityCompat.requestPermissions
import android.os.Build
import android.content.DialogInterface
import android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.text.Html
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.sideline.baguiopos.BuildConfig
import com.sideline.baguiopos.util.AppConfigPreference
import com.sideline.baguiopos.util.RemoteConfig


class LoginView(val activity: AppCompatActivity): BaseWrapper(activity), LoginContract.View{
    var etUsername: EditText? = null
    var etPassword: EditText? = null
    private var tvTitle : TextView? = null
    var tvLogin: TextView? = null
    val PERMISSION_REQUEST_CODE = 200;
    var conf = ""

    private lateinit var progressDialog: ProgressDialog

    override lateinit var presenter: LoginContract.Presenter

    init {
        initialized()
    }

    @SuppressLint("MissingPermission")
    override fun initialized() {
        val view = View.inflate(context, R.layout.login_activity_layout,this)

        etUsername = view.findViewById(R.id.etUsername)
        etPassword = view.findViewById(R.id.etPassword)
        tvLogin = view.findViewById(R.id.tvLogin)
        tvTitle = view.findViewById(R.id.tvTitle)


        tvTitle!!.text = Html.fromHtml("<b>M</b>POS")

        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Processing. . .")
        progressDialog.setCancelable(false)

        if(!checkPermission()){
            requestPermission()
        }else{
            sendEvent()
        }
    }

    override fun getIMEI(): String = conf



    @SuppressLint("HardwareIds", "SetTextI18n")
    fun sendEvent(){
        try{

            var telephonyManager =activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val bundle = Bundle()
            conf = telephonyManager.deviceId
            bundle.putString("emei", telephonyManager.deviceId)
            FirebaseAnalytics.getInstance(activity).logEvent("emei",bundle)
            allowedToUse(telephonyManager.deviceId)
        }catch (e: SecurityException){
            e.printStackTrace()
        }

    }

    fun allowedToUse(emei: String){
        if(RemoteConfig.getString("allowed_device").contains(emei)){
            tvLogin?.visibility = View.VISIBLE
        }else{
            tvLogin?.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        activity.onBackPressed()
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

    override fun getUserName(): String = etUsername?.text.toString()

    override fun getPassword(): String = etPassword?.text.toString()

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, READ_PHONE_STATE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {

        ActivityCompat.requestPermissions(activity, arrayOf(READ_PHONE_STATE), PERMISSION_REQUEST_CODE)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0) {

                val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED

                if (cameraAccepted){
                    sendEvent()
                    Snackbar.make(this, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show()
                } else {

                    Snackbar.make(this, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show()

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(activity,READ_PHONE_STATE)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(activity,arrayOf(READ_PHONE_STATE),
                                                    PERMISSION_REQUEST_CODE)
                                        }
                                    })
                            return
                        }
                    }

                }
            }
        }
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
         AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}