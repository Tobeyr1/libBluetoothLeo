package com.tobery.libbluetoothleo

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import android.util.Log

fun Any.printLog(){
    Log.i(this.javaClass.simpleName, toString())
}

fun Context.isMainProcess(): Boolean {
    val am = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val runningApp = am.runningAppProcesses
    return if (runningApp == null) {
        false
    } else {
        val var3: Iterator<*> = runningApp.iterator()
        var info: ActivityManager.RunningAppProcessInfo
        do {
            if (!var3.hasNext()) {
                return false
            }
            info = var3.next() as ActivityManager.RunningAppProcessInfo
        } while (info.pid != Process.myPid())
        this.packageName == info.processName
    }
}