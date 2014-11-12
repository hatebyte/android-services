package com.hatebyte.cynicalandroid.mysimpleservice.aidl;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.hatebyte.cynicalandroid.mysimpleservice.R;

import java.util.Date;
import java.util.Locale;

public class GPXService extends Service {
    public static final String TAG = "com.hatebyte.cynicalandroid.GPXService";
    private static final int GPS_NOTIFY = 0x2001;
    private static final String DEBUG_TAG = "GPXService";
    public static final String EXTRA_UPDATE_RATE = "update-rate";
    public static final String GPX_SERVICE = "com.hatebyte.cynicalandroid.mysimpleservice.aidl.GPXService.SERVICE";
    private LocationManager location = null;
    private NotificationManager notifier = null;
    private int updateRate = -1;
    public int connectionStatus = R.string.aidl_toast_disconnected;

    @Override
    public void onCreate() {
        super.onCreate();
        connectionStatus= R.string.aidl_toast_connected;
        location = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        notifier = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(DEBUG_TAG, "onStartCommand() called, must be L5 or later");
        if (flags != 0) {
            Log.w(DEBUG_TAG, "Redelivered or retrying service start: " + flags);
        }

        updateRate = intent.getIntExtra(EXTRA_UPDATE_RATE, -1);
        if (updateRate == -1) {
            updateRate = 60000;
        }

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.NO_REQUIREMENT);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        location = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String best = location.getBestProvider(criteria, true);
        Log.v(TAG, "THIS IS BEST : "+ best); // "network"
        location.requestLocationUpdates(LocationManager.GPS_PROVIDER, updateRate, 0, trackListener);

        notifier.cancel(GPS_NOTIFY);

        Intent toLaunch = new Intent(getApplicationContext(), AIDLActivity.class);
        PendingIntent intentBack = PendingIntent.getActivity(getApplicationContext(), 0, toLaunch, 0);

        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setTicker("Builder GPS Tracking");
        builder.setSmallIcon(android.R.drawable.stat_notify_more);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("Builder GPS Tracking");
        builder.setContentText("Tracking start at "+ updateRate + "ms intervals with [" + best + "] as the provider");
        builder.setContentIntent(intentBack);
        builder.setAutoCancel(true);

        Notification notify = builder.build();
        notifier.notify(GPS_NOTIFY, notify);

        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        connectionStatus= R.string.aidl_toast_disconnected;
        Toast.makeText(getApplicationContext(), connectionStatus, Toast.LENGTH_SHORT).show();
        Log.v(DEBUG_TAG, "onDestroy() called");
        if (location != null) {
            location.removeUpdates(trackListener);
            location = null;
        }

        notifier.cancel(GPS_NOTIFY);

        Intent toLaunch = new Intent(getApplicationContext(), AIDLActivity.class);
        PendingIntent intentBack = PendingIntent.getActivity(getApplicationContext(), 0, toLaunch, 0);

        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setTicker("Builder GPS Tracking");
        builder.setSmallIcon(android.R.drawable.stat_notify_more);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("Builder GPS Tracking");
        builder.setContentText("Tracking stopped");
        builder.setContentIntent(intentBack);
        builder.setAutoCancel(true);

        Notification notify = builder.build();
        notifier.notify(GPS_NOTIFY, notify);

        super.onDestroy();
    }


    //pragma mark location
    private Location firstLocation = null;
    private Location lastLocation = null;
    private long lastTime = -1;
    private long firstTime = -1;

    private LocationListener trackListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            long thisTime = System.currentTimeMillis();
            long diffTime = thisTime - lastTime;
            Log.v(DEBUG_TAG, "diffTime == "+diffTime + ", updateRate = " + updateRate);
            if (diffTime < updateRate) {
                return;
            }
            lastTime = thisTime;
            String locInfo = String.format(
                    Locale.getDefault(),
                    "Current loc = (%f, %f) @ (%.1f meters up)",
                    location.getLatitude(),
                    location.getLongitude(),
                    location.getAltitude()
            );
            if (lastLocation != null) {
                float distance = location.distanceTo(lastLocation);
                locInfo += String.format("\n Distance from last = %.1f meters", distance);
                float lastSpeed = distance / diffTime;
                locInfo += String.format("\n\tSpeed: %.2f m/s", lastSpeed);
                if (location.hasSpeed()) {
                    float gpsSpeed = location.getSpeed();
                    locInfo += String.format(" (or %.2f m/s)", gpsSpeed);
                } else {
                }
            }
            if (firstLocation != null && firstTime != -1) {
                float overallDistance = location.distanceTo(firstLocation);
                float overallSpeed = overallDistance / (thisTime - firstTime);
                locInfo += String.format("\n\tOverall speed: %.1fm/s over %.1f meters",
                        overallSpeed,
                        overallDistance);
            }
            lastLocation = location;
            if (firstLocation == null) {
                firstLocation = location;
                firstTime = thisTime;
            }
            Toast.makeText(getApplicationContext(), locInfo, Toast.LENGTH_LONG).show();
            Log.v(DEBUG_TAG, "Test time");
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.v(DEBUG_TAG, "Provider enabled " + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.v(DEBUG_TAG, "Provider disabled " + provider);
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        connectionStatus= R.string.aidl_toast_connected;
        return mRemoteInterfaceBinder;
    }


    private final IRemoteInterface.Stub mRemoteInterfaceBinder = new IRemoteInterface.Stub() {
        @Override
        public Location getLastLocation() throws RemoteException {
            Log.v(DEBUG_TAG, "getLastLocation() called");
            return lastLocation;
        }

        @Override
        public GPXPoint getGPXPoint() throws RemoteException {
            if (lastLocation == null) {
                return null;
            } else {
                Log.v(DEBUG_TAG, "getGPXPoint() called");
                GPXPoint point = new GPXPoint();
                point.elevation = lastLocation.getAltitude();
                point.latitude = (int)lastLocation.getLatitude();
                point.longitude = (int)lastLocation.getLongitude();
                point.timestamp = new Date(lastLocation.getTime());
                return point;
            }
       }
    };
}


















