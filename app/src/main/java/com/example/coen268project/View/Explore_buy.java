package com.example.coen268project.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Model.ItemDao;
import com.example.coen268project.Presentation.Item;
import com.example.coen268project.Presentation.Utility;
import com.example.coen268project.R;
import com.google.firebase.storage.StorageReference;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Explore_buy extends AppCompatActivity {
    private ImageView productImage;
    private TextView productPrice,productDescription,productTitle;
    private Item item;
    private Utility utility;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_buy);

        Bundle bundle = getIntent().getExtras();
        item=new Item();
        utility=new Utility();

        productImage=findViewById(R.id.product_image);
        productTitle=findViewById(R.id.product_title);
        productPrice=findViewById(R.id.product_price);
        productDescription=findViewById(R.id.product_description);


        if(bundle!=null){
          final  String resId= bundle.getString("Item");
            item.getItem(resId, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    ItemDao obj=(ItemDao) object;
                    BindItems(obj);


                    Toast.makeText(Explore_buy.this,"The activity works"+obj.getItemName(),Toast.LENGTH_LONG).show();

                }

                @Override
                public void onError(Object object) {

                }
            });

        }

    }

    private void BindItems(ItemDao itemDao) {
        utility.getItemPicture(itemDao.getPictureName(),new CallBack() {
            @Override
            public void onSuccess(Object object) {
                BindImage(object);
            }

            @Override
            public void onError(Object object) {

            }
        });
        productTitle.setText(itemDao.getItemName());
        productPrice.setText(itemDao.getPrice());
        productDescription.setText(itemDao.getDescription());

    }

    private void BindImage(Object object) {
        StorageReference storageReference = (StorageReference) object;
        //img_ProductPicture.setImageURI();
        Glide.with(this).load(storageReference).into(productImage);
    }
}
