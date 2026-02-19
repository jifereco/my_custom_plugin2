package com.example.my_custom_plugin2

import android.app.Activity
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.bluetooth.BluetoothDevice
import android.content.Context
import io.flutter.plugin.common.EventChannel
import android.util.Log


class MyCustomPlugin2Plugin :
    FlutterPlugin,
    MethodCallHandler,
    ActivityAware {

    private lateinit var channel: MethodChannel
    private var activity: Activity? = null
    private var bleScanner: BleScanner? = null
    private lateinit var bondManager: BondManager
    private var bondReceiver: BroadcastReceiver? = null
    private var bondEventSink: EventChannel.EventSink? = null
    private lateinit var context: Context
    private lateinit var connectionManager: ConnectionManager




override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {

    channel = MethodChannel(binding.binaryMessenger, "my_custom_plugin2")
    channel.setMethodCallHandler(this)

    bleScanner = BleScanner(binding.applicationContext, channel)
    bondManager = BondManager()
    context = binding.applicationContext

    connectionManager = ConnectionManager(context)

    // 🔹 Bond Stream
    val bondChannel = EventChannel(
        binding.binaryMessenger,
        "my_custom_plugin2/bond_stream"
    )

    bondChannel.setStreamHandler(object : EventChannel.StreamHandler {

        override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
            Log.d("BLE_DEBUG", "EventChannel onListen called")
            bondEventSink = events
            registerBondReceiver()
        }

        override fun onCancel(arguments: Any?) {
            bondReceiver?.let {
                context.unregisterReceiver(it)
            }
            bondEventSink = null
        }
    })

    // 🔹 Notify Stream
    val notifyChannel = EventChannel(
        binding.binaryMessenger,
        "my_custom_plugin2/notify_stream"
    )

    notifyChannel.setStreamHandler(object : EventChannel.StreamHandler {

        override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
            connectionManager.setNotifyEventSink(events)
        }

        override fun onCancel(arguments: Any?) {
            connectionManager.setNotifyEventSink(null)
        }
    })
}





    }

    override fun onMethodCall(call: MethodCall, result: Result) {

        when (call.method) {

            "getPlatformVersion" -> {
                result.success("Android ${android.os.Build.VERSION.RELEASE}")
            }

            "scanDevices" -> {
                if (activity == null) {
                    result.error("NO_ACTIVITY", "Activity not attached", null)
                    return
                }

                if (bleScanner == null) {
                    bleScanner = BleScanner(activity!!, channel)
                }

                bleScanner?.scanForDevices(result)
              }

            "getBondedDevices" -> {
                bondManager.getBondedDevices(result)

            }

"connectDevice" -> {
    val address = call.argument<String>("address")
    val autoConnect = call.argument<Boolean>("autoConnect") ?: false

    if (address != null) {
        connectionManager.connect(address, autoConnect, result)
    } else {
        result.error("NO_ADDRESS", "Address is null", null)
    }
}

"disconnectDevice" -> {
    connectionManager.disconnect()
    result.success(true)
}

"sendCommand" -> {
    val command = call.argument<String>("command")

    if (command != null) {
        val success = connectionManager.sendCommand(command)
        result.success(success)
    } else {
        result.error("NO_COMMAND", "Command null", null)
    }
}



            else -> result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
  // connectionManager.disconnect() cerra conexion si existe
    }

    // 🔥 ActivityAware implementation

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activity = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivity() {
        activity = null
    }



private fun registerBondReceiver() {

    bondReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {

            // 👇 AGREGAR AQUÍ
            Log.d("BLE_DEBUG", "Broadcast recibido: ${intent?.action}")

            if (intent?.action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {

                // 👇 Y TAMBIÉN AQUÍ (más específico)
                Log.d("BLE_DEBUG", "Bond state changed broadcast received")

                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

                val bondState =
                    intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)

                Log.d("BLE_DEBUG", "Nuevo bondState: $bondState")

                device?.let {
                    bondEventSink?.success(
                        mapOf(
                            "address" to it.address,
                            "bondState" to bondState
                        )
                    )
                }
            }
        }
    }

    val filter = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
    Log.d("BLE_DEBUG", "Bond receiver registered")
    context.registerReceiver(bondReceiver, filter)
}



}
