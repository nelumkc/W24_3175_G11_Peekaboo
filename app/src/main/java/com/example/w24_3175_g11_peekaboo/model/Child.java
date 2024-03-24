package com.example.w24_3175_g11_peekaboo.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName="children",
        foreignKeys = @ForeignKey(entity = Parent.class,parentColumns = "parentid",childColumns = "childparentid",onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = {"childparentid"})}
)
public class Child {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="childid")
    private long ChildId;
    @ColumnInfo(name="childfname")
    private String ChildFname;
    @ColumnInfo(name="childlname")
    private String ChildLname;
    @ColumnInfo(name="childdob")
    private String ChildDob;
    @ColumnInfo(name="childgender")
    private String ChildGender;
    @ColumnInfo(name="childimage")
    private String ChildImage;
    @ColumnInfo(name="childparentid")
    private long ChildParentId;

    public Child() {
    }
    @Ignore
    public Child(String childFname, String childLname, String childDob, String childGender, String childImage, long childParentId) {
        ChildFname = childFname;
        ChildLname = childLname;
        ChildDob = childDob;
        ChildGender = childGender;
        ChildImage = childImage;
        ChildParentId = childParentId;
    }

    @NonNull
    public long getChildId() {
        return ChildId;
    }

    public void setChildId(@NonNull long childId) {
        ChildId = childId;
    }

    public String getChildFname() {
        return ChildFname;
    }

    public void setChildFname(String childFname) {
        ChildFname = childFname;
    }

    public String getChildLname() {
        return ChildLname;
    }

    public void setChildLname(String childLname) {
        ChildLname = childLname;
    }

    public String getChildDob() {
        return ChildDob;
    }

    public void setChildDob(String childDob) {
        ChildDob = childDob;
    }

    public String getChildGender() {
        return ChildGender;
    }

    public void setChildGender(String childGender) {
        ChildGender = childGender;
    }

    public String getChildImage() {
        return ChildImage;
    }

    public void setChildImage(String childImage) {
        ChildImage = childImage;
    }

    public long getChildParentId() {
        return ChildParentId;
    }

    public void setChildParentId(long childParentId) {
        ChildParentId = childParentId;
    }
}
