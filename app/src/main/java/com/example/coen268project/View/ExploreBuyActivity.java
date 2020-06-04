package com.example.coen268project.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Model.ItemDao;
import com.example.coen268project.Presentation.Item;
import com.example.coen268project.Presentation.Utility;
import com.example.coen268project.R;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ExploreBuyActivity extends AppCompatActivity {
    private ImageView productImage;
    private TextView productPrice,productDescription,productTitle;
    private Button exploreBuyBtn;
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
        exploreBuyBtn=findViewById(R.id.exploreBuyBtn);
        productDescription=findViewById(R.id.product_description);


        if(bundle!=null){
          final  String resId= bundle.getString("Item");
            item.getItem(resId, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    ItemDao obj=(ItemDao) object;
                    BindItems(obj);


                    Toast.makeText(ExploreBuyActivity.this,"The activity works"+obj.getItemName(),Toast.LENGTH_LONG).show();

                }

                @Override
                public void onError(Object object) {

                }
            });

        }

        exploreBuyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
       // String s= "https://firebasestorage.googleapis."+storageReference.toString();
        //String s= "gs://coen268project-c7554.appspot.com/images/JPEG_20200527_201252_2758848949909595449.jpg";
       // Glide.with(this).load(storageReference.toString()).into(productImage);
        Picasso.with(this).load(storageReference.toString()).into(productImage);

    }
}
