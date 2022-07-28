package com.tobery.libbluetoothleo

import java.util.*

data class BluetoothConfig(
    var enableLog: Boolean = false,
    var reconnectCount: Int = 1,
    var reconnectTime: Long = 5000L,
    var connectTimeout: Long = 10000L,

    )

data class BluetoothScanRules(
    var serviceUUIDs: List<UUID>? = null,
    var deviceNames: List<String>? = null,
    var deviceMac: String? = null,
    var scanTimeout: Long = 10000L
)