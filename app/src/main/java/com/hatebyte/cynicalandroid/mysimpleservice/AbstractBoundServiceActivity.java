package com.hatebyte.cynicalandroid.mysimpleservice;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by scott on 11/8/14.
 */
public class AbstractBoundServiceActivity extends Activity implements ServiceFragment.Controls {
    private static final String TAG = "com.hatebyte.cynicalandroid.mysimpleservice.AbstractBoundServiceActivity";

    protected Boolean isBound;
    protected Button extraButton;
    protected TextView statusView;

    public void doBindService() {}
    public void doUnbindService() {}

    protected ServiceFragment controls;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       isBound = false;
       setContentView(R.layout.activity_service);

       FragmentManager fm = getFragmentManager();
       controls= (ServiceFragment)fm.findFragmentById(R.id.fragmentContainer);
       if (controls == null) {
           controls = new ServiceFragment();
           fm.beginTransaction()
                   .add(R.id.fragmentContainer, controls)
                   .commit();
       }
   }

    protected void extraButtonBehavior() {}
    protected void addExtraButton() {
        LinearLayout linearLayout = (LinearLayout)controls.getView().findViewById(R.id.linearLayout);

        if (extraButton == null) {

            extraButton = new Button(this);
            statusView = new TextView(this);


            linearLayout.addView(extraButton);
            linearLayout.addView(statusView);

            extraButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    extraButtonBehavior();
                }
            });
        }
    }

}
