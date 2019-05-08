package com.sideline.baguiopos.util

//interface OrderList{
//
//
////    var productId : String = ""
////    var productName : String = ""
////    var productPrice : String = ""
////    var productQty : String = ""
////    var productTotal : String = ""
//
//    companion object {
//        fun list(productId : String, productName : String, productPrice : String, productQty : String,
//                 productTotal : String){
//
////            this.productId = productId
////            this.productName = productName
////            this.productPrice = productPrice
////            this.productQty = productQty
////            this.productTotal = productTotal
//        }
//    }
//
//
//
//
//}




class OrderList {
    var productId : String = ""
    var productName : String = ""
    var productPrice : String = ""
    var productQty : String = ""
    var productTotal : String = ""

    constructor(productId : String, productName : String, productPrice : String, productQty : String,
                productTotal : String){
        this.productId = productId
        this.productName = productName
        this.productPrice = productPrice
        this.productQty = productQty
        this.productTotal = productTotal
    }



}