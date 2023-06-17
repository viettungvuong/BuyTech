package com.tung.buytech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
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
        Long price=intent.getLongExtra("ProductPrice",0);
        String productId=intent.getStringExtra("ProductId");

        TextView productName,productPrice,productDescription;
        productName=findViewById(R.id.productLabel);
        productPrice=findViewById(R.id.priceLabel);
        productDescription=findViewById(R.id.productDescription);

        productName.setText(s);
        productPrice.setText(price.toString());
        productDescription.setText(getDescription(productId)); //lấy mô tả sản phẩm
    }

    String getDescription(String productId){ //lấy description
        FirebaseFirestore db=AppController.getDatabaseInstance(); //truy cập kotlin từ file java
        DocumentReference docRef = db.collection("Items").document(productId);

        // Retrieve the document
        Task<DocumentSnapshot> document = docRef.get(); //Task là một dạng asynchronous (ví dụ như Runnable)

        // Retrieve the value of a specific field
        if (document.getResult().exists()) {
            DocumentSnapshot doc= document.getResult();
            String description = doc.getString("description");
            return description;
        } else {
            System.out.println("Document not found.");
            return null;
        }

    }
}