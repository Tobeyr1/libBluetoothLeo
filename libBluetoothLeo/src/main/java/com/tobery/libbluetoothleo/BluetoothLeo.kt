package com.tobery.libbluetoothleo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.ComponentActivity
import com.tobery.libbluetoothleo.util.AppLifecycleCallback
import java.lang.ref.WeakReference

object BluetoothLeo {

    private lateinit var bluetoothActivity: WeakReference<ComponentActivity>

    private lateinit var permissionRegister: PermissionChecks

    private var permissionArray = mutableListOf<String>()

    private var globalContext: Application? = null
    //广播接收器
    private var bluetoothBroadcastReceiver : BluetoothReceiver? = null
    //设备信息获取回调
    private var deviceInfo:DeviceInfoCallback? = null
    //蓝牙适配器
    private lateinit var bluetoothAdapter : BluetoothAdapter
    //扫描配置
    private var scanConfig : ArrayList<BluetoothScanRules>? = null
    //授权回调
    private var isGranted:PermissionCallback? = null
    //callback
    @SuppressLint("StaticFieldLeak")
    private var appLifecycleCallback = AppLifecycleCallback()


    /**
     * 初始化
     * @param activity 引用
     * @param permissionChecks 注册回调
     */
    fun init(activity: ComponentActivity, permissionChecks: PermissionChecks) = apply{
        bluetoothActivity = WeakReference(activity)
        bluetoothAdapter = (bluetoothActivity.get()?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
        permissionRegister = permissionChecks
    }

    fun setScanRule(scanRules: BluetoothScanRules) = apply{
        scanConfig?.add(scanRules)
    }

    fun setScanRule(scanRules: List<BluetoothScanRules>) = apply {
        this.scanConfig?.addAll(scanRules)
    }

    /**
     * 初始化
     */
    fun apply(){
        if (globalContext == null) {
            throw NullPointerException("context is null")
        }
        if (!globalContext!!.isMainProcess()) return
        globalContext!!.registerActivityLifecycleCallbacks(appLifecycleCallback)
        verifyBluetoothCapabilities()
    }

    /**
     * 判断蓝牙相关权限及蓝牙是否开启等
     */
    private fun verifyBluetoothCapabilities(){
        if(bluetoothAdapter == null) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionArray.add(Manifest.permission.BLUETOOTH_SCAN)
            permissionArray.add(Manifest.permission.BLUETOOTH_ADVERTISE)
            permissionArray.add(Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            permissionArray.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
       // val permissionCheckstest = PermissionChecks()
       // permissionCheckstest.register(appLifecycleCallback.getStackTopActivity() as ComponentActivity)



        permissionRegister.requestPermissions(permissionArray.toTypedArray()){
            if (it){
                val enable = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                permissionRegister.startActivityForResult(enable){result ->
                    isGranted?.invoke(result.resultCode == Activity.RESULT_OK)
                }
            }else{
                throw Exception("bluetooth permission is denied")
                "bluetooth permission is denied".printLog()
            }
        }

    }

    //开启蓝牙功能
    fun enableBluetooth() = apply {

    }

    //获取蓝牙适配器
    fun getBluetoothAdapters() = bluetoothAdapter

    //蓝牙是否启用
    fun isEnable() = bluetoothAdapter.isEnabled

    fun resetVariable(activity: Activity){
    }

    //对象全置空
    @SuppressLint("MissingPermission")
    fun release(){
        //pairedDevices = null
        bluetoothActivity.get()?.unregisterReceiver(bluetoothBroadcastReceiver)
        bluetoothActivity.clear()
        scanConfig?.clear()
        scanConfig = null
        if (bluetoothAdapter.isDiscovering) bluetoothAdapter.cancelDiscovery()
        //if (bluetoothScanCallback != null)  bluetoothLeScanner?.stopScan(bluetoothScanCallback)
        //bluetoothLeScanner = null
        permissionArray.clear()
        //handlerRegister = null
        //bluetoothScanCallback?.listener = null
        //bluetoothScanCallback = null
        bluetoothBroadcastReceiver?.listener = null
        bluetoothBroadcastReceiver = null
    }
}
typealias DeviceInfoCallback = (device: BluetoothDevice?, isStop:Boolean) -> Unit