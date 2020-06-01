package com.example.coen268project.View;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Model.ItemDao;
import com.example.coen268project.Presentation.Item;
import com.example.coen268project.Presentation.Utility;
import com.example.coen268project.R;
import com.google.firebase.storage.StorageReference;
import java.util.HashMap;
import java.util.Map;

/**
 * create an instance of this fragment.
 */
public class Update_Ads extends Fragment {
    Button btnUpdate;
    Button btnDelete;
    private EditText et_ProductName;
    private EditText et_Price;
    private TextView et_Description;
    private Item item;
    private ImageView img_ProductPicture;

    public Update_Ads() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_ads, container, false);
        item = new Item();
        et_ProductName = view.findViewById(R.id.et_ProductName);
        et_Price = view.findViewById(R.id.et_Price);
        et_Description = view.findViewById(R.id.et_Description);
        img_ProductPicture = view.findViewById(R.id.product_image);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnDelete = view.findViewById(R.id.btnDelete);
        Bundle bundle = getArguments();
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
                final String product_name = et_ProductName.getText().toString();
                final String product_price = et_Price.getText().toString();
                final String product_description = et_Description.getText().toString();
                updates.put("itemName", product_name);
                updates.put("description", product_description);
                updates.put("price", product_price);
                item.updateItem(itemId, (HashMap) updates, new CallBack() {
                    @Override
                    public void onSuccess(Object object) {
                        Toast.makeText(getContext(),"Order updated successfully",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Object object) {
                        Toast.makeText(getContext(),"Order update failed",Toast.LENGTH_LONG).show();
                    }
                });
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                item.deleteItem(itemId, new CallBack() {
                    @Override
                    public void onSuccess(Object object) {
                        Toast.makeText(getContext(),"Order deleted successfully",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Object object) {
                        Toast.makeText(getContext(),"Order update failed",Toast.LENGTH_LONG).show();
                    }
                });
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void BindItems(ItemDao itemDao) {
        Utility utility = new Utility();
        utility.getItemPicture(itemDao.getPictureName(), new CallBack() {
            @Override
            public void onSuccess(Object object) {
                BindImage(object);
            }

            @Override
            public void onError(Object object) {

            }
        });
        et_ProductName.setText(itemDao.getItemName());
        et_Description.setText(itemDao.getDescription());
        et_Price.setText(itemDao.getPrice());
    }

    private void BindImage(Object object) {
        StorageReference storageReference = (StorageReference) object;
        Glide.with(getActivity()).load(storageReference.toString()).into(img_ProductPicture);
    }
}