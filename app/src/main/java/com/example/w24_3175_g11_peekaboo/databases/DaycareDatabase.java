package com.example.w24_3175_g11_peekaboo.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.w24_3175_g11_peekaboo.interfaces.ChildDao;
import com.example.w24_3175_g11_peekaboo.interfaces.ParentDao;
import com.example.w24_3175_g11_peekaboo.interfaces.UserDao;
import com.example.w24_3175_g11_peekaboo.model.Child;
import com.example.w24_3175_g11_peekaboo.model.Parent;
import com.example.w24_3175_g11_peekaboo.model.User;

@Database(entities = {User.class, Parent.class, Child.class}, version = 1, exportSchema = false)
public abstract class DaycareDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract ParentDao parentDao();

    public abstract ChildDao childDao();

}
