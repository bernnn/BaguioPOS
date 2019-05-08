package com.sideline.baguiopos.util;

import android.util.Log;

import org.jetbrains.annotations.NotNull;


public class LoggerImpl implements ILogger {
    private static final String TAG = "gcash-tokyo";

    @Override
    public void v(@NotNull String tag, @NotNull String message, boolean log) {
        if ( ! log) return;

        Log.v(TAG, String.format("%s: %s", tag, message));
    }

    @Override
    public void i(String tag, String message, boolean log)
    {
        if ( ! log) return;

        Log.i(TAG, String.format("%s: %s", tag, message));
    }

    @Override
    public void e(String tag, String message, Throwable e, boolean log)
    {
        if ( ! log) return;

        Log.e(TAG, String.format("%s: %s", tag, message));

        if (null != e)
        {
            e.printStackTrace();
        }
    }

}
