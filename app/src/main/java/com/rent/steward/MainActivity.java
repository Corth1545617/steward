package com.rent.steward;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.rent.steward.general.http.ApiCallback;
import com.rent.steward.general.http.ApiService;
import com.rent.steward.general.http.RetrofitFactory;
import com.rent.steward.user.Person;
import com.rent.steward.user.PersonInfoDAO;
import com.rent.steward.user.SignUpFragment;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;

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

    private void sendApiRequest() {
        callApi = apiService.postUserInfo();
        callApi.enqueue(new ApiCallback<ResponseBody>(callApi) {
            @Override
            public void onSuccessResponse(ResponseBody response, int statusCode) {
                Toast.makeText(self, "onSuccessResponse", Toast.LENGTH_SHORT).show();
                try {
                    Log.i(TAG, "onSuccessResponse, " + response.string());
                } catch (IOException e) {
                    Log.i(TAG, "IOException", e);
                    Crashlytics.logException(e);
                }
            }

            @Override
            public void onErrorResponse(ResponseBody response) {
                try {
                    Log.w(TAG, "onErrorResponse, " + response.string());
                } catch (IOException e) {
                    Log.w(TAG, "IOException", e);
                    Crashlytics.logException(e);
                }
            }

            @Override
            public void onApiException(Throwable t) {
                Log.w(TAG, "onApiException, " + t.toString());
            }
        });
    }


}
