package com.tobery.libbluetoothleo

import android.util.Log

fun Any.printLog(){
    Log.i(this.javaClass.simpleName, toString())
}