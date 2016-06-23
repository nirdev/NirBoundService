package com.example.cjfr83.nirboundservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

public class KeepTrackService extends Service {


    private myRemoteCallbackList mCallBacks;

    private class myRemoteCallbackList extends RemoteCallbackList {

        @Override
        public void onCallbackDied(IInterface callback, Object cookie) {
            super.onCallbackDied(callback, cookie);
            Log.e("onCallbackDied ", " App name " + cookie);
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("onBind ", " Service Started! ");
        mCallBacks = new myRemoteCallbackList();

        return mStub;
    }

    private final IMainAidlInterface.Stub mStub = new IMainAidlInterface.Stub() {

        @Override
        public void registerCallBacks(IBinder cb, String packageName) throws RemoteException {

            boolean isRegistered = mCallBacks.register(new BinderWrapper(cb) , packageName);
            Log.e(packageName + " is register okay == ", " " + isRegistered);
        }
    };

    private final class BinderWrapper implements IInterface{
        IBinder mBinder;
        public BinderWrapper(IBinder mBinder) {
            this.mBinder = mBinder;
        }
        @Override
        public IBinder asBinder() {
            return mBinder;
        }
    }

}
