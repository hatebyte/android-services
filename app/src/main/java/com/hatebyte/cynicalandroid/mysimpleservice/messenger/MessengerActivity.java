package com.hatebyte.cynicalandroid.mysimpleservice.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hatebyte.cynicalandroid.mysimpleservice.AbstractBoundServiceActivity;
import com.hatebyte.cynicalandroid.mysimpleservice.R;

public class MessengerActivity extends AbstractBoundServiceActivity {
    private final String TAG = "MessengerActivity : ";
    private Boolean isBound;


    // Activity methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        addExtraButton();
        extraButton.setText(R.string.button_extra_messegner);
        statusView.setText(R.string.textview_status);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }


    // Bind or Unbind the service
    @Override
    public void doBindService() {
        bindService(new Intent(this, MessengerService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void doUnbindService() {
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    @Override
    protected void extraButtonBehavior() {
        if (!isBound) {
            return;
        }
        Message msg = Message.obtain(null, MessengerService.MSG_SAY_HELLO, 0, 0);
        msg.replyTo = messenger;
        
        try {
            messenger.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    Messenger messenger = null;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);
            Log.i(TAG, "onServiceConnected, FROM INTERFACE BIND");
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected FROM INTERFACE UNBIND");
            messenger = null;
            isBound = false;
        }
    };


}
