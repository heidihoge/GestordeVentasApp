package com.inge2.gestorventas.gestordeventas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ADMIN on 15/10/2016.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ventas.sqlite";
    private static final int DB_SCHEME_VERSION = 1;



    public DbHelper(Context context) {
        super(context, DB_NAME, null , DB_SCHEME_VERSION);
    }

    //le pasamos la sentencia sql para que se ejecute
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseManager.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
