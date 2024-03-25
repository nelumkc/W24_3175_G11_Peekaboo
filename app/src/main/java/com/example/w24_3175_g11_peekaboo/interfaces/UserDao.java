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
    @Query("SELECT * FROM users WHERE useremail= :email AND userpassword= :password LIMIT 1")
    User findUserByEmailAndPassword(String email,String password);

    @Query("SELECT COUNT(userid) FROM users WHERE userrole = :role")
    int countUserByRole(String role);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUserAdmin(User user);

    @Query("SELECT COUNT(userid) FROM users WHERE useremail = :email")
    int countUserByEmail(String email);



}
