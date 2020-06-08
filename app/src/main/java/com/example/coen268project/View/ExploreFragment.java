package com.example.coen268project.View;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;

public class ExploreFragment extends Fragment {
    private ArrayList<ItemDao> itemList = new ArrayList<>();
    private Item item;
    private static String[] category = Utility.Category.toArray();
    private static String[] location = null;
    private Spinner locationSpinner, categorySpinner;
    private GridView exploreGridView;
    private TextView textView;
    Button goBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        item = new Item();
        categorySpinner = (Spinner) view.findViewById(R.id.categorySpinner);
        locationSpinner = (Spinner) view.findViewById(R.id.locationSpinner);
        exploreGridView = (GridView) view.findViewById(R.id.exploreGridView);
        goBtn=(Button) view.findViewById(R.id.goBtn);


        getAllItems();
        if(!category[0].equals("All")) {
            ArrayList<String> tempArray = new ArrayList<>();
            tempArray.clear();
            tempArray.add("All");
            tempArray.addAll(Arrays.asList(category));
            category = tempArray.toArray(new String[0]);
        }
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_spinner_row, R.id.text_id, category);
        categorySpinner.setAdapter(categoryAdapter);

        item.getAllLocations(new CallBack() {
            @Override
            public void onSuccess(Object object) {
                Object[] obj=(Object[]) object;
                ArrayList<String> arrayList=new ArrayList<>();
                arrayList.clear();
                arrayList.add("All");
                for (Object location: obj
                     ) {
                    arrayList.add(location.toString());
                }
                location = arrayList.toArray(new String[0]);
                bindLocation();
            }

            @Override
            public void onError(Object object) {
            }
        });

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSpecificItem();
            }
        });
        return view;
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

    private void getSpecificItem() {
        String selectedCategory= categorySpinner.getSelectedItem().toString();
        String selectedLocation = locationSpinner.getSelectedItem().toString();
        if(!selectedCategory.equals("All") && !selectedLocation.equals("All")) {
            getSpecificCategory(selectedCategory);
            getSpecificLocation(selectedLocation);
        }
        else if (!selectedCategory.equals("All") && selectedLocation.equals("All")) {
            getSpecificCategory(selectedCategory);
        }
        else if (selectedCategory.equals("All") && !selectedLocation.equals("All")) {
            getSpecificLocation(selectedLocation);
        }
        else if (selectedCategory.equals("All") && selectedLocation.equals("All"))
        {
            getAllItems();
        }
    }

    private void getSpecificCategory(String selectedCategory)
    {
        item.getItemsByCategory(selectedCategory, new CallBack() {
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

    private void getSpecificLocation(String selectedLocation)
    {
        item.getItemsByLocation(selectedLocation, new CallBack() {
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

    private void bindLocation() {
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_spinner_row, R.id.text_id, location);
        locationSpinner.setAdapter(locationAdapter);
    }


    public void BindItems() {
        final MainAdapter_explore_screen adapter = new MainAdapter_explore_screen(itemList);
        exploreGridView.setAdapter(adapter);
        exploreGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ExploreBuyActivity.class);
                intent.putExtra("Item", (CharSequence) adapter.getItem(i).getItemId());
                startActivity(intent);
            }
        });
    }


    public class MainAdapter_explore_screen extends BaseAdapter {
        ArrayList<ItemDao> items;

        public MainAdapter_explore_screen(ArrayList<ItemDao> items) {
            this.items = items;
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
            LayoutInflater inflater = getLayoutInflater();
            convertView=inflater.inflate(R.layout.activity_card_view, null);
            ImageView imageView = convertView.findViewById(R.id.image_view);
            TextView textView = convertView.findViewById(R.id.text_view);
            Glide.with(getActivity()).load(this.items.get(position).getPictureName()).into(imageView);
            textView.setText(this.items.get(position).getItemName());
            return convertView;
        }
    }
}