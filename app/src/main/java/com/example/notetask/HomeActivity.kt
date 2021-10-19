package com.example.notetask

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notetask.adapter.RecyclerAdapter
import com.example.notetask.callback.NoteCallback
import com.example.notetask.common.Constants
import com.example.notetask.databinding.ActivityHomeBinding
import com.example.notetask.model.Note
import com.google.firebase.database.*

class HomeActivity : AppCompatActivity(), NoteCallback {

    private lateinit var  databaseRefRead: DatabaseReference
    private  var noteList= mutableListOf<Note>()
    private var recyclerAdapter= RecyclerAdapter(this,noteList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding= DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
        binding.progress.visibility=View.VISIBLE
        initRecyclerView(binding)

        databaseRefRead=FirebaseDatabase.getInstance().reference.child("Notes").child(Constants.userUid!!)
        databaseRefRead.addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@HomeActivity, "${error.message}", Toast.LENGTH_LONG).show()
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        noteList.clear()
                        for (note in snapshot.children)
                            noteList.add(note.getValue(Note::class.java)!!)
                    }
                    binding.progress.visibility=View.GONE
                    recyclerAdapter.notifyDataSetChanged()
                }
            })

        binding.btnAddNotes.setOnClickListener({
            startActivity(Intent(this,AddNoteActivity::class.java))
        })

    }

    private fun initRecyclerView(binding: ActivityHomeBinding) {
        binding.recyclerNote.setHasFixedSize(true)
        val linearLayoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.recyclerNote.layoutManager=linearLayoutManager
        binding.recyclerNote.adapter=recyclerAdapter
        recyclerAdapter.noteCallback=this

    }

    override fun noteItemClick(note: Note) {
        var intent= Intent(this,UpdateNoteActivity::class.java)
        intent.setData(Uri.parse("Notes/${Constants.userUid}/${note.noteId}"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}