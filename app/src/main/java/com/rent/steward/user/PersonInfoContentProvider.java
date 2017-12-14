package com.rent.steward.user;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;

/**
 * Created by Corth1545617 on 2017/5/3.
 */

public class PersonInfoContentProvider extends ContentProvider {

    private static final String TAG = "PersonInfoContentProvider";

    /**
     * The Content Authority is a name for the entire content provider, similar to the relationship
     * between a domain name and its website. A convenient string to use for content authority is
     * the package name for the app, since it is guaranteed to be unique on the device.
     */
    private static final String AUTHORITY = "com.wistron.steward";

    /**
     * The content authority is used to create the base of all URIs which apps will use to
     * contact this content provider.
     */
    private static final String BASE_URL = "content://" + AUTHORITY ;

    /**
     * The content authority is used to create the base of all URIs which apps will use to
     * contact this content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse(BASE_URL);


    /**
     * A list of possible paths that will be appended to the base URI for each of the different
     * tables.
     */
    public static final String PATH_PERSONINFO = "personinfo";

    // integer values used in content URI
    // Use an int for each URI we will run, this represents the different queries
    private static final int PERSONINFO = 1;
    private static final int PERSONINFO_ID = 2;

    // projection map for a query
    private static HashMap<String, String> BirthMap;

    private static final UriMatcher mUriMatcher = buildUriMatcher();
    /**
     * Builds a UriMatcher that is used to determine witch database request is being made.
     */
    public static UriMatcher buildUriMatcher(){

        // All paths to the UriMatcher have a corresponding code to return
        // when a match is found (the ints above).
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, PATH_PERSONINFO, PERSONINFO);
        matcher.addURI(AUTHORITY, PATH_PERSONINFO + "/#", PERSONINFO_ID);

        return matcher;
    }

    private SQLiteDatabase mDatabase;

    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub
        mDatabase = StewardDBHelper.getDatabase(getContext());
        if (mDatabase == null)
            return false;
        else
            return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // TODO Auto-generated method stub
        Cursor out = null;
        switch(mUriMatcher.match(uri)) {
            // maps all database column names
            case PERSONINFO:
                out = mDatabase.query(
                        PersonInfoDAO._DB_PERSONINFO_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PERSONINFO_ID:
                long _id = ContentUris.parseId(uri);
                out = mDatabase.query(
                        PersonInfoDAO._DB_PERSONINFO_TABLE_NAME,
                        projection,
                        PersonInfoEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );

                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        // Set the notification URI for the cursor to the one passed into the function. This
        // causes the cursor to register a content observer to watch for changes that happen to
        // this URI and any of it's descendants. By descendants, we mean any URI that begins
        // with this path.
        out.setNotificationUri(getContext().getContentResolver(), uri);
        return out;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // TODO Auto-generated method stub
        switch(mUriMatcher.match(uri)) {
            case PERSONINFO:
                return PersonInfoEntry.CONTENT_TYPE;
            case PERSONINFO_ID:
                return PersonInfoEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long _id;
        Uri returnUri;

        switch(mUriMatcher.match(uri)){
            case PERSONINFO:
                _id = mDatabase.insert(PersonInfoDAO._DB_PERSONINFO_TABLE_NAME, null, values);
                if(_id > 0){
                    returnUri =  PersonInfoEntry.buildPersonInfoUri(_id);
                } else{
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Use this on the URI passed into the function to notify any observers that the uri has
        // changed.
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int rows; // Number of rows effected

        switch(mUriMatcher.match(uri)){
            case PERSONINFO:
                rows = mDatabase.delete(PersonInfoDAO._DB_PERSONINFO_TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Because null could delete all rows:
        if(selection == null || rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rows;
        switch(mUriMatcher.match(uri)){
            case PERSONINFO:
                rows = mDatabase.update(PersonInfoDAO._DB_PERSONINFO_TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;

    }
}
