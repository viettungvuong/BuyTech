package com.tung.buytech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.Future;

public class ViewProduct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);


        Intent intent=getIntent();
        String s=intent.getStringExtra("ProductName");
        String price=intent.getStringExtra("ProductPrice");
        String productId=intent.getStringExtra("ProductId");
        String productImage=intent.getStringExtra("ProductImage");

        TextView productName,productPrice,productDescription;
        productName=findViewById(R.id.productLabel);
        productPrice=findViewById(R.id.priceLabel);
        productDescription=findViewById(R.id.productDescription);

        productName.setText(s);
        productPrice.setText(price);

        //đặt hình ảnh sản phẩm
        ImageView imgView=findViewById(R.id.imageView);
        getImage(productImage,imgView);

        //mô tả sản phẩm
        getDescription(productId,productDescription); //lấy mô tả sản phẩm
        productDescription.setTextSize(20);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMarginStart(50);
        productDescription.setLayoutParams(layoutParams);

        Button purchaseBtn=findViewById(R.id.buttonPurchase);
        Button favoriteBtn=findViewById(R.id.buttonFavorite);

        purchaseBtn.setOnClickListener(
                v->{
                    //xuất ra thông tin của người mua
                }
        );

        favoriteBtn.setOnClickListener(
                v->{

                }
        );
    }

    void getDescription(String productId, TextView descriptionText){ //lấy description

        FirebaseFirestore db=AppController.getDatabaseInstance(); //truy cập kotlin từ file java
        DocumentReference docRef = db.collection(MainActivity.collectionProducts).document(productId);

        // Retrieve the document
        Task<DocumentSnapshot> document = docRef.get(); //Task là một dạng asynchronous (ví dụ như Runnable)

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Docexists", "DocumentSnapshot data: " + document.getData());
                        Log.d("Docexists2", "DocumentSnapshot data: " + document.get("description"));
                        descriptionText.setText(document.get("description").toString()); //set text
                    } else {
                        Log.d("Docnodoc", "No such document");
                    }
                } else {
                    Log.d("Docfailed", "get failed with ", task.getException());
                }
            }
        });

    }

    //đặt hình ảnh
    void getImage(String productImage, ImageView imageView){
        Glide.with(this)
                .load(productImage)
                .into(imageView);
    }
}