package com.winsant.android.databasemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class LoginDataBaseAdapter {

    static final String DATABASE_NAME = "login.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 6;

    static final String DATABASE_CREATE = "create table " + "LOGIN" +
            "( " + "ID integer primary key autoincrement," + "USERNAME  text," + "NAME  text," + "PASSWORD  text," + "NUMBER text," + "ADDRESS text) ";

    public SQLiteDatabase db;
    private DataBaseHelper dbHelper;

    public LoginDataBaseAdapter(Context _context) {
        dbHelper = new DataBaseHelper(_context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public LoginDataBaseAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public void insertEntry(String username, String name, String password1, String number1, String address1) {
        ContentValues newValues = new ContentValues();
        newValues.put("USERNAME", username);
        newValues.put("NAME", name);
        newValues.put("PASSWORD", password1);
        newValues.put("NUMBER", number1);
        newValues.put("ADDRESS", address1);

        db.insert("LOGIN", null, newValues);
    }

    public int deleteEntry(String password)
    {
        String where = "PASSWORD=?";
        return db.delete("LOGIN", where, new String[]{password});
    }


    public String getSinlgeEntry(String username, String password) {
        Cursor cursor = db.query("LOGIN", null, " PASSWORD=?and", new String[]{password}, null, null, null);

        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String repassword = cursor.getString(cursor.getColumnIndex("REPASSWORD"));
        cursor.close();
        return repassword;
    }

    public int getLogin(String uname, String pass) {
//		'"+id+"'"
        Cursor c = db.rawQuery("SELECT ID FROM LOGIN " +
                "WHERE USERNAME ='" + uname + "'  and PASSWORD = '" + pass + "'", null);
        if (c.moveToFirst()) {

            Log.e("ID", c.getString(c.getColumnIndex("ID")));

            c.close();

            return 1;
        } else {
            c.close();
            return 0;
        }
    }

    public void getAllData()
    {


        Cursor c = db.rawQuery("SELECT * FROM " + "LOGIN", null);
        String str = null;
        int i = 0;
        if (c.moveToFirst())
        {
            do
            {
                str = c.getString(c.getColumnIndex("USERNAME"));
                i++;
                Log.e(i + "", str);
            } while (c.moveToNext());
        }

        c.close();
    }

    public String getAllTags(String a) {


        Cursor c = db.rawQuery("SELECT * FROM " + "LOGIN" + " where SECURITYHINT = '" + a + "'", null);
        String str = null;
        if (c.moveToFirst()) {
            do {
                str = c.getString(c.getColumnIndex("PASSWORD"));
            } while (c.moveToNext());
        }

        c.close();
        return str;
    }

}
