package com.tobery.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tobery.libbluetoothleo.BluetoothLeo
import com.tobery.libbluetoothleo.PermissionChecks

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BluetoothLeo.init(this, PermissionChecks())
            .apply()
    }
}