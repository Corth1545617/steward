package com.rent.steward.user;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.rent.steward.user.PersonInfoContentProvider.BASE_CONTENT_URI;
import static com.rent.steward.user.PersonInfoContentProvider.PATH_PERSONINFO;

/**
 * Created by Corth1545617 on 2017/5/9.
 */

public class PersonInfoEntry implements BaseColumns {

    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_PERSONINFO).build();

    public static final String CONTENT_TYPE =
            "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_PERSONINFO;

    public static final String CONTENT_ITEM_TYPE =
            "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_PERSONINFO;

    public static Uri buildPersonInfoUri(long id){
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
