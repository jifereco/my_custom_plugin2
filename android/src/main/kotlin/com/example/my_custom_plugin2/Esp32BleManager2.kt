package com.example.my_custom_plugin2

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import no.nordicsemi.android.ble.BleManager

class Esp32BleManager2(context: Context) : BleManager(context) {

    private var rxCharacteristic: BluetoothGattCharacteristic? = null
    private var txCharacteristic: BluetoothGattCharacteristic? = null

    override fun getGattCallback(): BleManager.BleManagerGattCallback {
        return object : BleManager.BleManagerGattCallback() {

            override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
                return true
            }

            override fun initialize() {
            }

            override fun onServicesInvalidated() {
                rxCharacteristic = null
                txCharacteristic = null
            }
        }
    }

    fun write(data: ByteArray) {
        txCharacteristic?.let {
            writeCharacteristic(it, data).enqueue()
        }
    }
}
