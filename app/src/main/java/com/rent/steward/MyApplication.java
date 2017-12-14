package com.rent.steward;

import android.app.Application;
import android.util.Log;

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
    }
}
