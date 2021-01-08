package com.reactnativeescpossahaab

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import io.github.escposjava.print.Printer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

class BluetoothPrinter(address: String) : Printer {
    private val adapter: BluetoothAdapter
    private var printer: OutputStream? = null
    private val address: String

    @Throws(IOException::class)
    override fun open() {
        val device: BluetoothDevice = adapter.getRemoteDevice(address)
        @SuppressLint("MissingPermission") val socket: BluetoothSocket = device.createRfcommSocketToServiceRecord(SPP_UUID)
        socket.connect()
        printer = socket.getOutputStream()
    }

    override fun write(command: ByteArray?) {
        try {
            printer?.write(command)
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    override fun close() {
        printer?.close()
    }

    companion object {
        private val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }

    init {
        adapter = BluetoothAdapter.getDefaultAdapter()
        this.address = address
    }
}