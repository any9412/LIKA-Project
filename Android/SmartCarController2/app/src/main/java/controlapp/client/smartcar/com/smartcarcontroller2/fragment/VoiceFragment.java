package controlapp.client.smartcar.com.smartcarcontroller2.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import controlapp.client.smartcar.com.smartcarcontroller2.MainActivity;
import controlapp.client.smartcar.com.smartcarcontroller2.R;
import controlapp.client.smartcar.com.smartcarcontroller2.thread.ClientThread;

public class VoiceFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ClientThread mThread;

    public VoiceFragment() {
        // Required empty public constructor
    }

    public static VoiceFragment newInstance(String param1, String param2) {
        VoiceFragment fragment = new VoiceFragment();
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

    private void requestVoice() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "말해요");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 20);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        startActivityForResult(intent, 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voice, container, false);

        MainActivity context = (MainActivity) getContext();
        mThread = context.getClient();

        Button button = (Button) view.findViewById(R.id.voiceBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestVoice();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            for (int i = 0; i < results.size(); i++) {
                //text.append(results.get(i)+"\n");
            }
            String right_result=results.get(0);

            //앞/뒤/오른쪽/왼쪽 구분
            if(right_result.contains("앞")==true) {
                Toast.makeText(getContext(),"앞",Toast.LENGTH_SHORT).show();
                mThread.send("f");
            }else if(right_result.contains("뒤")==true){
                Toast.makeText(getContext(),"뒤",Toast.LENGTH_SHORT).show();
            }else if(right_result.contains("오른쪽")==true){
                Toast.makeText(getContext(),"오른쪽",Toast.LENGTH_SHORT).show();
            }else if(right_result.contains("왼쪽")==true){
                Toast.makeText(getContext(),"왼쪽",Toast.LENGTH_SHORT).show();
            }else if(right_result.contains("멈춰")==true) {
                Toast.makeText(getContext(),"멈춰",Toast.LENGTH_SHORT).show();
                mThread.send("k");
            } else{
                Toast.makeText(getContext(),"다시말해라!!!",Toast.LENGTH_SHORT).show();

            }


        }
    }
}
