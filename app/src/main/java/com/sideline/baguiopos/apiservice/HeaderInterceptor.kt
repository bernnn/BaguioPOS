package com.sideline.baguiopos.apiservice

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val headers: Map<String, String>) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val builder = request.newBuilder()
        headers.forEach { (t, u) ->
            builder.header(t, u)
        }

        return chain.proceed(builder.build())
    }

}