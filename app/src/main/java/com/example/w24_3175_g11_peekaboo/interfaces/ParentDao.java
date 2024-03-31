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

    @Query("UPDATE parents SET parenttoken = :token WHERE parentid = :parentId")
    void updateNotificationToken(int parentId, String token);

    @Query("SELECT parenttoken FROM parents WHERE parentid = :parentId")
    String getNotificationToken(int parentId);
}
