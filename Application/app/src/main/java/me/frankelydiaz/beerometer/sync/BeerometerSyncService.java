package me.frankelydiaz.beerometer.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by frankelydiaz on 4/27/15.
 */
public class BeerometerSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static BeerometerSyncAdapter beerometerSyncAdapter = null;

    @Override
    public void onCreate() {

        synchronized (sSyncAdapterLock) {
            if (beerometerSyncAdapter == null) {
                beerometerSyncAdapter = new BeerometerSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return beerometerSyncAdapter.getSyncAdapterBinder();
    }
}