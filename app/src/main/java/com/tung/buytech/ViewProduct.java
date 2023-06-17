package com.tung.buytech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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

        TextView productName,productPrice,productDescription;
        productName=findViewById(R.id.productLabel);
        productPrice=findViewById(R.id.priceLabel);
        productDescription=findViewById(R.id.productDescription);

        productName.setText(s);
        productPrice.setText(price);
        productDescription.setText(getDescription(productId)); //lấy mô tả sản phẩm
    }

    String getDescription(String productId){ //lấy description
        FirebaseFirestore db=AppController.getDatabaseInstance(); //truy cập kotlin từ file java
        DocumentReference docRef = db.collection("Items").document(productId);

        // Retrieve the document
        Task<DocumentSnapshot> document = docRef.get(); //Task là một dạng asynchronous (ví dụ như Runnable)

        String description="";

        if (document.isSuccessful()){
            description = document.getResult().getString("description");
        }

        return description;

    }
}