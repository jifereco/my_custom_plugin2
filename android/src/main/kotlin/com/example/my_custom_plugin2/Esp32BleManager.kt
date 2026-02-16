package com.example.my_custom_plugin2

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import java.util.UUID
import no.nordicsemi.android.ble.BleManager

class Esp32BleManager(context: Context) : BleManager(context) {

    companion object {
        val SERVICE_UUID: UUID = UUID.fromString("0000BAAD-0000-1000-8000-00805F9B34FB")
        val CHAR_UUID: UUID = UUID.fromString("0000F00D-0000-1000-8000-00805F9B34FB")
    }

    private var foodCharacteristic: BluetoothGattCharacteristic? = null

    override fun getGattCallback(): BleManager.BleManagerGattCallback {
        return object : BleManager.BleManagerGattCallback() {

            override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {

                val service = gatt.getService(SERVICE_UUID)
                foodCharacteristic = service?.getCharacteristic(CHAR_UUID)

                return foodCharacteristic != null
            }

            override fun initialize() {
                foodCharacteristic?.let { characteristic ->

                    setNotificationCallback(characteristic)
                        .with { _, data ->
                            val value = data.value
                            println("DATA RECEIVED: ${value?.decodeToString()}")
                        }

                    enableNotifications(characteristic).enqueue()
                }
            }

            override fun onServicesInvalidated() {
                foodCharacteristic = null
            }
        }
    }

    fun write(data: ByteArray) {
        foodCharacteristic?.let {
            writeCharacteristic(it, data).enqueue()
        }
    }
}
