package com.siam.notify.Room.Note;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface NoteDAO {
    @Insert
    void InsertNote(NoteEntity note);

    @Query("SELECT * FROM note WHERE id=:id")
    NoteEntity getNoteById(int id);

    @Query("SELECT * FROM note")
    List<NoteEntity> getAllNote();

    @Query("DELETE FROM note WHERE id = :id")
    void deleteSingleNote(String id);

    @Query("DELETE FROM note")
    void deleteAllNotes();

    @Query("SELECT * FROM note WHERE title LIKE '%' || :title || '%'")
    List<NoteEntity> getSearchList(String title);



    @Query("UPDATE note SET note = :note AND timestamp=:time WHERE id = :id")
    void updateNote(int id, String note, String time);


}
