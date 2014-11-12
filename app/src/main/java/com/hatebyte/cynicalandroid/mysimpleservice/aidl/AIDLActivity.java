package com.hatebyte.cynicalandroid.mysimpleservice.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hatebyte.cynicalandroid.mysimpleservice.AbstractBoundServiceActivity;
import com.hatebyte.cynicalandroid.mysimpleservice.R;
import com.hatebyte.cynicalandroid.mysimpleservice.aidl.GPXPoint;
import com.hatebyte.cynicalandroid.mysimpleservice.aidl.GPXService;
import com.hatebyte.cynicalandroid.mysimpleservice.aidl.IRemoteInterface;

public class AIDLActivity extends AbstractBoundServiceActivity {
    private static final String TAG = "com.hatebyte.cynicalandroid.mysimpleservice";

    // Activity methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        addExtraButton();

        extraButton.setText(R.string.button_extra_aidl);
        statusView.setText(R.string.textview_status);
    }

    @Override
    protected void extraButtonBehavior() {
        try {
            String info = "Info from remote: \n";
            Location loc = mRemoteInterface.getLastLocation();
            if (loc != null) {
                double lat = loc.getLatitude();
                double lon = loc.getLongitude();
                info += String.format("Last location = (%f, %f)\n", lat, lon);
            } else {
                info += "No last location yet.\n";
            }
            GPXPoint point = mRemoteInterface.getGPXPoint();
            if (point != null) {
                info += String.format("GPX point = (%d, %d) @ (%.1f meters) @ (%s)\n",
                        point.latitude,
                        point.longitude,
                        point.elevation,
                        point.timestamp.toString());
            }
            statusView.setText(info);
        } catch (RemoteException e) {
            Log.e("ServiceControl", "Call to remote interface failed.",	e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    // Bind or Unbind the service
    @Override
    public void doBindService() {
        Intent service = new Intent(GPXService.GPX_SERVICE);
        service.putExtra(GPXService.EXTRA_UPDATE_RATE, 5000);
        startService(service);
        bindService(new Intent(IRemoteInterface.class.getName()), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void doUnbindService() {
        Log.i(TAG, "MainActivity : releaseGPXBind");
        if (isBound) {
            stopService(new Intent(GPXService.GPX_SERVICE));
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    // service connect interface
    IRemoteInterface mRemoteInterface = null;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRemoteInterface = IRemoteInterface.Stub.asInterface(service);
            Log.i(TAG, "onServiceConnected, FROM INTERFACE BIND");
            extraButton.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), getText(R.string.aidl_toast_connected), Toast.LENGTH_SHORT).show();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected FROM INTERFACE UNBIND");
            mRemoteInterface = null;
            isBound = false;
            extraButton.setVisibility(View.GONE);
        }
    };

}
