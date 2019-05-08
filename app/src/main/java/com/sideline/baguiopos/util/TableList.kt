package com.sideline.baguiopos.util

class TableList{
    var tableName : String = ""
    var tableOrderItem : String = ""
    var tableStatus : String = ""

    constructor(tableName : String, tableOrderItem : String){
        this.tableName = tableName
        this.tableOrderItem = tableOrderItem
    }

    constructor(tableName : String, tableStatus : String, extra : String){
        this.tableName = tableName
        this.tableStatus = tableStatus
    }



}