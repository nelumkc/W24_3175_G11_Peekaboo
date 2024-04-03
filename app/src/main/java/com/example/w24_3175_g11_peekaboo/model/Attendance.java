package com.example.w24_3175_g11_peekaboo.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "attendance",
        foreignKeys = @ForeignKey(entity = Child.class,parentColumns = "childid",childColumns = "attchildid",onDelete = ForeignKey.CASCADE)
)
public class Attendance {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="attid")
    private long attId;

    @ColumnInfo(name ="attchildid")
    public long attchildId;

    @ColumnInfo(name ="attdate")
    public String attDate;

    @ColumnInfo(name = "ispresent")
    public boolean isPresent;

    @ColumnInfo(name = "checkintime")
    public String checkInTime;

    @ColumnInfo(name = "checkouttime")
    public String checkOutTime;

    public Attendance() {
    }

    public long getAttId() {
        return attId;
    }

    public void setAttId(long attId) {
        this.attId = attId;
    }

    public long getAttchildId() {
        return attchildId;
    }

    public void setAttchildId(long attchildId) {
        this.attchildId = attchildId;
    }

    public String getAttDate() {
        return attDate;
    }

    public void setAttDate(String attDate) {
        this.attDate = attDate;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }
}
