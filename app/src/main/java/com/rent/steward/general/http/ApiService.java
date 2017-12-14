package com.rent.steward.general.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Corth1545617 on 2017/12/13.
 */

public interface ApiService {

    @POST("anything")
    Call<ResponseBody> postUserInfo();

}
