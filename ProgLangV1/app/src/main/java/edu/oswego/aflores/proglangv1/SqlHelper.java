package edu.oswego.aflores.proglangv1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by aflores on 2/9/2017.
 */

public class SqlHelper extends SQLiteOpenHelper {
    public static final String TBL_TOPICS = "Topics";
    public static final String TBL_LANGUAGES = "Languages";
    public static final String TBL_CODE = "Code";
    public static final String TOP_ID = "Topic_ID";
    public static final String TOP_NAM = "Name";
    public static final String LAN_ID = "Lang_ID";
    public static final String LAN_NAM = "Name";
    public static final String COD_SYN = "Syntax";
    public static final String COD_EXA = "Example";
    private static final String NAME = "prog_lang.db";
    private static final int VERSION = 1;
    private static final String CREATE_TOPICS = "create table " +
            TBL_TOPICS + "(" + TOP_ID + " integer primary key autoincrement, " +
            TOP_NAM + " text not null);";
    private static final String CREATE_LANG = "create table " +
            TBL_LANGUAGES + "(" + LAN_ID + " integer primary key autoincrement, " +
            LAN_NAM + " text not null);";
    private static final String CREATE_CODE = "create table " +
            TBL_CODE + "(" + TOP_ID + " integer not null, " +
            LAN_ID + " integer not null, " +
            COD_SYN + " text not null, " +
            COD_EXA + " text not null, " +
            "foreign key (" + TOP_ID + ") references " + TBL_TOPICS + "(" + TOP_ID + "), " +
            "foreign key (" + LAN_ID + ") references " + TBL_LANGUAGES + "(" + LAN_ID + "), " +
            "primary key (" + TOP_ID + ", " + LAN_ID + "));";

    public SqlHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TOPICS);
        db.execSQL(CREATE_LANG);
        db.execSQL(CREATE_CODE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SqlHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to " +
                        newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TBL_CODE);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_LANGUAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_TOPICS);
        onCreate(db);
    }
}
