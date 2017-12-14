package com.rent.steward;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;

import java.io.File;

public class MyService extends Service {

    private static final String TAG = MyService.class.getSimpleName();

    public MyService() {
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
//        AndroidNetworking.initialize(getApplicationContext());
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        downloadAFile(getApplicationContext());
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(TAG, "onBind");
        return null;
    }

    public void downloadAFile(Context context){
        Log.d(TAG, "Prepare to start downloading");

//        String url = "http://techslides.com/demos/sample-videos/small.mp4";
        String url = "http://distribution.bbb3d.renderfarming.net/video/mp4/bbb_sunflower_1080p_60fps_stereo_arcd.mp4";

        // errorCode : 403 responseFromServerError
//        String url = "https://doc-0c-1k-docs.googleusercontent.com/docs/securesc/1eb7kfn3b67u221f67rr2pfvpc9h7c45/p8einopv42np1o8urj0he2lsjvoume8u/1497945600000/12720479059260116357/12720479059260116357/0B8t8O5wAyqonajZZQl9MeFJDVGM?e=download&nonce=q69o0ut0uo370&user=12720479059260116357&hash=kqccd0hk05nlflhem8gi6tva8u74qi4t";

        String path = getRootDirPath(context);
        Log.d(TAG, "Path: " + path);
        String fileName = "sample2.mp4";

        AndroidNetworking.download(url, path, fileName)
                .setTag("downloadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setDownloadProgressListener((bytesDownloaded, totalBytes) -> {
//                     do anything with progress
                    Log.d(TAG, "Downloading: " + bytesDownloaded + "bytes");
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        // do anything after completion
                        Log.d(TAG, "onDownloadComplete");
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if (error.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                            // get parsed error object (If ApiError is your class)
//                            ApiError apiError = error.getErrorAsObject(ApiError.class);
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });
    }

    public String getRootDirPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.d(TAG, "media mounted");
            File file = ContextCompat.getExternalFilesDirs(context, null)[0];
            return file.getAbsolutePath();
        } else {
            Log.d(TAG, "media not mounted");
            return context.getFilesDir().getAbsolutePath();
        }
    }

}
