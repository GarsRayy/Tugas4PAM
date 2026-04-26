package org.garis.pam.platform

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import platform.UIKit.UIDevice
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_get_status
import platform.Network.nw_path_status_satisfied
import platform.darwin.dispatch_get_main_queue

class IosDeviceInfo : DeviceInfo {
    override fun getDeviceName(): String = UIDevice.currentDevice.name
    override fun getOsVersion(): String = "${UIDevice.currentDevice.systemName} ${UIDevice.currentDevice.systemVersion}"
    override fun getAppVersion(): String = "1.0.0"
}

class IosNetworkMonitor : NetworkMonitor {
    private val _isConnected = MutableStateFlow(true)
    override val isConnected: Flow<Boolean> = _isConnected

    init {
        val monitor = nw_path_monitor_create()
        nw_path_monitor_set_update_handler(monitor) { path ->
            val status = nw_path_get_status(path)
            _isConnected.value = status == nw_path_status_satisfied
        }
        nw_path_monitor_set_queue(monitor, dispatch_get_main_queue())
        nw_path_monitor_start(monitor)
    }
}

class IosBatteryInfo : BatteryInfo {
    init {
        UIDevice.currentDevice.batteryMonitoringEnabled = true
    }

    override fun getBatteryLevel(): Int = (UIDevice.currentDevice.batteryLevel * 100).toInt()

    override fun isCharging(): Boolean {
        val state = UIDevice.currentDevice.batteryState
        return state == platform.UIKit.UIDeviceBatteryStateCharging || 
               state == platform.UIKit.UIDeviceBatteryStateFull
    }
}
