package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {
    private lateinit var etName : EditText
    private lateinit var etGroup : EditText
    private lateinit var btnSave : Button

    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)
        etName = findViewById(R.id.etName)
        etGroup = findViewById(R.id.etGroup)
        btnSave = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Actors")
        btnSave.setOnClickListener{
            saveActorData()
        }
    }

    private fun saveActorData() {
        var name = etName.text.toString()
        var group = etGroup.text.toString()

        val id = dbRef.push().key!!
        val actor = Actor(id,name,group)
        dbRef.child(id).setValue(actor)
            .addOnCompleteListener{
                Toast.makeText(this,"Добавлено", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(this,"Ошибка", Toast.LENGTH_SHORT).show()
            }
        etName.setText("")
        etGroup.setText("")
    }
}