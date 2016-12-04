package controlapp.client.smartcar.com.smartcarcontroller2.thread;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by ChoiGunHee on 2016-11-16.
 */

public class TCPClient implements Runnable {

    @Override
    public void run() {
        try {
            // 서버의 주소를 로컬호스트라고 127.0.0.1로 하는 삽질은 하지말것 -_-;
            InetAddress serverAddr = InetAddress.getByName("172.24.1.1");

            Log.d("TCP", "C: Connecting...");
            Socket socket = new Socket(serverAddr, 9999);

            String message = "Hello from Client";
            try {
                Log.d("TCP", "C: Sending: '" + message + "'");
                PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);

                out.println(message);
                Log.d("TCP", "C: Sent.");
                Log.d("TCP", "C: Done.");

            } catch(Exception e) {
                Log.e("TCP", "S: Error", e);
            } finally {
                socket.close();
            }
        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
        }
    }

    public void send(String msg) {

    }
}
