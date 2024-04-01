package com.example.w24_3175_g11_peekaboo.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.w24_3175_g11_peekaboo.model.Parent;

@Dao
public interface ParentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long insertOneParent(Parent parent);

    @Query("SELECT parentId FROM parents WHERE parentUserId = :userId")
    Long getParentIdByUserId(String userId);

    @Query("SELECT parentuserid FROM parents WHERE parentid = :parentId")
    Long getUserIdByParentId(long parentId);
}
