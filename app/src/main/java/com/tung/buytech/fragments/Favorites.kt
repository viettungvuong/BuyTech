package com.tung.buytech.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tung.buytech.control.AppController
import com.tung.buytech.control.AppController.Companion.favorites
import com.tung.buytech.activities.BottomNavigationHandler
import com.tung.buytech.R
import com.tung.buytech.SwipeRecyclerHelper
import com.tung.buytech.adapters.CartRecyclerAdapter

class Favorites : Fragment() {

    lateinit var bottomNavigationHandler: BottomNavigationHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.favorite_page, container, false)

        if (AppController.favorites.isEmpty()) {
            // If favorites list is empty, show the EmptyFavoriteFragment
            childFragmentManager.beginTransaction()
                .replace(R.id.container, EmptyFavoriteFragment())
                .commit()
        } else {
            val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
            val recyclerView = rootView.findViewById<RecyclerView>(R.id.favRecyclerView)
            recyclerView.layoutManager = layoutManager
            val adapter = CartRecyclerAdapter(requireContext(), favorites)
            recyclerView.adapter = adapter

            val swipeHelperCallback =
                SwipeRecyclerHelper(adapter, requireContext()) // Callback for itemTouchHelper
            val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }


        return rootView
    }
}
