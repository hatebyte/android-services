// IRemoteInteface.aidl
package com.hatebyte.cynicalandroid.mysimpleservice.aidl;

import com.hatebyte.cynicalandroid.mysimpleservice.aidl.GPXPoint;
// Declare any non-default types here with import statements

interface IRemoteInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    Location getLastLocation();
    GPXPoint getGPXPoint();
}
