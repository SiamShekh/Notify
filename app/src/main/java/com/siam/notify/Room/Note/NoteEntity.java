package com.siam.notify.Room.Note;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "note")
public class NoteEntity {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "note")
    String note;

    @ColumnInfo(name = "timestamp")
    String timestamp;

    public NoteEntity(String note, String timestamp) {
        this.note = note;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
