package com.example.noteapproom

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapproom.database.NoteDatabase
import com.example.noteapproom.database.NoteEntity
import com.example.noteapproom.database.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val noteDao by lazy { NoteDatabase.getDatabase(this).noteDao()}
    private val repository  by lazy { NoteRepository(noteDao)}

    lateinit var btSubmit : Button
    lateinit var tvNewNote : EditText
    lateinit var rvNotes : RecyclerView
    lateinit var lv : List<NoteEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvNotes = findViewById(R.id.rvNotes)
        btSubmit = findViewById(R.id.btSubmit)
        tvNewNote = findViewById(R.id.tvNewNote)


        lv = listOf()
        getNotesList()

        updateRV()
        btSubmit.setOnClickListener {
            if(tvNewNote.text.isNotBlank() ){
                val newNote = tvNewNote.text.toString()
                addNote(newNote)
                tvNewNote.text.clear()
                tvNewNote.clearFocus()
                updateRV()
                Toast.makeText(this, "Note Added", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "please add a message", Toast.LENGTH_LONG).show()
            }
        }

    }
    fun updateRV(){
        rvNotes.adapter = RVAdapter(this, lv)
        rvNotes.layoutManager = LinearLayoutManager(this)

    }
    fun addNote(noteText: String){
     CoroutineScope(IO).launch {
         repository.addNote(NoteEntity(0, noteText))
     }
    }

    fun editNote(noteID : Int, noteText : String){
        CoroutineScope(IO).launch {
            repository.updateNote(NoteEntity(noteID, noteText))
        }
    }

    fun deleteNote(noteID : Int, noteText : String){
        CoroutineScope(IO).launch {
            repository.deleteNote(NoteEntity(noteID, noteText))
        }
    }

    fun getNotesList(){
        CoroutineScope(IO).launch {
            val data = async {
                repository.getNotes
            }.await()
            if(data.isNotEmpty()){
                lv = data
                updateRV()
            }else{
                Log.e("MainActivity", "Unable to get data", )
            }
        }
        }
    fun raiseDialog(id: Int){
        val dialogBuilder = AlertDialog.Builder(this)
        val updatedNote = EditText(this)
        updatedNote.hint = "Enter new text"
        dialogBuilder
            .setCancelable(false)
            .setPositiveButton("Save", DialogInterface.OnClickListener {

                    _, _ ->
                   run{
                       editNote(id, updatedNote.text.toString())
                       updateRV()
                       Toast.makeText(this, "Note Updated", Toast.LENGTH_LONG).show()
                   }
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, _ -> dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Update Note")
        alert.setView(updatedNote)
        alert.show()
    }
    fun checkDeleteDialog(id: Int){
        val dialogBuilder = AlertDialog.Builder(this)
        val checkTextView = TextView(this)
        checkTextView.text = "  Are sure you want to delete this note ?!"

        dialogBuilder
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener {

                    _, _ ->
              run{
                  deleteNote(id, checkTextView.text.toString())
                  updateRV()
                  Toast.makeText(this, "Note deleted", Toast.LENGTH_LONG).show()
              }
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, _ -> dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Check the deletion")
        alert.setView(checkTextView)
        alert.show()
    }
}

