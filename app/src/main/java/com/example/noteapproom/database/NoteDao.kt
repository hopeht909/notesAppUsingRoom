package com.example.noteapproom.database

import androidx.room.*

@Dao
interface NoteDao {

    //add a new Note
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note: NoteEntity)

    // a query to bring all the notes in the database
    @Query("SELECT * FROM NotesTable ORDER BY id ASC")
    fun getNotes(): List<NoteEntity>

    //update an existing note
    @Update
    suspend fun updateNote(note: NoteEntity)

    //delete note
    @Delete
    suspend fun deleteNote(note: NoteEntity)

}