package com.ben.selfadvocacy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * Created by Asus on 15/03/2018.
 */

public class UserDao {
    //Creating the variables to hold the column names of table
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    //Variables to store database name, table name and database version
    public static final String DATABASE_NAME = "UsersDb";
    public static final String DATABASE_TABLE = "Users";
    public static final int DATABASE_VERSION = 1;

    //Variable that will hold a query for creating the table
    private static final String DATABASE_CREATE =
            "create table users(_id integer primary key autoincrement not null," +
                    "username text not null unique," + " password text not null);";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public UserDao(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    //Database Helper class That will create and upgrade the database
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF Exists " +DATABASE_TABLE);
            onCreate(db);
        }
    }//end Database Helper Class

    //open the database
    public UserDao open() throws Exception
    {
        //db.execSQL("DROP TABLE IF EXIST" +DATABASE_TABLE);
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //close the database
    public void close()
    {
        DBHelper.close();
    }

    //Method to add user into the database
    public long addUser(String username,String password)
    {
        db = DBHelper.getWritableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_USERNAME,username);
        initialValues.put(COLUMN_PASSWORD,password);

        return db.insert(DATABASE_TABLE,null,initialValues);
    }//end Method

    /* This thing is from my assignment to display a list of all foods in the databese
    *  We can use it to display a list of all the users eventually so leave it in for now
    public Cursor DisplayUsers()
    {

        return db.query(DATABASE_TABLE,new String[]
                        {
                                COLUMN_ID,
                                COLUMN_USERNAME,
                                COLUMN_PASSWORD
                        },
                null,null,null,null,null);
    }//End Method*/

    //Method that returns a search result for a users
    public Cursor users(String username) throws SQLException
    {
        db = DBHelper.getReadableDatabase();
        String userName = null;

        Cursor mCursor = db.rawQuery("Select * from users where username =?",new String[]{
                username
        });

        if(mCursor.moveToFirst()) {
            username = mCursor.getString(mCursor.getColumnIndex("username"));
        }

        if(userName.equals(username))
        {
            return mCursor;
        }
        else
        {
            return  null;
        }

    }
}
