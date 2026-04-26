package org.garis.pam.platform

import kotlinx.coroutines.flow.Flow

interface DeviceInfo {
    fun getDeviceName(): String
    fun getOsVersion(): String
    fun getAppVersion(): String
}

interface NetworkMonitor {
    val isConnected: Flow<Boolean>
}

interface BatteryInfo {
    fun getBatteryLevel(): Int
    fun isCharging(): Boolean
}
