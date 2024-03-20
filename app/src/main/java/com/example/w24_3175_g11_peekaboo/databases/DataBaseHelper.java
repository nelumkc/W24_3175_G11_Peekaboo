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
    private static final String DATABASE_NAME = "schoolDatabase1.db";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    private static final String TABLE_USER = "user";
    private static final String TABLE_CHILD = "child";
    private static final String TABLE_PARENT = "parent";

    // User Table Columns
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_ROLE = "role";
    private static final String COLUMN_USER_PASSWORD = "password";

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
    private static final String COLUMN_PARENT_USER_ID = "userId";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER +
                "(" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USER_NAME + " TEXT," +
                COLUMN_USER_EMAIL + " TEXT UNIQUE," +
                COLUMN_USER_ROLE + " TEXT," +
                COLUMN_USER_PASSWORD + " TEXT" +
                ")";

        String CREATE_PARENT_TABLE = "CREATE TABLE " + TABLE_PARENT +
                "(" +
                COLUMN_PARENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_PARENT_USER_ID + " INTEGER," +
                "FOREIGN KEY(" + COLUMN_PARENT_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + ")" +
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

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_PARENT_TABLE);
        db.execSQL(CREATE_CHILD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHILD);
    }

    //Insert users
    public long insertUser(String name, String email, String role, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_ROLE, role);
        values.put(COLUMN_USER_PASSWORD, password);

        long userId = db.insert(TABLE_USER, null, values);
        if (userId == -1) {
            Log.e("DataBaseHelper", "Failed to insert new user");
        }
        db.close();
        return userId;
    }

    @SuppressLint("Range")
    public long insertOrGetParentId(String name, String email) {
        // Insert into User table first
        long userId = insertUser(name, email, "PARENT", "123");

        if (userId == -1) {
            // If user insertion failed, possibly due to email uniqueness constraint, attempt to fetch existing user ID
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_USER, new String[]{COLUMN_USER_ID},
                    COLUMN_USER_EMAIL + " = ?", new String[]{email}, null, null, null);

            if (cursor.moveToFirst()) {
                userId = cursor.getLong(cursor.getColumnIndex(COLUMN_USER_ID));
            }
            cursor.close();
            db.close();
        }

        // If we have a valid userId, ensure there's a corresponding entry in the Parent table
        if (userId != -1) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_PARENT_USER_ID, userId);
            // Check if parent already exists for this userId to avoid duplicate entries
            Cursor cursor = db.query(TABLE_PARENT, new String[]{COLUMN_PARENT_ID},
                    COLUMN_PARENT_USER_ID + " = ?", new String[]{String.valueOf(userId)}, null, null, null);

            // If not, insert new parent record
            if (!cursor.moveToFirst()) {
                long parentId = db.insert(TABLE_PARENT, null, values);
                cursor.close();
                db.close();
                return parentId;
            } else {
                long parentId = cursor.getLong(cursor.getColumnIndex(COLUMN_PARENT_ID));
                cursor.close();
                db.close();
                return parentId;
            }
        } else {
            // User insertion failed and existing user was not found
            return -1;
        }
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

    public void createAdminAccount() {
        SQLiteDatabase db = this.getWritableDatabase();

        if (!isTableExists(db, TABLE_USER)) {
            onCreate(db); 
        }
        // Check if an admin account already exists
        Cursor cursor = db.query(TABLE_USER, new String[]{"id"}, "role = ?", new String[]{"ADMIN"}, null, null, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return;
        }
        cursor.close();

        // Insert admin account as it does not exist
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, "admin");
        values.put(COLUMN_USER_EMAIL, "nelumkc@gmail.com");
        values.put(COLUMN_USER_PASSWORD, "admin");
        values.put(COLUMN_USER_ROLE, "ADMIN");
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    private boolean isTableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public boolean doesEmailExist(String email) {
        // Assuming you have a SQLiteDatabase instance named db
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[] { "id" }, "email = ?", new String[] { email }, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean authenticateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_USER +" WHERE email=? AND password=?", new String[]{email, password});
        boolean isAuthenticated = cursor.getCount() > 0;
        cursor.close();
        return isAuthenticated;
    }

    public String getUserRoleByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String userRole = null;
        Cursor cursor = db.query(TABLE_USER, new String[]{COLUMN_USER_ROLE}, COLUMN_USER_EMAIL + "=?", new String[]{email}, null, null, null);

        int roleColumnIndex = cursor.getColumnIndex(COLUMN_USER_ROLE);
        if (roleColumnIndex != -1 && cursor.moveToFirst()) {
            userRole = cursor.getString(roleColumnIndex);
        }
        cursor.close();
        db.close();
        return userRole;
    }



}
