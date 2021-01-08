package com.reactnativeescpossahaab

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.util.HashMap
import java.util.Map
import io.github.escposjava.print.NetworkPrinter
import io.github.escposjava.print.Printer
import io.github.escposjava.print.exceptions.BarcodeSizeError
import io.github.escposjava.print.exceptions.QRCodeException

class EscPosSahaabModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    private val reactContext: ReactApplicationContext
    private var printerService: PrinterService? = null
    private var config: ReadableMap? = null
    private val scanManager: ScanManager

    internal enum class BluetoothEvent {
        CONNECTED, DISCONNECTED, DEVICE_FOUND, NONE
    }

    public override fun getConstants() : MutableMap<String, Any> {
            val constants: MutableMap<String, Any> = HashMap()
            constants.put(PRINTING_SIZE_58_MM, PRINTING_SIZE_58_MM)
            constants.put(PRINTING_SIZE_76_MM, PRINTING_SIZE_76_MM)
            constants.put(PRINTING_SIZE_80_MM, PRINTING_SIZE_80_MM)
            constants.put(BLUETOOTH_CONNECTED, BluetoothEvent.CONNECTED.name)
            constants.put(BLUETOOTH_DISCONNECTED, BluetoothEvent.DISCONNECTED.name)
            constants.put(BLUETOOTH_DEVICE_FOUND, BluetoothEvent.DEVICE_FOUND.name)
            return constants
        }

    public override fun getName() : String {
        return "EscPosSahaab"
    }

    @ReactMethod
    fun cutPart(promise: Promise) {
        printerService?.cutPart()
        promise.resolve(true)
    }

    @ReactMethod
    fun cutFull(promise: Promise) {
        printerService?.cutFull()
        promise.resolve(true)
    }

    @ReactMethod
    fun lineBreak(promise: Promise) {
        printerService?.lineBreak()
        promise.resolve(true)
    }

    @ReactMethod
    fun print(text: String, promise: Promise) {
        try {
            printerService?.print(text)
            promise.resolve(true)
        } catch (e: UnsupportedEncodingException) {
            promise.reject(e)
        }
    }

    @ReactMethod
    fun printLn(text: String, promise: Promise) {
        try {
            printerService?.printLn(text)
            promise.resolve(true)
        } catch (e: UnsupportedEncodingException) {
            promise.reject(e)
        }
    }

    @ReactMethod
    fun printBarcode(code: String, bc: String, width: Int, height: Int, pos: String, font: String, promise: Promise) {
        try {
            printerService?.printBarcode(code, bc, width, height, pos, font)
            promise.resolve(true)
        } catch (e: BarcodeSizeError) {
            promise.reject(e)
        }
    }

    @ReactMethod
    fun printBarcode(str: String, nType: Int, nWidthX: Int, nHeight: Int, nHriFontType: Int, nHriFontPosition: Int, promise: Promise) {
        try {
            printerService?.printBarcode(str, nType, nWidthX, nHeight, nHriFontType, nHriFontPosition)
            promise.resolve(true)
        } catch (e: Exception) {
            promise.reject(e)
        }
    }

    @ReactMethod
    fun printDesign(text: String, promise: Promise) {
        try {
            printerService?.printDesign(text)
            promise.resolve(true)
        } catch (e: IOException) {
            promise.reject(e)
        }
    }

    @ReactMethod
    fun printImage(filePath: String, promise: Promise) {
        try {
            printerService?.printImage(filePath)
            promise.resolve(true)
        } catch (e: IOException) {
            promise.reject(e)
        }
    }

    @ReactMethod
    fun printImageWithOffset(filePath: String, widthOffet: Int, promise: Promise) {
        try {
            printerService?.printImage(filePath, widthOffet)
            promise.resolve(true)
        } catch (e: IOException) {
            promise.reject(e)
        }
    }

    @ReactMethod
    fun printQRCode(value: String, size: Int, promise: Promise) {
        try {
            printerService?.printQRCode(value, size)
            promise.resolve(true)
        } catch (e: QRCodeException) {
            promise.reject(e)
        }
    }

    @ReactMethod
    fun printSample(promise: Promise) {
        try {
            printerService?.printSample()
            promise.resolve(true)
        } catch (e: IOException) {
            promise.reject(e)
        }
    }

    @ReactMethod
    fun write(command: ByteArray?, promise: Promise) {
        printerService?.write(command)
        promise.resolve(true)
    }

    @ReactMethod
    fun setCharCode(code: String) {
        printerService?.setCharCode(code)
    }

    @ReactMethod
    fun setCharCodePage(page: Int) {
        printerService?.setCharCodePage(page)
    }

    @ReactMethod
    fun setTextDensity(density: Int) {
        printerService?.setTextDensity(density)
    }

    @ReactMethod
    fun setPrintingSize(printingSize: String) {
        val charsOnLine: Int
        val printingWidth: Int
        when (printingSize) {
            PRINTING_SIZE_80_MM -> {
                charsOnLine = LayoutBuilder.CHARS_ON_LINE_80_MM
                printingWidth = PrinterService?.PRINTING_WIDTH_80_MM
            }
            PRINTING_SIZE_76_MM -> {
                charsOnLine = LayoutBuilder.CHARS_ON_LINE_76_MM
                printingWidth = PrinterService?.PRINTING_WIDTH_76_MM
            }
            PRINTING_SIZE_58_MM -> {
                charsOnLine = LayoutBuilder.CHARS_ON_LINE_58_MM
                printingWidth = PrinterService?.PRINTING_WIDTH_58_MM
            }
            else -> {
                charsOnLine = LayoutBuilder.CHARS_ON_LINE_58_MM
                printingWidth = PrinterService?.PRINTING_WIDTH_58_MM
            }
        }
        printerService?.setCharsOnLine(charsOnLine)
        printerService?.setPrintingWidth(printingWidth)
    }

    @ReactMethod
    fun beep(promise: Promise) {
        printerService?.beep()
        promise.resolve(true)
    }

    @ReactMethod
    fun setConfig(config: ReadableMap?) {
        this.config = config
    }

    @ReactMethod
    fun kickCashDrawerPin2(promise: Promise) {
        printerService?.kickCashDrawerPin2()
        promise.resolve(true)
    }

    @ReactMethod
    fun kickCashDrawerPin5(promise: Promise) {
        printerService?.kickCashDrawerPin5()
        promise.resolve(true)
    }

    @ReactMethod
    fun connectBluetoothPrinter(address: String, promise: Promise) {
        try {
            if (!"bluetooth".equals(config?.getString("type"))) {
                promise.reject("config?.type is not a bluetooth type")
            }
            val printer: Printer = BluetoothPrinter(address)
            printerService = PrinterService(printer)
            printerService?.setContext(reactContext)
            promise.resolve(true)
        } catch (e: IOException) {
            promise.reject(e)
        }
    }

    @ReactMethod
    fun connectNetworkPrinter(address: String, port: Int, promise: Promise) {
        try {
            if (!"network".equals(config?.getString("type"))) {
                promise.reject("config?.type is not a network type")
            }
            val printer: Printer = NetworkPrinter(address, port)
            printerService = PrinterService(printer)
            printerService?.setContext(reactContext)
            promise.resolve(true)
        } catch (e: IOException) {
            promise.reject(e)
        }
    }

    @ReactMethod
    fun disconnect(promise: Promise) {
        try {
            printerService?.close()
            promise.resolve(true)
        } catch (e: IOException) {
            promise.reject(e)
        }
    }

    @SuppressWarnings("MissingPermission")
    @ReactMethod
    fun scanDevices() {
        scanManager.registerCallback(object : ScanManager.OnBluetoothScanListener {
            override fun deviceFound(bluetoothDevice: BluetoothDevice) {
                val deviceInfoParams: WritableMap = Arguments.createMap()
                deviceInfoParams.putString("name", bluetoothDevice.getName())
                deviceInfoParams.putString("macAddress", bluetoothDevice.getAddress())

                // put deviceInfoParams into callbackParams
                val callbackParams: WritableMap = Arguments.createMap()
                callbackParams.putMap("deviceInfo", deviceInfoParams)
                callbackParams.putString("state", BluetoothEvent.DEVICE_FOUND.name)

                // emit callback to RN code
                reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                        .emit("bluetoothDeviceFound", callbackParams)
            }
        })
        scanManager.startScan()
    }

    @ReactMethod
    fun stopScan() {
        scanManager.stopScan()
    }

    @ReactMethod
    fun initBluetoothConnectionListener() {
        // Add listener when bluetooth conencted
        reactContext.registerReceiver(bluetoothConnectionEventListener,
                IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED))

        // Add listener when bluetooth disconnected
        reactContext.registerReceiver(bluetoothConnectionEventListener,
                IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED))
    }

    /**
     * Bluetooth Connection Event Listener
     */
    @SuppressWarnings("MissingPermission")
    private val bluetoothConnectionEventListener: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val callbackAction: String = intent.getAction().toString()
            val bluetoothDevice: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) as BluetoothDevice

            // if action or bluetooth data is null
            if (callbackAction == null || bluetoothDevice == null) {
                // do not proceed
                return
            }

            // hold value for bluetooth event
            val bluetoothEvent: BluetoothEvent
            bluetoothEvent = when (callbackAction) {
                BluetoothDevice.ACTION_ACL_CONNECTED -> BluetoothEvent.CONNECTED
                BluetoothDevice.ACTION_ACL_DISCONNECTED -> BluetoothEvent.DISCONNECTED
                else -> BluetoothEvent.NONE
            }

            // bluetooth event must not be null
            if (bluetoothEvent != BluetoothEvent.NONE) {
                // extract bluetooth device info and put in deviceInfoParams
                val deviceInfoParams: WritableMap = Arguments.createMap()
                deviceInfoParams.putString("name", bluetoothDevice.getName())
                deviceInfoParams.putString("macAddress", bluetoothDevice.getAddress())

                // put deviceInfoParams into callbackParams
                val callbackParams: WritableMap = Arguments.createMap()
                callbackParams.putMap("deviceInfo", deviceInfoParams)
                callbackParams.putString("state", bluetoothEvent.name)

                // emit callback to RN code
                reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                        .emit("bluetoothStateChanged", callbackParams)
            }
        }
    }

    companion object {
        const val PRINTING_SIZE_58_MM = "PRINTING_SIZE_58_MM"
        const val PRINTING_SIZE_76_MM = "PRINTING_SIZE_76_MM"
        const val PRINTING_SIZE_80_MM = "PRINTING_SIZE_80_MM"
        const val BLUETOOTH_CONNECTED = "BLUETOOTH_CONNECTED"
        const val BLUETOOTH_DISCONNECTED = "BLUETOOTH_DISCONNECTED"
        const val BLUETOOTH_DEVICE_FOUND = "BLUETOOTH_DEVICE_FOUND"
    }

    init {
        this.reactContext = reactContext
        scanManager = ScanManager(reactContext, BluetoothAdapter.getDefaultAdapter())
    }

    // Example method
    // See https://reactnative.dev/docs/native-modules-android
    @ReactMethod
    fun multiply(a: Int, b: Int, promise: Promise) {
    
      promise.resolve(a * b)
    
    }

    
}
