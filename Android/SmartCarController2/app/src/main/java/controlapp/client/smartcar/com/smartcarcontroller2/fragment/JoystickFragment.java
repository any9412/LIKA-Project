package controlapp.client.smartcar.com.smartcarcontroller2.fragment;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import controlapp.client.smartcar.com.smartcarcontroller2.MainActivity;
import controlapp.client.smartcar.com.smartcarcontroller2.R;
import controlapp.client.smartcar.com.smartcarcontroller2.thread.ClientThread;

public class JoystickFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ClientThread mThread;

    public JoystickFragment() {
        // Required empty public constructor
    }

    public static JoystickFragment newInstance(String param1, String param2) {
        JoystickFragment fragment = new JoystickFragment();
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

        View view = inflater.inflate(R.layout.fragment_joystick, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        mThread = mainActivity.getClient();

        Button frontBtn = (Button) view.findViewById(R.id.frontBtn);
        frontBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    view.setBackgroundColor(Color.BLACK);
                    mThread.send("f");
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.setBackgroundColor(Color.BLUE);
                    mThread.send("k");
                }
                return false;
            }
        });

        Button backBtn = (Button) view.findViewById(R.id.backBtn);
        backBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    view.setBackgroundColor(Color.BLACK);
                    mThread.send("b");
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.setBackgroundColor(Color.BLUE);
                    mThread.send("k");
                }
                return false;
            }
        });

        Button leftBtn = (Button) view.findViewById(R.id.leftBtn);
        leftBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    view.setBackgroundColor(Color.BLACK);
                    mThread.send("l");
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.setBackgroundColor(Color.BLUE);
                    mThread.send("s");
                }
                return false;
            }
        });

        Button rifhtBtn = (Button) view.findViewById(R.id.rightBtn);
        rifhtBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    view.setBackgroundColor(Color.BLACK);
                    mThread.send("r");
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.setBackgroundColor(Color.BLUE);
                    mThread.send("s");
                }
                return false;
            }
        });

        return view;
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
