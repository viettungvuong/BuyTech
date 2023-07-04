package com.tung.buytech

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class PeopleAdapter(private val list: LinkedList<AppController.People>): RecyclerView.Adapter<PeopleAdapter.PeopleHolder>()  {
    inner class PeopleHolder(val peopleView: View): RecyclerView.ViewHolder(peopleView){
        fun bind(people: AppController.People){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleHolder {

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PeopleHolder, position: Int) {
        val currentItem = list[position] //lấy vật ở vị trí thứ position trong list
        holder.bind(currentItem)
    }
}