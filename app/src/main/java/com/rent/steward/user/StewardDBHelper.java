package com.rent.steward.user;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.rent.steward.user.PersonInfoDAO.CREATE_PERSONINFO_TABLE;
import static com.rent.steward.user.PersonInfoDAO._DB_PERSONINFO_TABLE_NAME;

/**
 * Created by Corth1545617 on 2017/5/3.
 */

public class StewardDBHelper extends SQLiteOpenHelper {

    // DB name
    public static final String _DB_NAME = "stewardData.db";

    // DB version
    // When columns revised, alter this number: normally ++
    public static final int _DB_VERSION = 1;

    // 資料庫物件，固定的欄位變數
    private static SQLiteDatabase database;


    public StewardDBHelper(Context context) {
        super(context, _DB_NAME, null, _DB_VERSION);
    }

    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new StewardDBHelper(context).getWritableDatabase();
        }

        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建立應用程式需要的表格
        // 待會再回來完成它
        addPersonInfoTable(db);
    }

    /**
     * Inserts the genre table into the database.
     * @param db The SQLiteDatabase the table is being inserted into.
     */
    private void addPersonInfoTable(SQLiteDatabase db){
        db.execSQL(CREATE_PERSONINFO_TABLE);
    }


    /**
     * Called whenever DATABASE_VERSION is incremented. This is used whenever schema changes need
     * to be made or new tables are added.
     * @param db The database being updated.
     * @param oldVersion The previous version of the database. Used to determine whether or not
     *                   certain updates should be run.
     * @param newVersion The new version of the database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+_DB_PERSONINFO_TABLE_NAME);
        onCreate(db);
    }
}
