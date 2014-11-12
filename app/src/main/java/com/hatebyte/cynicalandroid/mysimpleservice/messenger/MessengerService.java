package com.hatebyte.cynicalandroid.mysimpleservice.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

import android.os.Handler;

import com.hatebyte.cynicalandroid.mysimpleservice.R;

public class MessengerService extends Service {
    public static final int MSG_SAY_HELLO = 1;

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    Toast.makeText(getApplicationContext(), "hello!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }


    final Messenger messenger = new Messenger(new IncomingHandler());

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), getText(R.string.messenger_toast_connected), Toast.LENGTH_LONG).show();
        return messenger.getBinder();
    }


    public MessengerService() {
    }


}