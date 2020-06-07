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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MyBillingActivity extends AppCompatActivity {
    private Item item;
    private ArrayList<ItemDao> itemList = new ArrayList<>();
    private GridView billingGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_info);
        item = new Item();
        billingGridView= findViewById(R.id.billingGridView);
        getMyAds();
        getMyOrders();
    }

    private void getMyAds(){
        item.getMyAds(Utility.getCurrentUserId(), new CallBack() {
            @Override
            public void onSuccess(Object object) {
                Object[] objectArray = (Object[]) object;
                itemList.clear();
                if (object != null)
                {
                    for (Object itemElement : objectArray)
                    {
                        itemList.add((ItemDao) itemElement);
                    }
                }
                else{
                    Toast.makeText(MyBillingActivity.this,"You have not posted any Ads",Toast.LENGTH_SHORT).show();
                }
                BindItems();
            }

            @Override
            public void onError(Object object) {
            }
        });
    }

    private void getMyOrders(){
        item.getMyOrders(Utility.getCurrentUserId(), new CallBack() {
            @Override
            public void onSuccess(Object object) {
                Object[] objectArray = (Object[]) object;
                itemList.clear();
                if (object != null)
                {
                    for (Object itemElement : objectArray)
                    {
                        itemList.add((ItemDao) itemElement);
                    }
                }
                else{
                    Toast.makeText(MyBillingActivity.this,"You have no Orders",Toast.LENGTH_SHORT).show();
                }
                BindItems();
            }

            @Override
            public void onError(Object object) {
            }
        });
    }

    MainAdapter_billing_info adapter = null;
    public void BindItems() {
        if(adapter == null) {
            adapter = new MainAdapter_billing_info();
        }
        adapter.AddItems(itemList);
        billingGridView.setAdapter(adapter);
        billingGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MyBillingActivity.this,adapter.getItem(i).getItemName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MyBillingActivity.this, UpdateBillingActivity.class);
                intent.putExtra("ItemId", (CharSequence) adapter.getItem(i).getItemId());
                startActivity(intent);
            }
        });
    }

    public class MainAdapter_billing_info extends BaseAdapter {
        LayoutInflater inflater;
        ArrayList<ItemDao> items;

        public MainAdapter_billing_info() {
            this.items = new ArrayList<>();
        }

        public void AddItems(ArrayList<ItemDao> items)
        {
            this.items.addAll(items);
        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        @Override
        public ItemDao getItem(int position) {
            return this.items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            inflater = getLayoutInflater();
            convertView=inflater.inflate(R.layout.activity_billing_info_row, null);
            ImageView imageView = convertView.findViewById(R.id.imageBilling);
            TextView nameText = convertView.findViewById(R.id.nameBilling);
            TextView categoryText = convertView.findViewById(R.id.categoryBilling);
            TextView statusText = convertView.findViewById(R.id.statusBilling);
            Glide.with(MyBillingActivity.this).load(this.items.get(position).getPictureName()).into(imageView);
            nameText.setText(this.items.get(position).getItemName());
            categoryText.setText(this.items.get(position).getCategory());
            statusText.setText(this.items.get(position).getBillingStatus());
            return convertView;
        }
    }
}



