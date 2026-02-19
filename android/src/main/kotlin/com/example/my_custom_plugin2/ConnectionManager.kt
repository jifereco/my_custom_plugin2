package com.example.my_custom_plugin2

import android.bluetooth.*
import android.content.Context
import android.util.Log
import io.flutter.plugin.common.MethodChannel
import java.util.UUID
import io.flutter.plugin.common.EventChannel

class ConnectionManager(
    private val context: Context
) {

private val SERVICE_UUID =
    UUID.fromString("12345678-1234-1234-1234-1234567890AB")

private val CHARACTERISTIC_RX =
    UUID.fromString("12345678-1234-1234-1234-1234567890AC")

private val CHARACTERISTIC_TX =
    UUID.fromString("12345678-1234-1234-1234-1234567890AD")


    private var bluetoothGatt: BluetoothGatt? = null
private var rxCharacteristic: BluetoothGattCharacteristic? = null
private var txCharacteristic: BluetoothGattCharacteristic? = null
private var notifyEventSink: EventChannel.EventSink? = null



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
    rxCharacteristic = null
    txCharacteristic = null
    }

fun sendCommand(command: String): Boolean {

    if (bluetoothGatt == null || rxCharacteristic == null) {
        Log.d("BLE_GATT", "Not ready to send")
        return false
    }

    rxCharacteristic!!.value = command.toByteArray()
    return bluetoothGatt!!.writeCharacteristic(rxCharacteristic)
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
            if (status != BluetoothGatt.GATT_SUCCESS) {
            Log.d("BLE_GATT", "Service discovery failed")
            return
        }

        Log.d("BLE_GATT", "Services discovered")

        val service = gatt.getService(SERVICE_UUID)

        if (service == null) {
            Log.d("BLE_GATT", "Service not found")
            return
        }

        rxCharacteristic = service.getCharacteristic(CHARACTERISTIC_RX)
        txCharacteristic = service.getCharacteristic(CHARACTERISTIC_TX)

        if (txCharacteristic != null) {

            gatt.setCharacteristicNotification(txCharacteristic, true)
val descriptor = txCharacteristic!!
    .getDescriptor(UUID.fromString(
        "00002902-0000-1000-8000-00805f9b34fb"
    ))

if (descriptor != null) {
    descriptor.value =
        BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
    gatt.writeDescriptor(descriptor)
    Log.d("BLE_GATT", "Notifications enabled")
} else {
    Log.d("BLE_GATT", "Descriptor not found")
}



        }

        }

override fun onCharacteristicChanged(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) {

        if (characteristic.uuid == CHARACTERISTIC_TX) {

            val message = String(characteristic.value)
            Log.d("BLE_GATT", "Received notify: $message")
            
        // 🔥 Ejecutar en Main Thread
        android.os.Handler(android.os.Looper.getMainLooper()).post {
            notifyEventSink?.success(message)
        }
    }

   }
   fun setNotifyEventSink(sink: EventChannel.EventSink?) {
    notifyEventSink = sink
  }
}
