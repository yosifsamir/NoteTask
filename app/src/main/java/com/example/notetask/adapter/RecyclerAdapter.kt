package com.example.notetask.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.notetask.R
import com.example.notetask.callback.NoteCallback
import com.example.notetask.common.Constants
import com.example.notetask.model.Note


class RecyclerAdapter(private val context:Context ,private val notesList: MutableList<Note>) : RecyclerView.Adapter<RecyclerAdapter.NoteRecyclerViewHolder>() {
    var noteCallback: NoteCallback? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)

        return NoteRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: NoteRecyclerViewHolder, position: Int) {
        holder.noteText.setText(notesList[position].note)
        holder.itemView.setOnClickListener({
            noteCallback!!.noteItemClick(notesList[position])
        })

    }



    inner class NoteRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteText=itemView.findViewById<TextView>(R.id.note_text_TextView)
    }
}