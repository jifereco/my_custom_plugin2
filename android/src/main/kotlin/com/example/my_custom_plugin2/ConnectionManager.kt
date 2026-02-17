package com.example.my_custom_plugin2

import android.bluetooth.*
import android.content.Context
import android.util.Log
import io.flutter.plugin.common.MethodChannel

class ConnectionManager(
    private val context: Context
) {

    private var bluetoothGatt: BluetoothGatt? = null

    fun connect(
        address: String,
        autoConnect: Boolean,
        result: MethodChannel.Result
    ) {

        val adapter = BluetoothAdapter.getDefaultAdapter()

        if (adapter == null || !adapter.isEnabled) {
            result.error("BLUETOOTH_OFF", "Bluetooth is disabled", null)
            return
        }

        val device = adapter.getRemoteDevice(address)

        bluetoothGatt = device.connectGatt(
            context,
            autoConnect,
            gattCallback
        )

        result.success(true)
    }

    fun disconnect() {
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
        bluetoothGatt = null
    }

    private val gattCallback = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(
            gatt: BluetoothGatt,
            status: Int,
            newState: Int
        ) {

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d("BLE_GATT", "Connected")
                gatt.discoverServices()
            }

            if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d("BLE_GATT", "Disconnected")
            }
        }

        override fun onServicesDiscovered(
            gatt: BluetoothGatt,
            status: Int
        ) {
            Log.d("BLE_GATT", "Services discovered")
        }
    }
}
