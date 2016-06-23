package com.example.cjfr83.nirboundservice;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;

/**
 * Created by Nir Duan on 6/23/2016.
 */
public class ClientsDeathWatcher {

    private static final String TAG = "ClientsDeathWatcher";

    private ArrayMap<DeathCallBack, String> mCallbacks = new ArrayMap<>();

    private final class DeathCallBack implements IBinder.DeathRecipient {

        IBinder mService;

        DeathCallBack(IBinder service) {
            mService = service;
        }

        public void binderDied() {
            synchronized (mCallbacks) {
                clientDeath(this);
            }
        }
    }

    //To be called only from thread-safe functions - optional to put code inside "binder died"
    private void clientDeath(DeathCallBack mService) {
        String deathPackageName = mCallbacks.get(mService);
        Log.e(TAG, "deathPackageName: " + deathPackageName);
        mCallbacks.remove(mService);
    }

    //Optional to throw exception and not do boolean return
    public boolean register(IBinder token, String packageName) {
        synchronized (mCallbacks) {
            try {
                DeathCallBack mDeathCallBack = new DeathCallBack(token);
                token.linkToDeath(mDeathCallBack, 0);
                mCallbacks.put(mDeathCallBack, packageName);
                return true;
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
