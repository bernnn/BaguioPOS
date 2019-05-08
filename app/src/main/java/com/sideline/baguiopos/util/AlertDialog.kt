package com.sideline.baguiopos.util

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import com.sideline.baguiopos.R


class AlertDialogX: DialogFragment {

    companion object {
        val dialogFragmentLifecycle: FragmentManager.FragmentLifecycleCallbacks by lazy { Lifecycle() }
    }

    //    private lateinit var ctx: Activity
    private var title: String = ""
    private var message: String = ""
    private var okBtnTitle: String = ""
    private var okBtnListener: ((dialog: DialogInterface, which: Int) -> Unit)? = { d, w -> }
    private var cancelBtnTitle: String? = ""
    private var cancelBtnListener: DialogInterface.OnClickListener? = null

    constructor()

    @SuppressLint("ValidFragment")
    constructor(
            title: String = "Alert!",
            message: String = "",
            okBtnTitle: String = "Ok",
            okClickListener: ((dialog: DialogInterface, which: Int) -> Unit)? = { d, w -> },
            cancelBtnTitle: String? = null,
            cancelBtnListener: DialogInterface.OnClickListener? = null) : this() {
        this.title = title
        this.message = message
        this.okBtnTitle = okBtnTitle
        this.okBtnListener = okClickListener
        this.cancelBtnTitle = cancelBtnTitle
        this.cancelBtnListener = cancelBtnListener

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(this.activity!!, R.style.Base_Theme_AppCompat_Dialog)

        return builder.apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(okBtnTitle, okBtnListener)
            setNegativeButton(cancelBtnTitle, cancelBtnListener)
            setCancelable(false)
        }.create()
    }

    override fun show(manager: FragmentManager?, tag: String?) {
        if (activity != null && !activity!!.isFinishing) {
            manager?.registerFragmentLifecycleCallbacks(dialogFragmentLifecycle, true)
            super.show(manager, tag)
        }
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

    fun dialogShowManager(supportFragmentManager: FragmentManager, dialog: DialogFragment, tag: String) {
        supportFragmentManager?.registerFragmentLifecycleCallbacks(Lifecycle(), true)
        supportFragmentManager?.beginTransaction()?.add(dialog, "$tag")?.commitAllowingStateLoss()
    }
}