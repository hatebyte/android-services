package com.hatebyte.cynicalandroid.mysimpleservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hatebyte.cynicalandroid.mysimpleservice.aidl.AIDLActivity;
import com.hatebyte.cynicalandroid.mysimpleservice.binder.BinderExtensionActivity;
import com.hatebyte.cynicalandroid.mysimpleservice.messenger.MessengerActivity;

public class MainActivity extends Activity {
    private static final String TAG = "com.hatebyte.cynicalandroid.mainactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "MainActivity : onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToBinderAcitivity(View v) {
        Intent binderIntent = new Intent(this, BinderExtensionActivity.class);
        startActivity(binderIntent);
    }

    public void goToAIDLAcitivity(View v) {
        Intent binderIntent = new Intent(this, AIDLActivity.class);
        startActivity(binderIntent);
    }

    public void goToMessengerAcitivity(View v) {
        Intent binderIntent = new Intent(this, MessengerActivity.class);
        startActivity(binderIntent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
