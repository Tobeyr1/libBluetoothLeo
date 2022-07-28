package com.tobery.libbluetoothleo

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class PermissionChecks {
    private var permissionCallback: PermissionCallback? = null

    private var permissionRegister: ActivityResultLauncher<Array<String>>? = null

    private var activityResultCallback: ActivityResultCallback? = null

    private var activityResultRegister: ActivityResultLauncher<Intent>? = null

     fun register(activity: ComponentActivity){
        activityResultRegister =
            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                activityResultCallback?.invoke(it)
            }
        permissionRegister =
            activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { list ->
                val grantedList = list.filterValues { it }.mapNotNull { it.key }
                val allGranted = grantedList.size == list.size
                permissionCallback?.invoke(allGranted)
            }
    }

     fun startActivityForResult(intent: Intent, callback: ActivityResultCallback) {
        this.activityResultCallback = callback
        activityResultRegister?.launch(intent)
    }

     fun requestPermission(
        permission: String,
        callback: PermissionCallback? = null
    ) {
        this.permissionCallback = callback
        permissionRegister?.launch(arrayOf(permission))
    }

     fun requestPermissions(
        permissions: Array<String>,
        callback: PermissionCallback? = null
    ) {
        this.permissionCallback = callback
        permissionRegister?.launch(permissions)
    }
}

typealias PermissionCallback = (isGranted: Boolean) -> Unit
typealias ActivityResultCallback = (result: ActivityResult) -> Unit