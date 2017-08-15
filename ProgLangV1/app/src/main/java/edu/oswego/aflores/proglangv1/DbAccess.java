package edu.oswego.aflores.proglangv1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by aflores on 2/12/2017.
 */

public class DbAccess {
    private SQLiteDatabase database;
    private SqlHelper dbHelper;

    public DbAccess(Context context) {
        dbHelper = new SqlHelper(context);
        loadDb(context);
    }

    private void loadDb(Context ctx) {
        clearDb();
        loadTopics(ctx);
        loadLanguages(ctx);
        loadCode(ctx);
    }

    private void clearDb() {
        try {
            open();
            database.delete(SqlHelper.TBL_CODE, null, null);
            database.delete(SqlHelper.TBL_LANGUAGES, null, null);
            database.delete(SqlHelper.TBL_TOPICS, null, null);
            close();
        }
        catch (Exception e) {
            Log.d("Db", e.toString());
        }
    }

    private void loadTopics(Context ctx) {
        try {
            open();
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(ctx.getResources().openRawResource(R.raw.topics)));
            String line;
            while ( (line = in.readLine()) != null ) {
                Log.d("Db_topics", "creating topic " + line);
                ContentValues values = new ContentValues();
                values.put(SqlHelper.TOP_NAM, line);
                database.insert(SqlHelper.TBL_TOPICS, null, values);
            }
            in.close();
            close();
        }
        catch (Exception e) {
            Log.d("Db_topics", e.toString());
        }
    }

    private void loadLanguages(Context ctx) {
        try {
            open();
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(ctx.getResources().openRawResource(R.raw.languages)));
            String line;
            while ( (line = in.readLine()) != null ) {
                Log.d("Db_languages", "creating language " + line);
                ContentValues values = new ContentValues();
                values.put(SqlHelper.LAN_NAM, line);
                database.insert(SqlHelper.TBL_LANGUAGES, null, values);
            }
            in.close();
            close();
        }
        catch (Exception e) {
            Log.d("Db_languages", e.toString());
        }
    }

    private void loadCode(Context ctx) {
        try {
            open();
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(ctx.getResources().openRawResource(R.raw.code)));
            String line;
            while ( (line = in.readLine()) != null ) {
                Log.d("Db_code", "creating code " + line);
                String[] fields = line.split("_");
                createCode(fields[0], fields[1], fields[2], fields[3]);
            }
            in.close();
            close();
        }
        catch (Exception e) {
            Log.d("Db_code", e.toString());
        }
    }

    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    private void close() {
        dbHelper.close();
    }

    private void createCode(String language, String topic, String syntax, String example) {
        ContentValues values = new ContentValues();
        Code c = new Code(topic, language, syntax, example);
        values.put(SqlHelper.TOP_ID, getTopicID(c.getTopic()));
        values.put(SqlHelper.LAN_ID, getLangID(c.getLang()));
        values.put(SqlHelper.COD_SYN, c.getSyntax());
        values.put(SqlHelper.COD_EXA, c.getExample());
        database.insert(SqlHelper.TBL_CODE, null, values);
    }

    public String[] getTopics() {
        open();
        String[] column = { SqlHelper.TOP_NAM };
        Cursor cursor = database.query(SqlHelper.TBL_TOPICS, column,
                null, null, null, null, SqlHelper.TOP_ID + " ASC");
        cursor.moveToFirst();
        int length = cursor.getCount(); // Number of rows
        String[] allTopics = new String[length];
        for (int i = 0; i < length; i++) {
            String topic_name = cursor.getString(0);
            Log.d("Topics", "Retrieved topic " + topic_name);
            allTopics[i] = topic_name;
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return allTopics;
    }

    public ArrayList<Code> getAllCode(String topicName) {
        open();
        ArrayList<Code> codes = new ArrayList<>();
        Cursor cursor = database.rawQuery("select c." + SqlHelper.TOP_ID + ", c." +
                SqlHelper.LAN_ID + ", " + SqlHelper.COD_SYN + ", " + SqlHelper.COD_EXA +
                " from " + SqlHelper.TBL_CODE + " AS c, " + SqlHelper.TBL_TOPICS +
                " AS t where t." + SqlHelper.TOP_ID + " = c." + SqlHelper.TOP_ID + " AND t."
                + SqlHelper.TOP_NAM + " = ?", new String[]{ topicName });
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Code c = cursorToCode(cursor);
            Log.d("Code", "Creating code " + c);
            codes.add(c);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return codes;
    }

    private Code cursorToCode(Cursor cursor) {
        int top_id = cursor.getInt(cursor.getColumnIndex(SqlHelper.TOP_ID));
        int lan_id = cursor.getInt(cursor.getColumnIndex(SqlHelper.LAN_ID));
        String syntax = cursor.getString(cursor.getColumnIndex(SqlHelper.COD_SYN));
        String example = cursor.getString(cursor.getColumnIndex(SqlHelper.COD_EXA));
        Code c = new Code(getTopic(top_id), getLang(lan_id), syntax, example);
        return c;
    }

    private int getTopicID(String topic_name) {
        Cursor cursor = database.rawQuery("select " + SqlHelper.TOP_ID + " from " +
                        SqlHelper.TBL_TOPICS + " where " + SqlHelper.TOP_NAM + " = ?",
                new String[] { topic_name });
        cursor.moveToFirst();
        int topic_id = cursor.getInt(0);
        cursor.close();
        return topic_id;
    }

    private int getLangID(String lang_name) {
        Cursor cursor = database.rawQuery("select " + SqlHelper.LAN_ID + " from " +
                        SqlHelper.TBL_LANGUAGES + " where " + SqlHelper.LAN_NAM + " = ?",
                new String[] { lang_name });
        cursor.moveToFirst();
        int lang_id = cursor.getInt(0);
        cursor.close();
        return lang_id;
    }

    private String getTopic(int top_id) {
        String topic_id = "" + top_id;
        Cursor cursor = database.rawQuery("select " + SqlHelper.TOP_NAM + " from " +
                        SqlHelper.TBL_TOPICS + " where " + SqlHelper.TOP_ID + " = ?",
                new String[] { topic_id });
        cursor.moveToFirst();
        String topic_name = cursor.getString(0);
        cursor.close();
        return topic_name;
    }

    private String getLang(int lan_id) {
        String lang_id = "" + lan_id;
        Cursor cursor = database.rawQuery("select " + SqlHelper.LAN_NAM + " from " +
                        SqlHelper.TBL_LANGUAGES + " where " + SqlHelper.LAN_ID + " = ?",
                new String[] { lang_id });
        cursor.moveToFirst();
        String lang_name = cursor.getString(0);
        cursor.close();
        return lang_name;
    }
}
