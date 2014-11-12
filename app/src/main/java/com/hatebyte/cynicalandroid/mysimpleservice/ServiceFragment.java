package com.hatebyte.cynicalandroid.mysimpleservice;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


public class ServiceFragment extends Fragment {

    public interface Controls {

        public void doBindService();
        public void doUnbindService();

    }

    protected AbstractBoundServiceActivity delegate;
    public LinearLayout linearLayout;
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        delegate = (AbstractBoundServiceActivity)getActivity();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service_controls, parent, false);

        linearLayout = (LinearLayout)v.findViewById(R.id.linearLayout);

        Button startButton = (Button) v.findViewById(R.id.doStartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                delegate.doBindService();
            }

        });

        Button stopButton = (Button) v.findViewById(R.id.doStopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                delegate.doUnbindService();
            }
        });

        return v;
    }

}
