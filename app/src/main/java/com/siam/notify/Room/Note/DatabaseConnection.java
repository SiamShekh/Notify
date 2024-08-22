package com.siam.notify.Room.Note;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {NoteEntity.class}, version = 1)
public abstract class DatabaseConnection extends RoomDatabase {
    public abstract NoteDAO NoteDao();
}
