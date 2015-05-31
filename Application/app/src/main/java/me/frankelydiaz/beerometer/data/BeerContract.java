package me.frankelydiaz.beerometer.data;

/**
 * Created by frankelydiaz on 3/18/15.
 */
/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


public class BeerContract {

    public static final String CONTENT_AUTHORITY = "me.frankelydiaz.beerometer";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BEER = "beer";


    public static final class BeerEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BEER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BEER;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BEER;

        public static final String TABLE_NAME = "beer";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_ABV = "abv";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_IMAGE_URL = "image_url";
        public static final String COLUMN_BREWER = "brewer";
        public static final String COLUMN_COUNTRY = "country";
        public static final String COLUMN_ON_SALE = "on_sale";



        public static Uri buildBeerUri() {
            return CONTENT_URI.buildUpon().build();
        }
        public static Uri buildBeerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getBeerIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }


}

