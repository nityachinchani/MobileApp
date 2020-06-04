package com.example.coen268project.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Model.ItemDao;
import com.example.coen268project.Presentation.Item;
import com.example.coen268project.R;

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


public class AccountBillingActivity extends AppCompatActivity {
    int[] numberImage = {R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4, R.drawable.test5, R.drawable.test6
            , R.drawable.ic_account_box_black_24dp, R.drawable.ic_camera_alt_black_24dp, R.drawable.ic_chat_bubble_black_24dp, R.drawable.ic_current
            , R.drawable.ic_find_in_page_black_24dp};
    private Item item;
    private ArrayList<ItemDao> itemList = new ArrayList<>();
    private GridView billingGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_billing);
        item = new Item();
        billingGridView= (GridView)findViewById(R.id.billingGridView);
        getAllItems();
    }



    private void getAllItems(){
        item.getAllItems(new CallBack() {
            @Override
            public void onSuccess(Object object) {
                Object[] objectArray = (Object[]) object;
                itemList.clear();
                for (Object itemElement : objectArray
                ) {
                    itemList.add((ItemDao) itemElement);
                }
                BindItems();
            }

            @Override
            public void onError(Object object) {
            }
        });
    }

    public void BindItems() {
        final MainAdapter_billing_info adapter = new MainAdapter_billing_info(itemList, numberImage);
        billingGridView.setAdapter(adapter);
        billingGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(AccountBillingActivity.this,adapter.getItem(i).getItemName(),Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(AccountBilling.this, Explore_buy.class);
//                intent.putExtra("Item", (CharSequence) adapter.getItem(i).getItemId());
//                startActivity(intent);
            }
        });
    }




    public class MainAdapter_billing_info extends BaseAdapter {
        LayoutInflater inflater;
        ArrayList<ItemDao> items;
        int[] numberImage; // remove this parameter

        public MainAdapter_billing_info(ArrayList<ItemDao> items, int[] numberImage) {
            this.items = items;
            this.numberImage = numberImage;
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
            convertView=inflater.inflate(R.layout.billing_info_adapter_element, null);
            ImageView imageView = convertView.findViewById(R.id.imageBilling);
            TextView nameText = convertView.findViewById(R.id.nameBilling);
            TextView categoryText = convertView.findViewById(R.id.categoryBilling);
            TextView statusText = convertView.findViewById(R.id.statusBilling);

            imageView.setImageResource(numberImage[position]); // set same as in explore_buy
            nameText.setText(this.items.get(position).getItemName());
            categoryText.setText(this.items.get(position).getCategory());
            statusText.setText("Sold");
            return convertView;
        }
    }
}



