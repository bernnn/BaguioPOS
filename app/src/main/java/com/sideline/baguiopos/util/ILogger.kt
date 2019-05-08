package com.sideline.baguiopos.util



interface ILogger {
    fun v(tag: String, message: String, log: Boolean)
    fun i(tag: String, message: String, log: Boolean)
    fun e(tag: String, message: String, e: Throwable, log: Boolean)

    companion object {
        val create by lazy { LoggerImpl() }
    }
}
