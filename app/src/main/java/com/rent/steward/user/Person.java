package com.rent.steward.user;

/**
 * Created by Corth1545617 on 2017/6/5.
 */

public class Person {

    private long mID; // unique serial id in db
    private String mAccount;
    private String mName;
    private String mBirth;

    public Person() {
        mName = "";
        mBirth = "";
    }

    public Person(String account, String name, String birth) {
        this.mAccount = account;
        this.mName = name;
        this.mBirth = birth;
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
}
