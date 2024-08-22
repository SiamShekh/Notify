package com.siam.notify.Room.Note;

import android.content.Context;
import androidx.room.Room;

public class Database {
    Context context;

    public NoteDAO Database(Context context) {
        this.context = context;

        DatabaseConnection connection = Room.databaseBuilder(context, DatabaseConnection.class, "note")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        NoteDAO dao = connection.NoteDao();

        return dao;
    }
}
