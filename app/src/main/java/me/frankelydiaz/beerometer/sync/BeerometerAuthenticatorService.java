package me.frankelydiaz.beerometer.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by frankelydiaz on 4/27/15.
 */
public class BeerometerAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private BeerometerAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new BeerometerAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
