package com.example.w24_3175_g11_peekaboo.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.w24_3175_g11_peekaboo.model.Attendance;
import com.example.w24_3175_g11_peekaboo.model.Child;

import java.util.List;

@Dao
public interface ChildDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertOneChild(Child child);

    @Query("SELECT * FROM children")
    List<Child> getAllChildren();

    @Query("SELECT * FROM children WHERE childparentid = :parentid")
    List<Child> getChildrenByParentId(long parentid);

    @Query("SELECT * FROM children WHERE childfname LIKE '%' || :name || '%' OR childlname LIKE '%' || :name || '%'")
    List<Child> searchChildrenByName(String name);

    @Query("SELECT * FROM children WHERE childfname = :fname")
    long getChildIdByName(String fname);

    @Query("SELECT childparentid FROM children WHERE childfname = :fname")
    long getParentIdByChildfname(String fname);

    @Query("SELECT u.usertoken FROM users u INNER JOIN parents p ON u.userId = p.parentuserid INNER JOIN children c ON p.parentId = c.childparentid WHERE c.childfname = :fname")
    String getParentUserTokenByChildfname(String fname);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertAttendance(Attendance attendance);

    @Update
    void updateAttendance(Attendance attendance);

    @Query("SELECT * FROM attendance WHERE attchildid = :childId AND attdate = :currentDate LIMIT 1")
    Attendance getAttendanceForChild(long childId, String currentDate);


    @Query("SELECT COUNT(*) FROM Attendance WHERE attdate = :currentDate AND ispresent = 1")
    int countPresentChildren(String currentDate);

    @Query("SELECT COUNT(*) FROM Attendance WHERE attdate = :currentDate AND ispresent = 0")
    int countAbsentChildren(String currentDate);
}
