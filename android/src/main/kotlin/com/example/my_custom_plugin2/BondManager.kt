package com.example.my_custom_plugin2

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import io.flutter.plugin.common.MethodChannel

class BondManager {

    fun getBondedDevices(result: MethodChannel.Result) {

        val adapter = BluetoothAdapter.getDefaultAdapter()

        if (adapter == null) {
            result.error("NO_ADAPTER", "Bluetooth not supported", null)
            return
        }

        if (!adapter.isEnabled) {
            result.error("BLUETOOTH_OFF", "Bluetooth is disabled", null)
            return
        }

        val bondedDevices: Set<BluetoothDevice> = adapter.bondedDevices

        val list = bondedDevices.map { device ->
            mapOf(
                "name" to (device.name ?: "Unknown"),
                "address" to device.address,
                "bondState" to device.bondState
                // 10 = BOND_NONE
                // 11 = BOND_BONDING
                // 12 = BOND_BONDED
            )
        }

        result.success(list)
    }
}
