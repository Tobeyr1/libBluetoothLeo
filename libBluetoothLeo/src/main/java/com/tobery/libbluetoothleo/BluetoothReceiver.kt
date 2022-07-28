package com.tobery.libbluetoothleo

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BluetoothReceiver : BroadcastReceiver() {

    var listener:DeviceInfoCallback? = null

    var isFinished : PermissionCallback? = null

    var isOpen : PermissionCallback? = null


    @SuppressLint("MissingPermission")
    override fun onReceive(p0: Context?, intent: Intent) {
        val action = intent.action.toString()
        when(action){
            BluetoothAdapter.ACTION_STATE_CHANGED ->{//蓝牙开关状态
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)
                when(state){
                    BluetoothAdapter.STATE_ON ->{//蓝牙被打开
                        isOpen?.invoke(true)
                    }
                    BluetoothAdapter.STATE_OFF ->{//蓝牙被关闭
                        isOpen?.invoke(false)
                    }
                }
            }
            BluetoothDevice.ACTION_FOUND -> { //发现蓝牙设备
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                device?.let {
                    if (it.name != null) listener?.invoke(it,false)
                }
            }
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED ->{//传统扫描结束
                isFinished?.invoke(true)
            }
            BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED ->{//连接状态
                val state = intent.getIntExtra(
                    BluetoothAdapter.EXTRA_CONNECTION_STATE,
                    BluetoothAdapter.ERROR
                )
            }
            BluetoothDevice.ACTION_ACL_CONNECTED ->{//设备连接
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
            }
            BluetoothDevice.ACTION_ACL_DISCONNECTED ->{//设备断开连接
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
            }
            BluetoothDevice.ACTION_NAME_CHANGED ->{//远程蓝牙修改name
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
            }
            BluetoothDevice.ACTION_PAIRING_REQUEST ->{ //蓝牙配对意图
                val device :BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
                val type = intent.getIntExtra(
                    BluetoothDevice.EXTRA_PAIRING_VARIANT,
                    BluetoothDevice.ERROR)
                var pairingKey = 0
                if (type == BluetoothDevice.PAIRING_VARIANT_PASSKEY_CONFIRMATION ) {
                    //返回蓝牙配对的数字码
                    pairingKey = intent.getIntExtra(
                        BluetoothDevice.EXTRA_PAIRING_KEY,
                        BluetoothDevice.ERROR
                    )
                }
                if (type == BluetoothDevice.PAIRING_VARIANT_PIN){
                    //需要输入配对码
                }


            }
        }
    }




}
