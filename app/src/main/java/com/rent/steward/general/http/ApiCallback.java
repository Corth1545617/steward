package com.rent.steward.general.http;

import android.util.Log;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Corth1545617 on 2017/12/13.
 */

public abstract class ApiCallback<T> implements Callback<T> {

    private final static String TAG = ApiCallback.class.getSimpleName();

    private Call<T> call;

    private final static int TOTAL_RETRIES = 1;
    private int retryCount = 0;

    public ApiCallback(Call<T> call) {
        this.call = call;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (null != response.body()) {
            onSuccessResponse(response.body(), response.code());
        } else if (null != response.errorBody()) {
            onErrorResponse(response.errorBody());
        }
    }

    @Override
    public void onFailure(final Call<T> call, Throwable t) {
        Log.e(TAG, t.toString() + "\n" + t.getCause());
        if (t instanceof SocketTimeoutException) {
            Log.d(TAG, t.toString());
            if (retryCount < TOTAL_RETRIES) {
                retryCount++;
                Log.d(TAG, "Retrying ... (" + retryCount + " out of " + TOTAL_RETRIES + ")");
                retry();
            } else {
                onApiException(t);
            }

        } else {
            if (t instanceof ConnectException) {
                Log.d(TAG, "Please check the internet connection.");
            } else if (t instanceof UnknownHostException) {
                Log.d(TAG, t.toString());
            }
            onApiException(t);
        }
    }

    public abstract void onSuccessResponse(T response, int statusCode);

    public abstract void onErrorResponse(ResponseBody response);

    public abstract void onApiException(Throwable t);

    private void retry() {
        call.clone().enqueue(this);
    }

}
