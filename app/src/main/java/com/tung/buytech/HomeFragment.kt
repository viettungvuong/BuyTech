package com.tung.buytech

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.GridLayout
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.tung.buytech.AppController.Companion.hideKeyboard

class HomeFragment: Fragment() {
    lateinit var grid: GridLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home, container, false)

        var searchBtn = view.findViewById<Button>(R.id.search)
        var searchBar = view.findViewById<TextInputEditText>(R.id.productSearch)

        grid = view.findViewById(R.id.suggestedProducts) //phần đề xuất sản phẩm


        searchBtn.setOnClickListener(
            View.OnClickListener {
                var toSearch = searchBar.text.toString() //lay string tu searchbar
                search(toSearch, AppController.db)
                searchBtn.hideKeyboard()
            }
        )


        return view
    }


    fun search(productName: String, db: FirebaseFirestore) {
        //search sản phẩm và thêm product view vào grid
        suggestions(productName, db, grid)
    }



    //hiện kết quả tìm kiếm
    fun suggestions(productName: String, db: FirebaseFirestore, grid: GridLayout) {
        //chiến thuật là ta sẽ gom lại những cái sản phẩm có tên đó
        //ta sẽ cho biết giá trung bình, giá cao nhất và giá rẻ nhất
        grid.removeAllViews() //xoá hết mọi view

        db.collection(collectionProducts)
            .whereEqualTo(fieldProduct, productName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                //lấy từng sản phẩm thoả điều kiện
                for (document in querySnapshot) {
                    // Access other fields as needed
                    AppController.bindProductById(document.id) { bindedProduct ->
                        grid.addView(ProductView(requireContext(),bindedProduct)) //them product view vao grid layout
                    }

                }
            }
    }


}