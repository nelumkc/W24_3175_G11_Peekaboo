package com.example.w24_3175_g11_peekaboo.databases;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "schoolDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_CHILD = "child";
    private static final String TABLE_PARENT = "parent";

    // Child Table Columns
    private static final String COLUMN_CHILD_ID = "id";
    private static final String COLUMN_CHILD_FIRST_NAME = "firstName";
    private static final String COLUMN_CHILD_LAST_NAME = "lastName";
    private static final String COLUMN_CHILD_DOB = "dob";
    private static final String COLUMN_CHILD_GENDER = "gender";
    private static final String COLUMN_CHILD_PROFILE_PIC_URI = "profilePicUri";
    private static final String COLUMN_CHILD_PARENT_ID = "parentId";

    // Parent Table Columns
    private static final String COLUMN_PARENT_ID = "id";
    private static final String COLUMN_PARENT_NAME = "name";
    private static final String COLUMN_PARENT_EMAIL = "email";
    private static final String COLUMN_PARENT_ROLE = "role";
    private static final String COLUMN_PARENT_PASSWORD = "password";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PARENT_TABLE = "CREATE TABLE " + TABLE_PARENT +
                "(" +
                COLUMN_PARENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_PARENT_NAME + " TEXT," +
                COLUMN_PARENT_EMAIL + " TEXT UNIQUE," +
                COLUMN_PARENT_ROLE + " TEXT," +
                COLUMN_PARENT_PASSWORD + " TEXT " +
                ")";

        String CREATE_CHILD_TABLE = "CREATE TABLE " + TABLE_CHILD +
                "(" +
                COLUMN_CHILD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CHILD_FIRST_NAME + " TEXT," +
                COLUMN_CHILD_LAST_NAME + " TEXT," +
                COLUMN_CHILD_DOB + " TEXT," +
                COLUMN_CHILD_GENDER + " TEXT," +
                COLUMN_CHILD_PROFILE_PIC_URI + " TEXT," +
                COLUMN_CHILD_PARENT_ID + " INTEGER," +
                "FOREIGN KEY(" + COLUMN_CHILD_PARENT_ID + ") REFERENCES " + TABLE_PARENT + "(" + COLUMN_PARENT_ID + ")" +
                ")";

        db.execSQL(CREATE_PARENT_TABLE);
        db.execSQL(CREATE_CHILD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists TABLE_PARENT");
        db.execSQL("drop Table if exists TABLE_CHILD");
    }

    @SuppressLint("Range")
    public long insertOrGetParentId(String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if parent already exists
        Cursor cursor = db.query(TABLE_PARENT, new String[]{COLUMN_PARENT_ID},
                COLUMN_PARENT_EMAIL + " = ?", new String[]{email}, null, null, null);

        long parentId = -1;
        if (cursor.moveToFirst()) {
            parentId = cursor.getLong(cursor.getColumnIndex(COLUMN_PARENT_ID));
        } else {
            ContentValues values = new ContentValues();// No parent found, insert a new
            values.put(COLUMN_PARENT_NAME, name);
            values.put(COLUMN_PARENT_EMAIL, email);
            values.put(COLUMN_PARENT_ROLE, "PARENT");
            values.put(COLUMN_PARENT_PASSWORD, "123");

            // Insert new parent and get the new ID
            parentId = db.insert(TABLE_PARENT, null, values);
            if (parentId == -1) {
                Log.e("DatabaseHelper", "Failed to insert or get parent ID");
            }else{
                Log.e("DatabaseHelper", "parent data inserted sucessfully");
            }

        }
        cursor.close();
        db.close();

        return parentId;
    }


    // Add a child with parent email
    public Boolean addChildWithParent(String firstName, String lastName, String dob, String gender, String profilePicUri, String parentName, String parentEmail) {
        // Insert parent or get existing ID
        long parentId = insertOrGetParentId(parentName, parentEmail);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHILD_FIRST_NAME, firstName);
        values.put(COLUMN_CHILD_LAST_NAME, lastName);
        values.put(COLUMN_CHILD_DOB, dob);
        values.put(COLUMN_CHILD_GENDER, gender);
        values.put(COLUMN_CHILD_PROFILE_PIC_URI, profilePicUri);
        values.put(COLUMN_CHILD_PARENT_ID, parentId); // Link child to parent by ID

        long result = db.insert(TABLE_CHILD, null, values);
        db.close();
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +  TABLE_CHILD,null);
        return cursor;
    }



}
