package com.tung.buytech

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore


class SellPage  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sell_product);

        val gallery=findViewById<LinearLayout>(R.id.images)
        val pickMultipleMedia =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
                if (uris.isNotEmpty()) {
                    Log.d("PhotoPicker", "Number of items selected: ${uris.size}")
                    for (uri in uris){
                        var newImg=ImageView(this)
                        Glide.with(this)
                            .load(uri)
                            .into(newImg)
                        val params = LinearLayout.LayoutParams(
                            128,
                            128 //set width và height
                        )
                        params.setMargins(10,10,10,10);
                        newImg.setLayoutParams(params)
                        gallery.addView(newImg) //thêm ảnh vào phần image view
                    }
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        //cho chọn tối đa 5 items
        val addImageBtn=findViewById<Button>(R.id.addImageBtn)
        addImageBtn.setOnClickListener(
            View.OnClickListener {

                addImageToLayout(gallery,pickMultipleMedia)
            }
        )
    }

    fun addImageToLayout(gallery: LinearLayout, pickMultipleMedia: ActivityResultLauncher<PickVisualMediaRequest>){

        //define pickMultipleMedia rồi tiến hành launch

        pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
    }

    fun sellItem(db: FirebaseFirestore){
        val productName=findViewById<TextInputEditText>(R.id.productName).text
        val productPrice=findViewById<TextInputEditText>(R.id.productPrice)
        val tag=findViewById<TextInputEditText>(R.id.tag)

        // Add a new document with a generated id.
        val data = hashMapOf(
            "name" to productName,
            "price" to productPrice,
            "tag" to tag,
        )

        db.collection("Items")
            .add(data)
            .addOnSuccessListener { documentReference ->

            }
            .addOnFailureListener { e ->

            }
    }
}