package com.tobery.libbluetoothleo.util

import android.bluetooth.BluetoothDevice

abstract class OnBluetoothScanResultListener{

    abstract fun oonScanFinished(scanResultList: List<BluetoothDevice>?)

    abstract fun onScanning(bluetooth: BluetoothDevice?)

}