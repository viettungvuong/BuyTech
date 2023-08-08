package com.tung.buytech

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
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

        grid = view.findViewById(R.id.suggestedProducts) //phần đề xuất sản phẩm (grid layout)


        searchBtn.setOnClickListener {
            moveToStart(view)
            var toSearch = searchBar.text.toString() //lay string tu searchbar
            search(toSearch)
            searchBtn.hideKeyboard()
        }



        return view
    }

    private fun moveToStart(view: View) {
        val textViewAnimator = AnimatorInflater.loadAnimator(requireContext(),R.animator.search_bar_animator)
        textViewAnimator.setTarget(view)
        textViewAnimator.start()
    }


    fun search(productName: String) {
        //search sản phẩm và thêm product view vào grid
        suggestions(productName, grid)
    }



    //hiện kết quả tìm kiếm
    fun suggestions(productName: String, grid: GridLayout) {
        //chiến thuật là ta sẽ gom lại những cái sản phẩm có tên đó
        //ta sẽ cho biết giá trung bình, giá cao nhất và giá rẻ nhất
        grid.removeAllViews() //xoá hết mọi view

        AppController.db.collection(collectionProducts)
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