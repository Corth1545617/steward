package com.rent.steward.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corth1545617 on 2017/6/5.
 */

// 資料功能類別
public class PersonInfoDAO implements IPersonInfoDAO {

    public static final String TAG = "PersonInfoDAO";

    // 表格名稱
    public static final String _DB_PERSONINFO_TABLE_NAME = "PersonInfoTable";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    // fields for the database
    public static final String ACCOUNT = "account";
    public static final String NAME = "name";
    public static final String BIRTHDAY = "birthday";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_PERSONINFO_TABLE =
            " CREATE TABLE " + _DB_PERSONINFO_TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ACCOUNT + " TEXT NOT NULL, " +
                    NAME + " TEXT NOT NULL, " +
                    BIRTHDAY + " TEXT NOT NULL);";

    private SQLiteDatabase mDatabase;

    public PersonInfoDAO(Context context) {
        WeakReference<Context> weakReference = new WeakReference(context);
        mDatabase = StewardDBHelper.getDatabase(weakReference.get());
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        mDatabase.close();
    }

    @Override
    public Person insert(Person person) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(ACCOUNT, person.getAccount());
        cv.put(NAME, person.getName());
        cv.put(BIRTHDAY, person.getBirth());

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = mDatabase.insert(PersonInfoDAO._DB_PERSONINFO_TABLE_NAME, null, cv);

        // 設定編號
        person.setID(id);
        // 回傳結果
        return person;

    }

    @Override
    public Person findByAccount(String account) {
        // 準備回傳結果用的物件
        Person item = null;
        // 使用編號為查詢條件
        String where = ACCOUNT + " = ? " ;//+ account;
        // 執行查詢
        Cursor result = mDatabase.query(
                _DB_PERSONINFO_TABLE_NAME, new String[]{"*"}, where,
                new String[]{String.valueOf(account)}, null, null, null);

        Log.d(TAG, "Find Person by Account: " + result.toString());

        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item = getRecord(result);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return item;
    }

    @Override
    public List<Person> getAll() {
        List<Person> result = new ArrayList<>();
        Cursor cursor = mDatabase.query(
                _DB_PERSONINFO_TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    // 把Cursor目前的資料包裝為物件
    public Person getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        Person result = new Person();

        result.setID(cursor.getLong(0));
        result.setAccount(cursor.getString(1));
        result.setName(cursor.getString(2));
        result.setBirth(cursor.getString(3));

        // 回傳結果
        return result;
    }

    @Override
    public boolean update(Person person) {
        ContentValues cv = new ContentValues();

        cv.put(ACCOUNT, person.getAccount());
        cv.put(NAME, person.getName());
        cv.put(BIRTHDAY, person.getBirth());

        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = KEY_ID + "=" + person.getID();

        // 執行修改資料並回傳修改的資料數量是否成功
        return mDatabase.update(_DB_PERSONINFO_TABLE_NAME, cv, where, null) > 0;
    }

    @Override
    public boolean delete(long id) {
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = KEY_ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return mDatabase.delete(_DB_PERSONINFO_TABLE_NAME, where , null) > 0;
    }

    // 取得資料數量
    public int getCount() {
        int result = 0;
        Cursor cursor = mDatabase.rawQuery("SELECT COUNT(*) FROM " + _DB_PERSONINFO_TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }
}
