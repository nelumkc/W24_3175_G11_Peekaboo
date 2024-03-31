package com.example.w24_3175_g11_peekaboo.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.w24_3175_g11_peekaboo.model.Entry;

@Dao
public interface EntryDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long insertOneEntry(Entry entry);

    @Query("SELECT * FROM entries WHERE entryid = :entryId")
    Entry getEntryById(Long entryId);
}
