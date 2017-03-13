package skot.multiservo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Looper;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;

class ConnectThread extends Thread {

    MainActivity activity;
    BluetoothAdapter adapter;
    BluetoothDevice device;
    BluetoothSocket socket;
    OutputStream out;


    public ConnectThread(MainActivity activity) {
        this.activity = activity;
        adapter = BluetoothAdapter.getDefaultAdapter();
        adapter.startDiscovery();
    }

    public void run() {

        System.out.println(">>> ConnectThread START...");

        try {
            device = adapter.getRemoteDevice(activity.hostAddress);
            socket = device.createInsecureRfcommSocketToServiceRecord(activity.myUUID);
            socket.connect();

            if (socket != null) {

                out = socket.getOutputStream();
                activity.transmitThread = new TransmitThread(out, activity.commandQueue);
                activity.transmitThread.start();

                activity.device = device;
                activity.socket = socket;

                Looper.prepare();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "Connection established: " + device.getName(), Toast.LENGTH_LONG).show();
                    }
                });

                System.out.println(">>> Connection established!");
            }
            else {
                System.err.println(">>> Failed to establish connection!");
            }

        } catch (IOException connectException) {
            System.out.println(">>> DERP! " + connectException.toString());
            connectException.printStackTrace();
        }
    }

}