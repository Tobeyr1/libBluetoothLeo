package com.tobery.libbluetoothleo

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanFilter
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import java.lang.ref.WeakReference

class BluetoothUtil {

    //广播接收器
    private var bluetoothBroadcastReceiver : BluetoothReceiver? = null
    //设备信息获取回调
    private var deviceInfo:DeviceInfoCallback? = null
    //蓝牙适配器
    private lateinit var bluetoothAdapter : BluetoothAdapter
    //扫描过滤
    private var buildFilter : ArrayList<ScanFilter>? = null
    //授权回调
    private var isGranted:PermissionCallback? = null

    private lateinit var bluetoothActivity: WeakReference<ComponentActivity>

    private lateinit var permissionregister: PermissionChecks

    private var permissionArray = mutableListOf<String>()


    companion object{
        @Volatile private var bluetooth : BluetoothUtil? = null
        fun getInstance():BluetoothUtil = bluetooth?: synchronized(this){
            bluetooth?: BluetoothUtil()
        }
    }

    /**
     * 初始化
     * @param activity 引用
     * @param permissionChecks 注册回调
     */
    fun init(activity: ComponentActivity,permissionChecks: PermissionChecks):BluetoothUtil{
        bluetoothActivity = WeakReference(activity)
        bluetoothAdapter = (bluetoothActivity.get()?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
        permissionregister = permissionChecks
        verifyBluetoothCapabilities()
        return this
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


        permissionregister.requestPermissions(permissionArray.toTypedArray()){
            if (it){
                val enable = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                permissionregister.startActivityForResult(enable){result ->
                    isGranted?.invoke(result.resultCode == Activity.RESULT_OK)
                }
            }else{
                throw Exception("bluetooth permission is denied")
                "bluetooth permission is denied".printLog()
            }
        }

    }

}
typealias DeviceInfoCallback = (device: BluetoothDevice?, isStop:Boolean) -> Unit