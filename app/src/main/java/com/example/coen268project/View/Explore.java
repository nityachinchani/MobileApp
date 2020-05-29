package com.example.coen268project.View;
import com.example.coen268project.Model.ItemDao;
import com.example.coen268project.Presentation.Item;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Explore extends Fragment {
    private static final String[] titles = new String[]{"Item1", "Item2",
            "Item3", "Item4", "Item5","Item6", "Item7", "Item8","Item9", "Item10"};

    int[] numberImage={R.drawable.test1,R.drawable.test2,R.drawable.test3,R.drawable.test4,R.drawable.test5,R.drawable.test6
    ,R.drawable.ic_account_box_black_24dp,R.drawable.ic_camera_alt_black_24dp,R.drawable.ic_chat_bubble_black_24dp,R.drawable.ic_current
    ,R.drawable.ic_find_in_page_black_24dp};

    private static final String[] category = new String[]{"Sofa and Dining", "Bed and Wardrobes",
            "Home Decor and Garden", "Kids Furniture", "Other Household Items"};

    private static final String[] location = new String[]{"Santa Clara", "San jose",
            "Mountain View", "Palo Alto"};

    private ListView exploreListView;
    private Spinner locationSpinner, categorySpinner;
    private GridView exploreGridView;
    private TextView textView;
    private Item item;
    private ArrayList<ItemDao> itemList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.explore_fragment, container, false);

        //exploreListView = (ListView) view.findViewById(R.id.explore_List_View);
        categorySpinner = (Spinner) view.findViewById(R.id.categorySpinner);
        locationSpinner = (Spinner) view.findViewById(R.id.locationSpinner);
        exploreGridView=(GridView)view.findViewById(R.id.exploreGridView);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_row, R.id.text_id, titles);
//        exploreListView.setAdapter(adapter);
//        item = new Item();
//        item.getAllItems(new CallBack() {
//            @Override
//            public void onSuccess(Object object) {
//                Object[] objectArray = (Object[]) object;
//                for (Object itemElement : objectArray
//                ) {
//                    itemList.add((ItemDao) itemElement);
//                }
//                BindItems();
//            }
//
//            @Override
//            public void onError(Object object) {
//            }
//        });
        MainAdapter_explore_screen adapter= new MainAdapter_explore_screen(getActivity(),titles,numberImage);
        exploreGridView.setAdapter(adapter);
        exploreGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               // Toast.makeText(getContext(),"You clicked "+titles[+i],Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), Explore_buy.class);
                startActivity(intent);

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


        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_spinner_row, R.id.text_id, location);
        locationSpinner.setAdapter(locationAdapter);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(), location_fragment.class);
//                intent.putExtra("Item", (CharSequence) locationSpinner.getItemAtPosition(position+1));
//                startActivity(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }


    public void BindItems() {
        final CustomAdapter adapter = new CustomAdapter(itemList);
//        exploreListView.setAdapter(adapter);
//        exploreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(getActivity(), location_fragment.class);
//                intent.putExtra("Item", (CharSequence) adapter.getItem(i));
//                startActivity(intent);
//            }
//        });
    }

    public class CustomAdapter extends BaseAdapter {
        ArrayList<ItemDao> items = new ArrayList<>();

        public CustomAdapter(ArrayList<ItemDao> itemList) {
            for (ItemDao item : itemList
            ) {
                this.items.add(item);
            }
        }


        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return this.items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View activity_row;
            activity_row = inflater.inflate(R.layout.activity_row, parent, false);
            textView = activity_row.findViewById(R.id.text_id);
            textView.setText(this.items.get(position).getItemName());
            return activity_row;
        }
    }
}
