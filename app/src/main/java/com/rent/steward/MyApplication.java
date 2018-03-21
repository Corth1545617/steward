package com.rent.steward;

import android.app.Application;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.rent.steward.general.RemoteConfigHelper;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Corth1545617 on 2017/6/26.
 */

public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
//        AndroidNetworking.initialize(getApplicationContext());
//        Intent serviceIntent = new Intent(this, MyService.class);
//        startService(serviceIntent);

        // initializing Crashlytics
        Fabric.with(this, new Crashlytics());

        // Init remote config
        RemoteConfigHelper.getInstance().init();

    }
}
