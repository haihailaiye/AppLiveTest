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
import android.widget.Toast;

import com.lihai.applivetest.IMyAidlInterface;

/**
 * 模块：
 * 功能：
 * 作者：李海
 * 时间：2017年02月23日10时48分
 */
public class LocalService extends Service {

    private MyLocalStub myLocalStub;

    public static final int SOCKET_SERVICE_ID = 203;
    private MyLocalConnection myLocalConnection;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myLocalStub;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(),"启动了",Toast.LENGTH_SHORT).show();
        myLocalStub = new MyLocalStub();
        myLocalConnection = new MyLocalConnection();
        bindService(new Intent(getApplicationContext(),RemoteService.class), myLocalConnection, Context.BIND_AUTO_CREATE);
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(SOCKET_SERVICE_ID, new Notification());//API < 18 ，此方法能有效隐藏Notification上的图标
        } else {
            Intent innerIntent = new Intent(this, InnerLocalService.class);
            startService(innerIntent);
            startForeground(SOCKET_SERVICE_ID, new Notification());
        }
        return Service.START_STICKY;
    }

    class MyLocalStub extends IMyAidlInterface.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }
    }

    class MyLocalConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            startService(new Intent(getApplicationContext(),RemoteService.class));
            bindService(new Intent(getApplicationContext(),RemoteService.class),myLocalConnection, Context.BIND_AUTO_CREATE);
        }
    }

    public static class InnerLocalService extends Service{

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(SOCKET_SERVICE_ID, new Notification());
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
