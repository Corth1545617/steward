package com.rent.steward.general.http;

import com.rent.steward.user.Person;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Corth1545617 on 2017/12/13.
 */

public interface ApiService {

    @GET("accounts.json")
    Call<List<Person>> getUsers();

    @GET("accounts.json")
    Call<ResponseBody> getUserInfo();

    @GET("products.json")
    Call<ResponseBody> getProducts();

    @POST("accounts.json")
    Call<ResponseBody> postUserInfo(@Body Person user);

}
