package sahaab.reactnativeescpos;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import io.github.escposjava.print.Printer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothPrinter implements Printer {
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter adapter;

    private OutputStream printer = null;
    private final String address;

    public BluetoothPrinter(String address) {
        adapter = BluetoothAdapter.getDefaultAdapter();
        this.address = address;
    }

    public void open() throws IOException {
        BluetoothDevice device = adapter.getRemoteDevice(address);
        @SuppressLint("MissingPermission") BluetoothSocket socket = device.createRfcommSocketToServiceRecord(SPP_UUID);
        socket.connect();
        printer = socket.getOutputStream();
    }

    public void write(byte[] command) {
        try {
            printer.write(command);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        printer.close();
    }
}
