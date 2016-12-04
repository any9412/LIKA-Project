package controlapp.client.smartcar.com.smartcarcontroller2.thread;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by ChoiGunHee on 2016-11-17.
 */

public class ClientThread extends Thread {

    private final Context context;
    private final Handler handler;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String data;

    public ClientThread(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    public void run() {

        InetAddress serverAddr = null;
        try {
            serverAddr = InetAddress.getByName("172.24.1.1");
            Log.d("TCP", "C: Connecting...");
            socket = new Socket(serverAddr, 9999);
            out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            try {
                while (true) {
                    data = in.readLine();
                    Log.d("serverSend", data);
                }
            } catch (Exception e) {
                Log.d("TCP", "read error");
                e.printStackTrace();
            }

        } catch (IOException e) {
            Log.e("TCP", "S: Error", e);
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
            e.printStackTrace();
        }

    }

    public void send(String msg) {
        out.println(msg);
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
