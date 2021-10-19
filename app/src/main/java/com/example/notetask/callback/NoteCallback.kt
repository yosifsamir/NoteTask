package com.example.notetask.callback

import com.example.notetask.model.Note


interface NoteCallback {
    fun noteItemClick(note: Note)
}