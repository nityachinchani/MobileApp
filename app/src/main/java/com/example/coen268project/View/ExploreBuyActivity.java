package com.example.coen268project.View;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Model.ItemDao;
import com.example.coen268project.Presentation.Item;
import com.example.coen268project.Presentation.Utility;
import com.example.coen268project.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ExploreBuyActivity extends AppCompatActivity {
    private ImageView productImage;
    private TextView productPrice,productDescription,productTitle, productStatus;
    private Button exploreBuyBtn;
    private Item item;
    private ItemDao obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_buy);

        Bundle bundle = getIntent().getExtras();
        item=new Item();
        productImage=findViewById(R.id.product_image);
        productTitle=findViewById(R.id.product_title);
        productPrice=findViewById(R.id.product_price);
        exploreBuyBtn=findViewById(R.id.exploreBuyBtn);
        productStatus=findViewById(R.id.product_status);
        productDescription=findViewById(R.id.product_description);


        if(bundle!=null){
          final  String resId= bundle.getString("Item");
            item.getItem(resId, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    obj = (ItemDao) object;
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
            public void onClick(View v)
            {
                if(Utility.getCurrentUserId().equals(obj.getSellerId()))
                {
                    Toast.makeText(ExploreBuyActivity.this,"You are the owner of this item, you cannot chat with yourself.",Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(ExploreBuyActivity.this, OneToOneChatActivity.class);
                intent.putExtra("sellerId",obj.getSellerId());
                intent.putExtra("buyerId",obj.getBuyerId());
                startActivity(new Intent(intent));
            }
        });
    }

    private void BindItems(ItemDao itemDao) {
        productTitle.setText(itemDao.getItemName());
        productPrice.setText(itemDao.getPrice());
        productDescription.setText(itemDao.getDescription());
        productStatus.setText(itemDao.getItemStatus());
        Glide.with(ExploreBuyActivity.this).load(itemDao.getPictureName()).into(productImage);
    }
}
