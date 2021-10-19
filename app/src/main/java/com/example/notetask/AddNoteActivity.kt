package com.example.notetask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.notetask.common.Constants
import com.example.notetask.databinding.ActivityAddNoteBinding
import com.example.notetask.databinding.ActivityHomeBinding
import com.example.notetask.model.Note
import com.google.firebase.database.FirebaseDatabase

class AddNoteActivity : AppCompatActivity() {

    private var databaseRef=
        FirebaseDatabase.getInstance().reference.child("Notes").child(Constants.userUid!!).push()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding= DataBindingUtil.setContentView<ActivityAddNoteBinding>(this, R.layout.activity_add_note)

        binding.saveBtn.setOnClickListener({
            if (binding.noteEdt.text.isNullOrEmpty()){
                binding.noteEdt.error="The field cannot be blank"
                return@setOnClickListener
            }
            var noteString=binding.noteEdt.text.toString()
            var note= Note(databaseRef.key!!,noteString)
            databaseRef.setValue(note).addOnCompleteListener {
                Toast.makeText(this@AddNoteActivity, "The note added successfully", Toast.LENGTH_LONG).show()
                finish()
            }
        })

    }
}