package skot.multiservo;

import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Queue;

/**
 * Created by skot on 12/31/16.
 */

class TransmitThread extends Thread {

    OutputStream out = null;
    Queue<Pair<String,Integer>> commandQueue;
    public boolean keepRunning = false;


    public TransmitThread (OutputStream out, Queue<Pair<String,Integer>> commandQueue) {
        this.out = out;
        this.commandQueue = commandQueue;
        keepRunning = true;
    }

    public void run() {

        System.out.println(">>> TransmitThread START...");

        while (keepRunning) {

            if (commandQueue.peek() != null) {
                Pair<String,Integer> command = commandQueue.poll();

                try {
                    JSONObject cmd = new JSONObject();
                    cmd.put("servo", command.first);
                    cmd.put("position", command.second);
                    out.write(cmd.toString().getBytes());
                    out.flush();
                    System.out.println("eventsInQueue: " + commandQueue.size());
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                catch(JSONException je) {
                    je.printStackTrace();
                }
            }
        }

        try {
            out.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

        System.out.println(">>> TransmitThread END...");
    }

}
