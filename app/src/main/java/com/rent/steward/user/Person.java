package com.rent.steward.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Corth1545617 on 2017/6/5.
 */

public class Person {

    private long mID; // unique serial id in db
    @SerializedName("account")
    private String mAccount;
    @SerializedName("name")
    private String mName;
    @SerializedName("birth")
    private String mBirth;
    @SerializedName("sex")
    private String mSex; // m or f

    public Person() {
        mAccount = "";
        mName = "";
        mBirth = "";
        mSex = "";
    }

    public Person(String account, String name, String birth, String sex) {
        this.mAccount = account;
        this.mName = name;
        this.mBirth = birth;
        this.mSex = sex;
    }

    public Long getID() {
        return mID;
    }

    public void setID(long id) {
        this.mID = id;
    }

    public String getAccount() {
        return mAccount;
    }

    public void setAccount(String account) {
        this.mAccount = account;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getBirth() {
        return mBirth;
    }

    public void setBirth(String birth) {
        this.mBirth = birth;
    }

    public String getSex() {
        return mSex;
    }

    public void setSex(String sex) {
        this.mSex = sex;
    }
}
