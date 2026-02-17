package com.example.my_custom_plugin2

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
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

                // 🔥 Forma correcta estilo Nordic
                val bonded = device.bondState == BluetoothDevice.BOND_BONDED

                Log.d("BLE", "Device: $address bondState=${device.bondState}")

                devices[address] = mapOf(
                    "name" to name,
                    "address" to address,
                    "rssi" to rssi,
                    "bonded" to bonded
                )
            }

            override fun onScanFailed(errorCode: Int) {
                result.error("SCAN_FAILED", "Error code: $errorCode", null)
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