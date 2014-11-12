package com.hatebyte.cynicalandroid.mysimpleservice.binder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.hatebyte.cynicalandroid.mysimpleservice.AbstractBoundServiceActivity;

public class BinderExtensionActivity extends AbstractBoundServiceActivity {

    // Activity methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

   @Override
    protected void onDestroy() {
       super.onDestroy();
       doUnbindService();
    }



    // Bind or Unbind the service
    @Override
    public void doBindService() {
        bindService(new Intent(this, BinderService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void doUnbindService() {
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }



    // ServiceConnection Interface(Delegate) object.
    private BinderService boundService = null;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            boundService = ((BinderService.LocalBinder) service).getService();
            Toast.makeText(getApplicationContext(), boundService.connectionStatus, Toast.LENGTH_SHORT).show();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(getApplicationContext(), boundService.connectionStatus, Toast.LENGTH_SHORT).show();
            boundService = null;
            isBound = false;
        }
    };


}
