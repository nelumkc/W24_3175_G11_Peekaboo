package com.example.w24_3175_g11_peekaboo.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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

}
