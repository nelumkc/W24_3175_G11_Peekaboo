package com.example.w24_3175_g11_peekaboo.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "entries",
        foreignKeys = @ForeignKey(entity = Child.class,parentColumns ="childid",childColumns = "entrychildid",
                onDelete = ForeignKey.CASCADE))
public class Entry implements Serializable {


    public long getEntryId() {
        return entryId;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }

    @NonNull
    @ColumnInfo(name = "entryid")
    @PrimaryKey(autoGenerate = true)
    private long entryId;

    @ColumnInfo(name = "entrydesc")
    private String entryDesc;

    @ColumnInfo(name = "entrytitle")
    private String entryTitle;

    @ColumnInfo(name="entryimage")
    private String entryImage;

    @ColumnInfo(name="entrychildid")
    private long entryChildId;

    public long getActivityId() {
        return entryId;
    }

    public void setActivityId(long activityId) {
        this.entryId = entryId;
    }

    public String getDateOfEntry() {
        return entryDate;
    }

    public void setDateOfEntry(String dateOfEntry) {
        this.entryDate = dateOfEntry;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    @ColumnInfo(name="entrydate")
    private String entryDate;

    public Entry() {
    }

    public Entry(String activityDesc, String activityTitle, String activityImage, long activityChildId, String dateOfEntry) {
        this.entryDesc = activityDesc;
        this.entryTitle = activityTitle;
        this.entryImage = activityImage;
        this.entryChildId = activityChildId;
        this.entryDate = dateOfEntry;
    }

    public String getEntryDesc() {
        return entryDesc;
    }

    public void setEntryDesc(String entryDesc) {
        this.entryDesc = entryDesc;
    }

    public String getEntryTitle() {
        return entryTitle;
    }

    public void setEntryTitle(String entryTitle) {
        this.entryTitle = entryTitle;
    }

    public String getEntryImage() {
        return entryImage;
    }

    public void setEntryImage(String entryImage) {
        this.entryImage = entryImage;
    }

    public long getEntryChildId() {
        return entryChildId;
    }

    public void setEntryChildId(long entryChildId) {
        this.entryChildId = entryChildId;
    }
}
