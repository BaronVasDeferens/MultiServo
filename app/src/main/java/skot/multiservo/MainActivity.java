package skot.multiservo;

import android.app.FragmentManager;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

public class MainActivity extends AppCompatActivity implements SliderFragment.OnFragmentInteractionListener {

    SliderFragment sliderOne, sliderTwo, sliderThree, sliderFour, sliderFive;

    public BluetoothDevice device;
    public BluetoothSocket socket;

    public UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    final String hostAddress = "B8:27:EB:8A:FB:F7";  // rapsberry pi
    //final String hostAddress = "A4:17:31:D6:1E:E6";     // laptop

    ConnectThread connectThread;
    TransmitThread transmitThread;

    public Queue<Pair<String, Integer>> commandQueue;


    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        commandQueue = new ArrayBlockingQueue<>(512);


        FragmentManager fragManager = getFragmentManager();

        android.app.FragmentTransaction transaction = fragManager.beginTransaction();

        // NOTE: "Hot load" doesn't clear out old transactions

        sliderOne = SliderFragment.newInstance("s1", commandQueue);
        sliderTwo = SliderFragment.newInstance("s2", commandQueue);
        sliderThree = SliderFragment.newInstance("s3", commandQueue);
        sliderFour = SliderFragment.newInstance("s4", commandQueue);
        sliderFive = SliderFragment.newInstance("s5", commandQueue);

        transaction.add(R.id.activity_main, sliderOne);
        transaction.add(R.id.activity_main, sliderTwo);
        transaction.add(R.id.activity_main, sliderThree);
        transaction.add(R.id.activity_main, sliderFour);
        transaction.add(R.id.activity_main, sliderFive);
        transaction.commit();

        connectThread = new ConnectThread(this);
        connectThread.start();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        transmitThread.keepRunning = false;
        try {
            socket.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
}
