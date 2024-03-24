package com.example.w24_3175_g11_peekaboo.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.w24_3175_g11_peekaboo.model.User;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long insertOneUser(User user);

    @Query("SELECT userId FROM users WHERE userEmail =:email")
    Long getUserIdByEmail(String email);

    @Query("SELECT userrole FROM users WHERE userEmail=:email")
    String getUserRoleByEmail(String email);


}
