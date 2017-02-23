package com.lihai.applivetest.service;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.lihai.applivetest.IMyAidlInterface;

public class RemoteService extends Service {
    private RemoteService.MyRemoteStub myLocalStub;

    public static final int REMOTE_SERVICE_ID = 204;
    private MyRemoteConnection myServiceConnection;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myLocalStub;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myLocalStub = new RemoteService.MyRemoteStub();
        myServiceConnection = new MyRemoteConnection();
        bindService(new Intent(getApplicationContext(),LocalService.class), myServiceConnection, Context.BIND_AUTO_CREATE);
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(REMOTE_SERVICE_ID, new Notification());//API < 18 ，此方法能有效隐藏Notification上的图标
        } else {
            Intent innerIntent = new Intent(this, LocalService.InnerLocalService.class);
            startService(innerIntent);
            startForeground(REMOTE_SERVICE_ID, new Notification());
        }
        return Service.START_STICKY;
    }

    class MyRemoteStub extends IMyAidlInterface.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }
    }

    class MyRemoteConnection implements ServiceConnection {

        public MyRemoteConnection(){

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            startService(new Intent(getApplicationContext(),LocalService.class));
            bindService(new Intent(getApplicationContext(),LocalService.class), myServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    public static class InnerRemoteService extends Service{

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(REMOTE_SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
