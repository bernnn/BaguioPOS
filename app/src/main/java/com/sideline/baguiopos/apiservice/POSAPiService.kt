package com.sideline.baguiopos.apiservice

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.sideline.baguiopos.util.ILogger
import com.sideline.baguiopos.util.LoggerInterceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface POSAPiService {

    sealed class Response {

        data class Order(
                @field: Expose val message: String? = ""
        ) : Response()

        data class Login (
            @field: Expose val success: Boolean = false,
            @field: Expose val cashierName: String? = "",
            @field: Expose val cashierId: String? = ""
        ) : Response(), Parcelable {
            constructor(parcel: Parcel) : this(
                    parcel.readByte() != 0.toByte(),
                    parcel.readString(),
                    parcel.readString()) {
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeByte(if (success) 1 else 0)
                parcel.writeString(cashierName)
                parcel.writeString(cashierId)
            }

            override fun describeContents(): Int {
                return 0
            }

            companion object CREATOR : Parcelable.Creator<Login> {
                override fun createFromParcel(parcel: Parcel): Login {
                    return Login(parcel)
                }

                override fun newArray(size: Int): Array<Login?> {
                    return arrayOfNulls(size)
                }
            }
        }

        class ProductItems(
                val productId : String = "",
                val productName : String = "",
                val img : String = "",
                val productPrice : String = ""
        ): Parcelable {
            constructor(parcel: Parcel) : this(
                    parcel.readString(),
                    parcel.readString(),
                    parcel.readString(),
                    parcel.readString()) {
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(productId)
                parcel.writeString(productName)
                parcel.writeString(img)
                parcel.writeString(productPrice)
            }

            override fun describeContents(): Int {
                return 0
            }

            companion object CREATOR : Parcelable.Creator<ProductItems> {
                override fun createFromParcel(parcel: Parcel): ProductItems {
                    return ProductItems(parcel)
                }

                override fun newArray(size: Int): Array<ProductItems?> {
                    return arrayOfNulls(size)
                }
            }
        }

        data class OrdersList(
                @field: Expose val list: String? = ""
        ) : Response()

    }

    companion object {
        fun create(): POSAPiService {

            val builder = OkHttpClient.Builder()

//            val headers = mutableMapOf("Content-Type" to "application/json",
//                    "Authorization" to "Sample"
//            )


//            builder.addInterceptor(HeaderInterceptor(headers))
            builder.addInterceptor(LoggerInterceptor.getInstance(ILogger.create))
            builder.addInterceptor(NetworkInspector.getChuckNetworkInspector())
            val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .baseUrl("http://192.168.1.8/")
                    .baseUrl("http://www.mocky.io/v2/")
                    .client(builder.build())
                    .build()
                    .create(POSAPiService::class.java)

        }
    }


    @GET("5caf51003400009b24ab729f")
//    @GET("practice/baguio-pos/webservice/controller/ApiController.php?cmd=getProducts")
    fun getProducts(): Call<ResponseBody>

//    @GET("practice/baguio-pos/webservice/controller/ApiController.php?cmd=getProducts")
    @GET("5cb346043000007d25a78dc6")
    fun getOrders(): Call<ResponseBody>

//    @GET("practice/baguio-pos/webservice/controller/ApiController.php?cmd=getProducts")
    @GET("5cb2ce203000007100a78cbc")
    fun getOrdersList(): Call<ResponseBody>

//    @POST("practice/baguio-pos/webservice/controller/ApiController.php")
    @POST("5cb2e3f03000006c00a78cd4")
//    @FormUrlEncoded
    fun submitOrder(@Body params: @JvmSuppressWildcards HashMap<String, Any>): Call<Response.Order>

    @POST("5cb1e0de3300004227572105")
    fun sigIn(@Body params: @JvmSuppressWildcards HashMap<String, Any>): Call<Response.Login>



}