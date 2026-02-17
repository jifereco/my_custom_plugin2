package com.example.my_custom_plugin2

import android.bluetooth.BluetoothAdapter

import android.bluetooth.le.*
import android.os.Handler
import android.os.Looper
import io.flutter.plugin.common.MethodChannel

class BleScanner(private val channel: MethodChannel) {

    private val devices = mutableMapOf<String, Map<String, Any>>()
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private var scanCallback: ScanCallback? = null

    fun scanForDevices(result: MethodChannel.Result, duration: Long = 5000L) {

        val adapter = BluetoothAdapter.getDefaultAdapter()



        bluetoothLeScanner = adapter.bluetoothLeScanner

        devices.clear()

        scanCallback = object : ScanCallback() {

            override fun onScanResult(callbackType: Int, resultScan: ScanResult) {

                val device: BluetoothDevice = resultScan.device
                val name = device.name ?: "Unknown"
                val address = device.address
                val rssi = resultScan.rssi

val bonded = when (device.bondState) {
    android.bluetooth.BluetoothDevice.BOND_BONDED -> true
    else -> false
}

                devices[address] = mapOf(
                    "name" to name,
                    "address" to address,
                    "rssi" to rssi,
                    "bonded" to bonded
                )
            }


            }
        }

        bluetoothLeScanner?.startScan(scanCallback)

        Handler(Looper.getMainLooper()).postDelayed({

            bluetoothLeScanner?.stopScan(scanCallback)

            val list = devices.values.toList()
            result.success(list)

        }, duration)
    }
}