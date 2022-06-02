package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var actRecyclerView: RecyclerView
    private lateinit var tvLoadingData : TextView
    private lateinit var nameEvent : EditText
    private lateinit var datePicker: DatePicker
    private lateinit var actorList : ArrayList<Actor>
    private lateinit var dbRef : DatabaseReference
    private lateinit var Save: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        actRecyclerView = findViewById(R.id.rvActors)
        actRecyclerView.layoutManager = LinearLayoutManager(this)
        actRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)
        Save = findViewById(R.id.saveEvent)
        nameEvent = findViewById(R.id.nameEvent)
        datePicker = findViewById(R.id.datePicker)


        actorList = arrayListOf<Actor>()
        getActorsData()

        //Календарь
        val today = Calendar.getInstance()
        var dmy = "28.05.2022"
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
        { view, year, month, day ->
            val month = month + 1
            dmy = "$day.$month.$year"
        }
        dbRef = FirebaseDatabase.getInstance().getReference("Events")
        Save.setOnClickListener{
            saveEvent(dmy)
        }


    }
    var listAE=""
    private fun saveEvent(dmy: String) {

        val nameE = nameEvent.text.toString()
        val id = dbRef.push().key!!
        val event = Event(id,nameE,dmy, listAE)
        val mAdapter = ActorAdapter(actorList)
        dbRef.child(id).setValue(event)

            .addOnCompleteListener{
                Toast.makeText(this,"Мероприятие добавлено", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(this,"Ошибка", Toast.LENGTH_SHORT).show()
            }
        //mAdapter.getListEventActors(id)
        nameEvent.setText("")
        listAE=""
    }


    private fun getActorsData() {

        actRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE
        dbRef = FirebaseDatabase.getInstance().getReference("Actors")
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                actorList.clear()
                if(snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val empData = empSnap.getValue(Actor::class.java)
                        actorList.add(empData!!)
                    }
                    val mAdapter = ActorAdapter(actorList)

                    actRecyclerView.adapter = mAdapter
                    mAdapter.setOnItemClickListener(object : ActorAdapter.onItemClickListener{
                        override fun onItemClick(nameAct: String) {
                            Toast.makeText(this@MainActivity, "$nameAct", Toast.LENGTH_SHORT).show()
                            if(listAE.contains(nameAct)){

                            }
                            else {
                                listAE = listAE + ", " + nameAct
                            }

                        }

                    })
                    actRecyclerView.visibility =View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    //Меню
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add -> {
                val intent = Intent(this, InsertionActivity::class.java)
                startActivity(intent)}

        }
        return false
    }

}