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

    @Query("SELECT * FROM children WHERE childfname LIKE '%' || :name || '%'")
    List<Child> getChildParentIdByChildName(String name);

}
