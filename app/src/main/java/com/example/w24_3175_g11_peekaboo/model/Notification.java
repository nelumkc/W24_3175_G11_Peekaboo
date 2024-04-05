package com.example.w24_3175_g11_peekaboo.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "notifications",
        foreignKeys = {
                @ForeignKey(entity = Child.class,parentColumns ="childid",childColumns = "notchildid",
                            onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Entry.class,parentColumns ="entryid",childColumns = "notentryid",
                            onDelete = ForeignKey.CASCADE)
        },indices = {@Index(value = {"notchildid"}),
                     @Index(value = {"notentryid"})
                     }
        )
public class Notification {
    @NonNull
    @ColumnInfo(name = "notid")
    @PrimaryKey(autoGenerate = true)
    private long notId;

    @ColumnInfo(name = "nottitle")
    private String notTitle;

    @ColumnInfo(name="notchildid")
    private long notChildId;

    @ColumnInfo(name="notentryid")
    private long notentryId;

    @ColumnInfo(name="notischecked")
    private boolean notIsChecked;

    public long getNotId() {
        return notId;
    }

    public void setNotId(long notId) {
        this.notId = notId;
    }

    public String getNotTitle() {
        return notTitle;
    }

    public void setNotTitle(String notTitle) {
        this.notTitle = notTitle;
    }

    public long getNotChildId() {
        return notChildId;
    }

    public void setNotChildId(long notChildId) {
        this.notChildId = notChildId;
    }

    public long getNotentryId() {
        return notentryId;
    }

    public void setNotentryId(long notentryId) {
        this.notentryId = notentryId;
    }

    public boolean isNotIsChecked() {
        return notIsChecked;
    }

    public void setNotIsChecked(boolean notIsChecked) {
        this.notIsChecked = notIsChecked;
    }

    public Notification() {
    }

    @Ignore
    public Notification(String notTitle, long notChildId, long notentryId, boolean notIsChecked) {
        this.notTitle = notTitle;
        this.notChildId = notChildId;
        this.notentryId = notentryId;
        this.notIsChecked = notIsChecked;
    }


}
