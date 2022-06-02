package com.example.myapplication

import android.graphics.Color
import android.graphics.Color.BLUE
import android.graphics.Color.green
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowId
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase





class ActorAdapter (val actorList : ArrayList<Actor>) :
    RecyclerView.Adapter<ActorAdapter.ViewHolder>() {
    private lateinit var dbRef : DatabaseReference
    private lateinit var  mListener : onItemClickListener
    interface onItemClickListener{
        fun onItemClick(name: String)
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    /*var numberPosition = 0
    var eventActorId = ""*/


    inner class ViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val chName : CheckBox = itemView.findViewById(R.id.chName)
        fun bind(property: Actor, index: Int) {

            /*if (chName.isChecked){
                eventActorId = property.Id.toString()
            }*/
             val btndel: ImageButton = itemView.findViewById(R.id.button2)
            btndel.setOnClickListener {
                actorList.removeAt(index)
                dbRef = FirebaseDatabase.getInstance().getReference()
                val del =dbRef.child("Actors").child(property.Id.toString())
                del.removeValue();
                notifyDataSetChanged() }
        }


        init {
                itemView.setOnClickListener {
                    listener.onItemClick(chName.text.toString())
                }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_actor, parent, false)
        return ViewHolder(itemView, mListener)

    }
    /*fun getListEventActors(id: String){

        dbRef.child("Events").child(id).child("Event_Actors").setValue(eventActorId)
    }*/

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentact = actorList[position]
        holder.chName.text = currentact.Name
        holder.bind(actorList[position], position)

    }

    override fun getItemCount(): Int {
        return actorList.size
    }
}
