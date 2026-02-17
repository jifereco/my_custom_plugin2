package com.example.my_custom_plugin2

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import io.flutter.plugin.common.MethodChannel

class BleScanner(
    private val context: Context,
    private val channel: MethodChannel
) {

    private val devices = mutableMapOf<String, Map<String, Any>>()
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private var scanCallback: ScanCallback? = null


    fun scanForDevices(result: MethodChannel.Result, duration: Long = 5000L) {

    val adapter = BluetoothAdapter.getDefaultAdapter()

    if (adapter == null) {
        result.error("NO_ADAPTER", "Bluetooth not supported", null)
        return
    }

    if (!adapter.isEnabled) {
        result.error("BLUETOOTH_OFF", "Bluetooth is disabled", null)
        return
    }

    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH_SCAN
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        result.error("NO_PERMISSION", "BLUETOOTH_SCAN not granted", null)
        return
    }

    bluetoothLeScanner = adapter.bluetoothLeScanner

    if (bluetoothLeScanner == null) {
        result.error("SCANNER_NULL", "Scanner is null", null)
        return
    }

    devices.clear()

    // 🔥 Obtener bonded reales del sistema
    val bondedMap = adapter.bondedDevices.associateBy { it.address }

    scanCallback = object : ScanCallback() {

        override fun onScanResult(callbackType: Int, resultScan: ScanResult) {

            val device = resultScan.device
            val address = device.address
            val name = device.name ?: "Unknown"
            val rssi = resultScan.rssi

            val systemDevice = bondedMap[address]
            val bondState = systemDevice?.bondState ?: BluetoothDevice.BOND_NONE

            devices[address] = mapOf(
                "name" to name,
                "address" to address,
                "rssi" to rssi,
                "bondState" to bondState,
                "bonded" to (bondState == BluetoothDevice.BOND_BONDED)
            )
        }

        override fun onScanFailed(errorCode: Int) {
            result.error("SCAN_FAILED", "Error code: $errorCode", null)
        }
    }

    bluetoothLeScanner?.startScan(scanCallback)

    Handler(Looper.getMainLooper()).postDelayed({

        bluetoothLeScanner?.stopScan(scanCallback)

        result.success(devices.values.toList())

    }, duration)
}
}
