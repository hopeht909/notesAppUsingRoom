package com.example.noteapproom

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapproom.database.NoteEntity
import com.example.noteapproom.databinding.NoteRowBinding


class RVAdapter(
    private val activity: MainActivity,
    private val items: List<NoteEntity>): RecyclerView.Adapter<RVAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: NoteRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVAdapter.ItemViewHolder {
        return ItemViewHolder(
            NoteRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RVAdapter.ItemViewHolder, position: Int) {
        val item = items[position]

        holder.binding.apply {
            tvNote.text = item.noteText
            if(position%2==0){llNoteHolder.setBackgroundColor(Color.GRAY)}
            ibEditNote.setOnClickListener {
                activity.raiseDialog(item.id)

            }
            ibDeleteNote.setOnClickListener {
                activity.checkDeleteDialog(item.id)
            }
        }
    }

    override fun getItemCount() = items.size
}
