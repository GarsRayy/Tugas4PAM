package org.garis.pam.di

import org.garis.pam.data.local.DatabaseDriverFactory
import org.garis.pam.platform.*
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { DatabaseDriverFactory() }
    single<DeviceInfo> { IosDeviceInfo() }
    single<NetworkMonitor> { IosNetworkMonitor() }
    single<BatteryInfo> { IosBatteryInfo() }
}
