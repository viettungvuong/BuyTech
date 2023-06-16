package com.tung.buytech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ViewProduct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        Intent intent=getIntent();
        String s=intent.getStringExtra("ProductName");
        Long price=intent.getLongExtra("ProductPrice",0);
        int productId=intent.getIntExtra("ProductId",0);

        TextView productName,productPrice,productDescription;
        productName=findViewById(R.id.productLabel);
        productPrice=findViewById(R.id.priceLabel);
        productDescription=findViewById(R.id.productDescription);

        productName.setText(s);
        productPrice.setText(price.toString());
        productDescription.setText(getDescription(productId)); //lấy mô tả sản phẩm
    }

    String getDescription(int productId){

    }
}