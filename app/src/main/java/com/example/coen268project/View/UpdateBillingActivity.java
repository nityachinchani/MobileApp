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

public class UpdateBillingActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_update_billing);
        Bundle bundle = getIntent().getExtras();
        item = new Item();
        tv_ProductName = findViewById(R.id.product_title);
        tv_Price = findViewById(R.id.product_price);
        tv_Description = findViewById(R.id.product_description);
        img_ProductPicture = findViewById(R.id.product_image);
        btnUpdate = findViewById(R.id.btnUpdate);
        statusSpinner = findViewById(R.id.billingStatusSpinner);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(UpdateBillingActivity.this, R.layout.activity_spinner_row, R.id.text_id, billingStatus);
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
                if(statusSpinner.getSelectedItem().toString().equals(Utility.BillingStatus.DELIVERED))
                {
                    updates.put("buyerId", Utility.getCurrentUserId());
                }

                if(statusSpinner.getSelectedItem().toString().equals(Utility.BillingStatus.RECEIVED)) {
                    updates.put("itemStatus", Utility.ItemStatus.SOLD.toString());
                }
                item.updateItem(itemId, (HashMap) updates, new CallBack() {
                    @Override
                    public void onSuccess(Object object) {
                        Toast.makeText(UpdateBillingActivity.this,"Billing status updated successfully",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Object object) {
                        Toast.makeText(UpdateBillingActivity.this,"Billing status update failed",Toast.LENGTH_LONG).show();
                    }
                });
                Intent intent = new Intent(UpdateBillingActivity.this, MyBillingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void BindItems(ItemDao itemDao) {
        statusSpinner.setSelection(Utility.ItemStatus.getIndex(itemDao.getItemStatus()));
        tv_ProductName.setText(itemDao.getItemName());
        tv_Description.setText(itemDao.getDescription());
        tv_Price.setText(itemDao.getPrice());
        Glide.with(UpdateBillingActivity.this).load(itemDao.getPictureName()).into(img_ProductPicture);
    }
}
