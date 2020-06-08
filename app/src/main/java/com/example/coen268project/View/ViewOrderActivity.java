package com.example.coen268project.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Model.ItemDao;
import com.example.coen268project.Presentation.Item;
import com.example.coen268project.R;

public class ViewOrderActivity extends AppCompatActivity {
    private TextView tv_ProductName;
    private TextView tv_Price;
    private TextView tv_Description;
    private TextView tv_StatusValue;
    private TextView tv_Location;
    private Item item;
    private ImageView img_ProductPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        Bundle bundle = getIntent().getExtras();
        item = new Item();
        tv_ProductName = findViewById(R.id.product_title);
        tv_Price = findViewById(R.id.product_price);
        tv_Description = findViewById(R.id.product_description);
        img_ProductPicture = findViewById(R.id.product_image);
        tv_StatusValue = findViewById(R.id.tv_StatusValue);
        tv_Location = findViewById(R.id.tv_LocationValue);
        final String itemId = bundle.getString("ItemId");
        item.getItem(itemId, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                ItemDao itemDao = (ItemDao) object;
                BindItems(itemDao);
            }

            @Override
            public void onError(Object object) {

            }
        });
    }

    private void BindItems(ItemDao itemDao) {
        tv_ProductName.setText(itemDao.getItemName());
        tv_Description.setText(itemDao.getDescription());
        tv_Price.setText(itemDao.getPrice());
        tv_StatusValue.setText(itemDao.getItemStatus());
        tv_Location.setText(itemDao.getLocation());
        Glide.with(ViewOrderActivity.this).load(itemDao.getPictureName()).into(img_ProductPicture);
    }
}
