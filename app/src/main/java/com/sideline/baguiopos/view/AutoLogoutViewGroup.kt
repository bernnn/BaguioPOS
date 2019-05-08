package com.sideline.baguiopos.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.sideline.baguiopos.common.Constant

open class AutoLogoutViewGroup(activity: Activity) : FrameLayout(activity) {

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        triggerTimer()
        super.onVisibilityChanged(changedView, visibility)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        triggerTimer()
        return false
    }

    private fun triggerTimer() {
        broadCastTouch(context)
    }

    companion object {

        @Synchronized
        fun broadCastTouch(context: Context) {
            val intent = Intent(Constant.TOUCH_EVENT)
            context.sendBroadcast(intent)
        }
    }
}