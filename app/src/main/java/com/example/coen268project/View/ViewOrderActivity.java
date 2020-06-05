package com.example.coen268project.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Model.ItemDao;
import com.example.coen268project.Presentation.Item;
import com.example.coen268project.Presentation.Utility;
import com.example.coen268project.R;

import java.util.HashMap;
import java.util.Map;

public class ViewOrderActivity extends AppCompatActivity {
    Button btnUpdate;
    private TextView tv_ProductName;
    private TextView tv_Price;
    private TextView tv_Description;
    private Spinner statusSpinner;
    private Item item;
    private ImageView img_ProductPicture;
    private static String[] billingStatus = Utility.BillingStatus.toArray();

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
        btnUpdate = findViewById(R.id.btnUpdate);
        statusSpinner = findViewById(R.id.itemStatusSpinner);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(ViewOrderActivity.this, R.layout.activity_spinner_row, R.id.text_id, billingStatus);
        statusSpinner.setAdapter(categoryAdapter);
        final String itemId = bundle.getString("ItemId");
        final Map<String, String> updates = new HashMap<>();
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

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                updates.put("billingStatus", statusSpinner.getSelectedItem().toString());
                item.updateItem(itemId, (HashMap) updates, new CallBack() {
                    @Override
                    public void onSuccess(Object object) {
                        Toast.makeText(ViewOrderActivity.this,"Order updated successfully",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Object object) {
                        Toast.makeText(ViewOrderActivity.this,"Order update failed",Toast.LENGTH_LONG).show();
                    }
                });
                Intent intent = new Intent(ViewOrderActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void BindItems(ItemDao itemDao) {
        statusSpinner.setSelection(Utility.ItemStatus.getIndex(itemDao.getItemStatus()));
        tv_ProductName.setText(itemDao.getItemName());
        tv_Description.setText(itemDao.getDescription());
        tv_Price.setText(itemDao.getPrice());
        Glide.with(ViewOrderActivity.this).load(itemDao.getPictureName()).into(img_ProductPicture);
    }
}
