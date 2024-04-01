package com.example.w24_3175_g11_peekaboo.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="userid")
    private long userId;
    @ColumnInfo(name="username")
    private String userName;
    @ColumnInfo(name="useremail")
    private String userEmail;
    @ColumnInfo(name="userrole")
    private String userRole;

    @ColumnInfo(name="userpassword")
    private String userPassword;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    @ColumnInfo(name="usertoken")
    public String userToken;

    public User() {
    }

    @Ignore
    public User(String userName, String userEmail, String userRole, String userPassword, String userToken) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userRole = userRole;
        this.userPassword = userPassword;
        this.userToken = userToken;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
