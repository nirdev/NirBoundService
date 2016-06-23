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
    private ArrayMap<String, DeathCallBack> mCallbacks = new ArrayMap<>();

    private final class DeathCallBack implements IBinder.DeathRecipient {
        private String pn;
        private IBinder mBinder;

        DeathCallBack(String packageName,IBinder binder) {
            pn = packageName;
            mBinder = binder;
        }

        public void binderDied() {
            synchronized (mCallbacks) {
                mBinder.unlinkToDeath(this,0);
                clientDeath(pn);
            }
        }
    }

    //To be called only from thread-safe functions - optional to put code inside "binder died"
    private void clientDeath(String packageName) {
        Log.e(TAG, "deathPackageName: " + packageName);
        mCallbacks.remove(packageName);
    }

    //Optional to throw exception and not do boolean return
    public boolean register(IBinder token, String packageName) {
        synchronized (mCallbacks) {
            try {
                if (!mCallbacks.containsKey(packageName)) {
                    DeathCallBack mDeathCallBack = new DeathCallBack(packageName,token);
                    mCallbacks.put(packageName, mDeathCallBack);
                    token.linkToDeath(mDeathCallBack, 0);
                }
                return true;
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

}
