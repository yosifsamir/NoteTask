package com.example.notetask

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.example.notetask.common.Constants
import com.example.notetask.databinding.ActivityUpdateNoteBinding
import com.example.notetask.model.Note
import com.google.firebase.database.*

class UpdateNoteActivity : AppCompatActivity() {
    lateinit var databaseRef: DatabaseReference
    lateinit var binding:ActivityUpdateNoteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView<ActivityUpdateNoteBinding>(this, R.layout.activity_update_note)
        if(intent!=null){
            if(intent.data!=null){
                var uri: Uri = intent.data!!
                var paths=uri.pathSegments

                Log.d("ddd","${paths[1]} ,\n" +
                        " ${paths[2]}")

                databaseRef=
                    FirebaseDatabase.getInstance().reference.child(paths[0]).child(paths[1]).child(paths[2])
                databaseRef.addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {

                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                var note: Note? =snapshot.getValue(Note::class.java)
                                updateUI(note)
                            }
                        }
                    })
                binding.copyLinkBtn.setOnClickListener({
                    val clipbroadManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clipData = ClipData.newPlainText("copy text","https://notetask123.page.link/Notes/${Constants.userUid}/${paths[2]}")
                    clipbroadManager.setPrimaryClip(clipData)
                    Toast.makeText(this,"Link is copied successfully", Toast.LENGTH_LONG).show()
                })

                binding.updateNoteEdt.addTextChangedListener {
                    Log.d("ddd", binding.updateNoteEdt.text.toString())
                    updateFirebase()
                }
            }
        }
        else{

        }

    }

    private fun updateUI(note: Note?) {
        binding.updateNoteEdt.setText(note!!.note)
        binding.updateNoteEdt.setSelection(binding.updateNoteEdt.length())
        binding.updateBtn.setOnClickListener({
            if (binding.updateNoteEdt.text.isNotEmpty()) {
//                Log.d("ddd", databaseRef.path.toString())
                updateFirebase()
            }
            else{
                binding.updateNoteEdt.setError("Enter Some Text !")
                binding.updateNoteEdt.requestFocus()
            }
        })
    }

    private fun updateFirebase() {
        var noteText = binding.updateNoteEdt.text.toString()
        var hashMap = HashMap<String, String>()
        hashMap.put("note", noteText)
        databaseRef.updateChildren(hashMap as Map<String, String>)





        /*
        -To solve data corrupted by concurrent modifications.
        -I am using Transaction but the problem still exists.
        -this is my code.



            databaseRef.runTransaction(object : Transaction.Handler {
            override fun onComplete(error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {
                Log.d("ddd","onComplete")
            }

            override fun doTransaction(currentData: MutableData): Transaction.Result {
                var note=currentData.getValue(Note::class.java)
                hashMap.put("noteId",note!!.noteId)
                currentData.child("note").setValue(noteText)
                return Transaction.success(currentData)
            }

        })

      */

    }
}