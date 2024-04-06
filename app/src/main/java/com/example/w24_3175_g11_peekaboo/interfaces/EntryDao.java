package com.example.w24_3175_g11_peekaboo.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.w24_3175_g11_peekaboo.model.Entry;

import java.util.List;

@Dao
public interface EntryDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long insertOneEntry(Entry entry);

    @Query("SELECT * FROM entries WHERE entryid = :entryId")
    Entry getEntryById(Long entryId);

    @Query("SELECT entries.* FROM entries INNER JOIN children ON entries.entrychildid = children.childId WHERE children.childparentid = :parentId")
    List<Entry> getEntriesByParentId(long parentId);

    @Query("SELECT * FROM entries")
    List<Entry> getAllEntries();

    @Query("SELECT * FROM entries WHERE entrychildid IN (SELECT childId FROM children WHERE childparentid = :parentId) ORDER BY entryid DESC LIMIT 4")
    List<Entry> getLatestFourEntriesByParentId(long parentId);
}
