package com.tung.buytech

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class PeopleAdapter(private val list: LinkedList<AppController.People>): RecyclerView.Adapter<PeopleAdapter.PeopleHolder>()  {
    inner class PeopleHolder(val peopleView: View): RecyclerView.ViewHolder(peopleView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: PeopleHolder, position: Int) {
        TODO("Not yet implemented")
    }
}