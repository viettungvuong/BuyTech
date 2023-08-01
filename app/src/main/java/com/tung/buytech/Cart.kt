package com.tung.buytech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.LinkedList

class Cart
    : Fragment() {

    lateinit var bottomNavigationHandler: BottomNavigationHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.cart, container, false)

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.cartView)

        // Set recyclerView in LinearLayoutManager
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        recyclerView.layoutManager = layoutManager
        val adapter = CartRecyclerAdapter(requireContext(), AppController.cart)
        recyclerView.adapter = adapter
        // Assign the adapter to the recyclerView


        return rootView
    }

    fun deleteFromCart(itemInCart: AppController.Product, cart: LinkedList<AppController.Product>) {
        if (!cart.contains(itemInCart))
            return

        cart.removeAt(cart.indexOf(itemInCart))
    }
}
