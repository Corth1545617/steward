package com.rent.steward.general;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.rent.steward.BuildConfig;
import com.rent.steward.R;

/**
 * Created by Corth1545617 on 2018/3/20.
 */

public class RemoteConfigHelper {

    private static final String TAG = RemoteConfigHelper.class.getSimpleName();

    private static RemoteConfigHelper sInstance = null;
    private FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

    // Remote Config caches values locally after the first successful fetch.
    // Remote Config will request fresh config values from the service.
    // If your app requests fresh values using fetch several times, requests are throttled and your app is provided with cached values.
    private long cacheExpiration = 60; // 1 hour in seconds.

    private RemoteConfigHelper() {}

    public static RemoteConfigHelper getInstance() {
        if (sInstance == null) {
            sInstance = new RemoteConfigHelper();
        }
        return sInstance;
    }

    public void init() {
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);

        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        updateRemoteConfig();
    }

    public void updateRemoteConfig() {
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "fetch onComplete");

                        if (task.isSuccessful()) {
                            Log.d(TAG, "FirebaseRemoteConifg Fetch Succeeded");

                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Log.e(TAG, "FirebaseRemoteConifg Fetch Failed");
                        }

                    }
                });
    }

}
