package com.rent.steward;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.rent.steward.general.http.ApiCallback;
import com.rent.steward.general.http.ApiService;
import com.rent.steward.general.http.RetrofitFactory;
import com.rent.steward.user.Person;
import com.rent.steward.user.PersonInfoDAO;
import com.rent.steward.user.SignUpFragment;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    private final AppCompatActivity self = this;

    /*** Constant ***/

    /*** UI ***/
    private Toolbar toolbar;
    private Button signUp_btn, login_btn, showUser_btn, callApi_btn;

    /*** Http ***/
    private ApiService apiService;
    private retrofit2.Call<ResponseBody> callApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewByRId();
        apiService = RetrofitFactory.createRetrofit(ApiService.class);
    }

    private void findViewByRId(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        signUp_btn = (Button) findViewById(R.id.sign_up);
        signUp_btn.setOnClickListener(this);

        login_btn = (Button) findViewById(R.id.login);
        login_btn.setOnClickListener(this);

        showUser_btn = (Button) findViewById(R.id.show_room);
        showUser_btn.setOnClickListener(this);

        callApi_btn = (Button) findViewById(R.id.call_api);
        callApi_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction frag = getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.sign_up:
                Log.d(TAG, "sign up");
                SignUpFragment signUp = new SignUpFragment();
                frag.add(R.id.taskContainer, signUp);
                frag.addToBackStack("signup");
                frag.commit();
                break;
            case R.id.login:
                startDownload();
//                downloadAFile();
                break;
            case R.id.show_room:
                Log.d(TAG, "show room");
//                Uri friends = PersonInfoEntry.CONTENT_URI;
//                Cursor c = getContentResolver().query(friends, null, null, null, NAME);
//                String result = "Show Results:";
//                if (!c.moveToFirst()) {
//                    Toast.makeText(this, result+" no content yet!", Toast.LENGTH_LONG).show();
//                }else{
//                    do{
//                        result = result + "\n" + c.getString(c.getColumnIndex(NAME)) +
//                        " with id " +  c.getString(c.getColumnIndex(KEY_ID)) +
//                        " with account " +  c.getString(c.getColumnIndex(ACCOUNT)) +
//                        " has birthday: " + c.getString(c.getColumnIndex(BIRTHDAY));
//                    } while (c.moveToNext());
//                    Toast.makeText(this, result, Toast.LENGTH_LONG).show();
//                }
                List<Person> users = new PersonInfoDAO(this).getAll();
                for (int i = 0; i < users.size(); i++) {
                    Toast.makeText(this, users.get(i).getID() + ": " +
                            users.get(i).getAccount() + "'s name: " +
                            users.get(i).getName() + " and birthday: " +
                            users.get(i).getBirth(), Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.call_api:
                sendApiRequest();
                break;
            default:
                break;
        }
    }

    private void startDownload(){
        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);
    }

    public void downloadAFile(){
        Log.d(TAG, "Prepare to start downloading");

//        String url = "http://techslides.com/demos/sample-videos/small.mp4";
        String url = "http://distribution.bbb3d.renderfarming.net/video/mp4/bbb_sunflower_1080p_60fps_stereo_arcd.mp4";

        // errorCode : 403 responseFromServerError
//        String url = "https://doc-0c-1k-docs.googleusercontent.com/docs/securesc/1eb7kfn3b67u221f67rr2pfvpc9h7c45/p8einopv42np1o8urj0he2lsjvoume8u/1497945600000/12720479059260116357/12720479059260116357/0B8t8O5wAyqonajZZQl9MeFJDVGM?e=download&nonce=q69o0ut0uo370&user=12720479059260116357&hash=kqccd0hk05nlflhem8gi6tva8u74qi4t";

        String path = getRootDirPath(getApplicationContext());
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


    private void sendApiRequest() {
        callApi = apiService.postUserInfo();
        callApi.enqueue(new ApiCallback<ResponseBody>(callApi) {
            @Override
            public void onSuccessResponse(ResponseBody response, int statusCode) {
                Toast.makeText(self, "onSuccessResponse", Toast.LENGTH_SHORT).show();
                try {
                    Log.i(TAG, "onSuccessResponse, " + response.string());
                } catch (IOException e) {
                    Log.i(TAG, "IOException: " + e.toString());
                }
            }

            @Override
            public void onErrorResponse(ResponseBody response) {
                try {
                    Log.w(TAG, "onErrorResponse, " + response.string());
                } catch (IOException e) {
                    Log.w(TAG, "IOException: " + e.toString());
                }
            }

            @Override
            public void onApiException(Throwable t) {
                Log.w(TAG, "onApiException, " + t.toString());
            }
        });
    }


}
