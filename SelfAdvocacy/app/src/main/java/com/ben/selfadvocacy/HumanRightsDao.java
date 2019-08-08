package com.ben.selfadvocacy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Asus on 12/04/2018.
 */

public class HumanRightsDao {
    //Creating the variables to hold the column names of table
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_HEADING = "HumanRightHeading";
    public static final String COLUMN_SHORT_DESC = "HumanRightShortDesc";
    public static final String COLUMN_DESC = "HumanRightDesc";

    //Variables to store database name, table name and database version
    public static final String DATABASE_NAME = "HumanRights";
    public static final String DATABASE_TABLE = "HumanRight";
    public static final int DATABASE_VERSION = 1;

    //Variable that will hold a query for creating the table
    private static final String DATABASE_CREATE =
            "create table HumanRight(_id integer primary key autoincrement not null," +
                    "HumanRightHeading text not null unique," + " HumanRightShortDesc text not null,"+
                    " HumanRightDesc text not null);";

    private final Context context;
    private HumanRightsDao.DatabaseHelperHr DBHelper;
    private SQLiteDatabase db;

    public HumanRightsDao(Context ctx)
    {
        this.context = ctx;
        DBHelper = new HumanRightsDao.DatabaseHelperHr(context);
    }

    //Database Helper class That will create and upgrade the database
    private static class DatabaseHelperHr extends SQLiteOpenHelper
    {
        DatabaseHelperHr(Context context)
        {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF Exists '" +DATABASE_TABLE+"'");
            onCreate(db);
        }
    }//end Database Helper Class

    //open the database
    public HumanRightsDao open() throws Exception
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public HumanRightsDao clear() throws Exception{
        db = DBHelper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
        db.execSQL(DATABASE_CREATE);
        return this;
    }

    //close the database
    public void close()
    {
        DBHelper.close();
    }

    //Method to add user into the database
    public long addHumanRight(String heading,String short_desc,String description)
    {
        db = DBHelper.getWritableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_HEADING,heading);
        initialValues.put(COLUMN_SHORT_DESC,short_desc);
        initialValues.put(COLUMN_DESC,description);

        return db.insert(DATABASE_TABLE,null,initialValues);
    }//end Method


    public Cursor ListHumanRights()
    {

        return db.query(DATABASE_TABLE,new String[]
                        {
                                COLUMN_ID,
                                COLUMN_HEADING,
                                COLUMN_SHORT_DESC
                        },
                null,null,null,null,null);
    }


    public Cursor getRight(String heading) throws SQLException
    {
        db = DBHelper.getReadableDatabase();
        String rightName = null;

        Cursor mCursor = db.rawQuery("Select * from HumanRight where HumanRightHeading =?",new String[]{
               heading
        });

        if(mCursor.getCount() == 0){
            return null;
        }else if(mCursor.moveToFirst()) {
            rightName = mCursor.getString(mCursor.getColumnIndex("HumanRightHeading"));
        }

        if(rightName.equals(heading))
        {
            return mCursor;
        }
        else
        {
            return  null;
        }
    }
}
