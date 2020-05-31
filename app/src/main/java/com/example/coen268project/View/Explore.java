package com.example.coen268project.View;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Explore extends Fragment {
//    private static final String[] titles = new String[]{"Item1", "Item2",
//            "Item3", "Item4", "Item5","Item6", "Item7", "Item8","Item9", "Item10"};

    private ArrayList<ItemDao> itemList = new ArrayList<>();
    private Item item;

    int[] numberImage = {R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4, R.drawable.test5, R.drawable.test6
            , R.drawable.ic_account_box_black_24dp, R.drawable.ic_camera_alt_black_24dp, R.drawable.ic_chat_bubble_black_24dp, R.drawable.ic_current
            , R.drawable.ic_find_in_page_black_24dp};

    private static final String[] category = Utility.Category.toArray();

    private static String[] location = null;




   // private ListView exploreListView;
    private Spinner locationSpinner, categorySpinner;
    private GridView exploreGridView;
    private TextView textView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.explore_fragment, container, false);

        item = new Item();
        //exploreListView = (ListView) view.findViewById(R.id.explore_List_View);
        categorySpinner = (Spinner) view.findViewById(R.id.categorySpinner);
        locationSpinner = (Spinner) view.findViewById(R.id.locationSpinner);
        exploreGridView = (GridView) view.findViewById(R.id.exploreGridView);

        item.getAllItems(new CallBack() {
            @Override
            public void onSuccess(Object object) {
                Object[] objectArray = (Object[]) object;
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


        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_spinner_row, R.id.text_id, category);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(), location_fragment.class);
//                intent.putExtra("Item", (CharSequence) categorySpinner.getItemAtPosition(position+1));
//                startActivity(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        item.getAllLocations(new CallBack() {
            @Override
            public void onSuccess(Object object) {
                Object[] obj=(Object[]) object;
                ArrayList<String> arrayList=new ArrayList<>();
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




        return view;
    }

    private void bindLocation() {
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_spinner_row, R.id.text_id, location);
        locationSpinner.setAdapter(locationAdapter);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public void BindItems() {
        final MainAdapter_explore_screen adapter = new MainAdapter_explore_screen(itemList, numberImage);
        exploreGridView.setAdapter(adapter);
        exploreGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), Explore_buy.class);
                intent.putExtra("Item", (CharSequence) adapter.getItem(i).getItemId());
                startActivity(intent);
            }
        });
    }


    public class MainAdapter_explore_screen extends BaseAdapter {
        ArrayList<ItemDao> items;
        int[] numberImage; // remove this parameter

        public MainAdapter_explore_screen(ArrayList<ItemDao> items, int[] numberImage) {
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
            LayoutInflater inflater = getLayoutInflater();

            convertView=inflater.inflate(R.layout.explore_screen_adapter_element, null);
            ImageView imageView = convertView.findViewById(R.id.image_view);
            TextView textView = convertView.findViewById(R.id.text_view);

            imageView.setImageResource(numberImage[position]); // set same as in explore_buy
            textView.setText(this.items.get(position).getItemName());

            return convertView;
        }


    }

}