package controlapp.client.smartcar.com.smartcarcontroller2.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import controlapp.client.smartcar.com.smartcarcontroller2.MainActivity;
import controlapp.client.smartcar.com.smartcarcontroller2.R;
import controlapp.client.smartcar.com.smartcarcontroller2.thread.ClientThread;

public class ElectromyogramFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_ENABLE_BT = 8161;

    private String mParam1;
    private String mParam2;

    private ClientThread mThread;

    private Context mContext;

    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> mDevices;
    private BluetoothSocket mSocket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private Thread mWorkerThread;

    private boolean isStopClicked = false;

    public ElectromyogramFragment() {
        // Required empty public constructor
    }

    public static ElectromyogramFragment newInstance(String param1, String param2) {
        ElectromyogramFragment fragment = new ElectromyogramFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_electromyogram, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        mThread = mainActivity.getClient();
        mContext = getContext();

        Button bluetoothBtn = (Button) view.findViewById(R.id.bluetoothConnectBtn);
        bluetoothBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", "blue tooth connect button click");
                checkBluetooth();
                setDevice();
            }
        });

        final Button stopBtn = (Button) view.findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(!isStopClicked) {
                 isStopClicked = true;
                 stopBtn.setText("START");
                 mThread.send("k");
                 onDestroy();
             } else {
                 isStopClicked = false;
                 stopBtn.setText("STOP");
                 mThread.send("f");
             }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void checkBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null) {
            // 장치가 블루투스를 지원하지 않는 경우
            // Dialog

        } else {
            // 장치가 블루투스를 지원하는 경우
            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBtnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtnIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_ENABLE_BT :
                if(resultCode == Activity.RESULT_OK) {
                    // 블루투스 활성
                }  else if(resultCode == Activity.RESULT_CANCELED) {
                    // 블루투스 비활성
                }
            break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setDevice() {
        mDevices = mBluetoothAdapter.getBondedDevices();
        if(mDevices.size() == 0) {
            // 페어링된 장치가 없는 경우
            Toast.makeText(mContext, "No device connected", Toast.LENGTH_SHORT).show();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("블루투스 장치 선택");

        // 페어링 된 블루투스 장치의 이름 목록 작성
        List<String> listItems = new ArrayList<String>();
        for(BluetoothDevice device : mDevices) {
            listItems.add(device.getName());
        }
            listItems.add("취소");    // 취소 항목 추가

            final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);

            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    if (item == mDevices.size()) {
                        // 연결할 장치를 선택하지 않고 '취소'를 누른 경우

                    } else {
                        // 연결할 장치를 선택한 경우
                        // 선택한 장치와 연결을 시도함
                        connectToSelectedDevices(items[item].toString());
                    }
                }
            });


            builder.setCancelable(false);    // 뒤로 가기 버튼 사용 금지
            AlertDialog alert = builder.create();
            alert.show();
    }

    BluetoothDevice getDeviceFromBondedList(String name) {
        BluetoothDevice selectedDevice = null;

        for(BluetoothDevice device : mDevices) {
            if(name.equals(device.getName())) {
                selectedDevice = device;
                break;
            }
        }
        return selectedDevice;
    }


    public void connectToSelectedDevices(String selectedDeviceName) {
        BluetoothDevice mRemoteDevice = getDeviceFromBondedList(selectedDeviceName);
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        try {
            // 소켓 생성
            mSocket = mRemoteDevice.createRfcommSocketToServiceRecord(uuid);
            // RFCOMM 채널을 통한 연결
            mSocket.connect();

            Toast.makeText(mContext, "connected to " + selectedDeviceName, Toast.LENGTH_SHORT).show();

            // 데이터 송수신을 위한 스트림 열기
            mInputStream = mSocket.getInputStream();
            mOutputStream = mSocket.getOutputStream();

            // 데이터 수신 준비
            beginListenForData();
        }catch(Exception e) {
            // 블루투스 연결 중 오류 발생
        }
    }

    @Override
    public void onDestroy() {

        Toast.makeText(mContext, String.valueOf(isStopClicked), Toast.LENGTH_SHORT).show();

        try {
            mWorkerThread.interrupt();   // 데이터 수신 쓰레드 종료
            mInputStream.close();
            mOutputStream.close();
            mSocket.close();
        } catch(Exception e) { }

        super.onDestroy();
    }

    void beginListenForData()
    {
        final Handler handler = new Handler();
        // 문자열 수신 쓰레드
        mWorkerThread = new Thread(new Runnable() {
            public void run() {
                while(!Thread.currentThread().isInterrupted()){
                    try {
                        int bytesAvailable = mInputStream.available();    // 수신 데이터 확인
                        if(bytesAvailable > 0) {                     // 데이터가 수신된 경우
                            byte[] packetBytes = new byte[bytesAvailable];
                            mInputStream.read(packetBytes);
                            for(int i=0 ; i<bytesAvailable; i++) {
                                final byte b = packetBytes[i];

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext, ((char) b) + "", Toast.LENGTH_SHORT).show();
                                        Log.d("BTSerial", ((char) b) + "");
                                        mThread.send(((char) b) + "");
                                    }
                                });

                            }
                        }
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        mWorkerThread.start();
    }

    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
