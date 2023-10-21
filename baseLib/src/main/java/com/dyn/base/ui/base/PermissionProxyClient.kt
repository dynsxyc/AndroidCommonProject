package com.dyn.base.ui.base

import com.dyn.base.ui.OnRequestPermissionListener
import com.vmadalin.easypermissions.EasyPermissions

interface PermissionProxyClient {
    fun requestPermission(
        tips: String,
        vararg params: String,
        action: OnRequestPermissionListener?)
    fun registerPermissionResult(permissionResultListener:OnPermissionResultListener)

}

interface OnPermissionResultListener:EasyPermissions.PermissionCallbacks,EasyPermissions.RationaleCallbacks{

}