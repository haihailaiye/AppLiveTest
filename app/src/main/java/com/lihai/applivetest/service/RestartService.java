package com.lihai.applivetest.service;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * 模块：
 * 功能：
 * 作者：李海
 * 时间：2017年02月23日10时37分
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class RestartService extends JobService {
    private  int jobId = 1000;
    private  final String TAG = RestartService.class.getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        scheduleJob(getJobInfo());
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "MyJobService onCreate "  + this.toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "MyJobService onDestroy "  + this.toString());
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e(TAG, "MyJobService onStartJob "  + this.toString());
        boolean isLocalServiceWork = isServiceWork(this, "com.lihai.applivetest.service.LocalService");
        boolean isRemoteServiceWork = isServiceWork(this, "com.lihai.applivetest.service.RemoteService");
        if(!isLocalServiceWork||
                !isRemoteServiceWork){
            this.startService(new Intent(this,LocalService.class));
            this.startService(new Intent(this,RemoteService.class));
            Toast.makeText(this, "进程启动", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e(TAG, "MyJobService onStopJob");
        scheduleJob(getJobInfo());
        return false;
    }

    //将任务作业发送到作业调度中去
    public void scheduleJob(JobInfo t) {
        Log.i("castiel", "调度job");
        JobScheduler tm =
                (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.schedule(t);
    }

   /* public static void scheduleService(Context context) {
        JobScheduler js = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(context.getPackageName(), RestartService.class.getName()));
        builder.setPersisted(true);     //设置开机启动
        builder.setPeriodic(3 * 1000);     //设置1分钟执行一次
        js.cancel(JOB_ID);
        js.schedule(builder.build());
    }*/

    public JobInfo getJobInfo(){
        JobInfo.Builder builder = new JobInfo.Builder(jobId++, new ComponentName(this, RestartService.class));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);
        builder.setRequiresCharging(false);
        builder.setRequiresDeviceIdle(false);
        //间隔100毫秒
        builder.setPeriodic(10*1000);
        return builder.build();
    }


    // 判断服务是否正在运行
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
