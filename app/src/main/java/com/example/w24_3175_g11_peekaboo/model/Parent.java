package com.example.w24_3175_g11_peekaboo.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "parents",
        foreignKeys = @ForeignKey(entity = User.class,parentColumns = "userid",childColumns = "parentuserid",onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = {"parentuserid"})}
)
public class Parent {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="parentid")
    private long parentId;

    @ColumnInfo(name="parentuserid")
    private long parentUserId;


    public String getParentToken() {
        return parentToken;
    }

    public void setParentToken(String parentToken) {
        this.parentToken = parentToken;
    }

    @ColumnInfo(name="parenttoken")
    public String parentToken;

    public Parent() {
    }

    @Ignore
    public Parent(long parentUserId) {
        this.parentUserId = parentUserId;
    }

    @NonNull
    public long getParentId() {
        return parentId;
    }

    public void setParentId(@NonNull long parentId) {
        this.parentId = parentId;
    }

    public long getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(long parentUserId) {
        this.parentUserId = parentUserId;
    }
}
