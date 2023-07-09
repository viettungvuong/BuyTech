package com.tung.buytech

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.tung.buytech.AppController.Companion.db
import com.tung.buytech.AppController.Companion.storageRef
import java.util.*


class SellPage  : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        var images = LinkedList<Uri>()

        val mainActivity = MainActivity()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.sell_product);
        supportActionBar?.hide()


        val priceInput=findViewById<TextInputEditText>(R.id.productPrice)
        priceInput.addTextChangedListener(object : TextWatcher {
            //có thể để biến ở anonymous class
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed in this example.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            }

            override fun afterTextChanged(s: Editable?) {
                priceInput.removeTextChangedListener(this)

                val originalString = s.toString().replace(",","")

                if (!originalString.isEmpty()) {
                    val longVal=originalString.toLong()
                    val formattedString: String = AppController.reformatNumber(longVal)
                    priceInput.setText(formattedString)
                    priceInput.setSelection(formattedString.length)
                }

                priceInput.addTextChangedListener(this)
            }
        })


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
                            180,
                            180 //set width và height
                        )
                        params.setMargins(10,10,10,10);
                        newImg.setLayoutParams(params)
                        gallery.addView(newImg) //thêm ảnh vào phần image view
                        images.add(uri)  //thêm tên file vào
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

        val sellBtn=findViewById<Button>(R.id.sellBtn)
        sellBtn.setOnClickListener(
            View.OnClickListener {
                for (image in images){
                    uploadImage(storageRef,image) //upload từng hình lên
                }
                sellItem(db,images)
            }
        )

        var navBar=findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.selectedItemId=R.id.sell
        navBar.setOnItemSelectedListener { item ->
            // do stuff
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.cart -> {
                    val intent = Intent(this, Cart::class.java)
                    startActivity(intent)
                }
                R.id.favorite -> {
                    val intent = Intent(this, Favorites::class.java)
                    startActivity(intent)
                }
                R.id.sell -> {
                    val intent = Intent(this, SellPage::class.java)
                    startActivity(intent)
                }
                R.id.account -> {
                    val intent = Intent(this, UserPage::class.java)
                    startActivity(intent)
                }
            }

            return@setOnItemSelectedListener true
        }

    }

    fun addImageToLayout(gallery: LinearLayout, pickMultipleMedia: ActivityResultLauncher<PickVisualMediaRequest>){

        //define pickMultipleMedia rồi tiến hành launch
       try{
           pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
       }
       catch (e: java.lang.Exception){
           Log.d("Exception",e.toString())
       }

    }

    fun sellItem(db: FirebaseFirestore, images: LinkedList<Uri>){
        val productName=findViewById<TextInputEditText>(R.id.productName).text.toString()
        val productPrice=findViewById<TextInputEditText>(R.id.productPrice).text.toString()
        val tag=findViewById<TextInputEditText>(R.id.tag).text.toString()

        var filterImages: LinkedList<String> = LinkedList()

        for (i in 0..images.lastIndex){
            filterImages.add(filterFileNameFromUri(images[i]))
        }

        // Add a new document with a generated id.
        val data = hashMapOf(
            "name" to productName,
            "price" to productPrice.toInt(),
            "tag" to arrayListOf<String>(tag),
            "image" to filterImages,
        )

        db.collection("Items")
            .add(data)
            .addOnSuccessListener { documentReference ->

            }
            .addOnFailureListener { e ->

            }
    }

    fun uploadImage(storageRef: StorageReference, file: Uri){
        var newFileName=filterFileNameFromUri(file) //cái này chỉ để đặt tên
        var fullFileName = Firebase.auth.currentUser!!.uid+"/"+newFileName
        val imageRef = storageRef.child(fullFileName)

        var uploadTask = imageRef.putFile(file)

        uploadTask.addOnFailureListener {
            // upload thất bại
        }.addOnSuccessListener { taskSnapshot ->
           // upload thành công
        }
    }

    fun filterFileNameFromUri(uri: Uri): String{
        var uriStr=uri.toString()
        val ssplit=uriStr.split("/")
        return ssplit[ssplit.lastIndex]
        //lọc filename lấy từ cuối
    }
}