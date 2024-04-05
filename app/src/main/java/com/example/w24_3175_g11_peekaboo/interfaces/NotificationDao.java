package com.example.w24_3175_g11_peekaboo.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.w24_3175_g11_peekaboo.model.Attendance;
import com.example.w24_3175_g11_peekaboo.model.Entry;
import com.example.w24_3175_g11_peekaboo.model.Notification;

import java.util.List;

@Dao
public interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long insertOneNotification(Notification notification);

    @Query("SELECT notifications.* FROM notifications " +
            "JOIN children ON notifications.notchildid = children.childid " +
            "JOIN parents ON children.childparentid = parents.parentid " +
            "WHERE notischecked = 0 AND parents.parentuserid = :userId")
    List<Notification> getNotificationDetailsByUser(String userId);

    @Query("UPDATE notifications SET notIsChecked = 1 WHERE notId = :notificationId")
    void updateNotification(long notificationId);

}
