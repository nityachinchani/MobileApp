package com.example.coen268project.View;
import com.example.coen268project.Presentation.Utility;
import com.example.coen268project.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class Explore extends Fragment {
    private static final String[] titles = new String[]{"Item1", "Item2",
            "Item3", "Item4", "Item5"};

    private static final String[] category = new String[]{"Sofa and Dining", "Bed and Wardrobes",
            "Home Decor and Garden", "Kids Furniture", "Other Household Items"};

    private static final String[] location = new String[]{"Santa Clara", "San jose",
            "Mountain View", "Palo Alto"};

    private ListView exploreListView;
    private Spinner locationSpinner, categorySpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.explore_fragment, container, false);

        exploreListView = (ListView) view.findViewById(R.id.explore_List_View);
        categorySpinner = (Spinner)view.findViewById(R.id.categorySpinner);
        locationSpinner = (Spinner)view.findViewById(R.id.locationSpinner);



        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), R.layout.activity_row,R.id.text_id,titles);
        exploreListView.setAdapter(adapter);

        exploreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), location_fragment.class);
                intent.putExtra("Item", (CharSequence) exploreListView.getItemAtPosition(i+1));
                startActivity(intent);
            }
        });

        ArrayAdapter<String> categoryAdapter=new ArrayAdapter<String>(getActivity(), R.layout.activity_spinner_row,R.id.text_id,category);
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


        ArrayAdapter<String> locationAdapter=new ArrayAdapter<String>(getActivity(), R.layout.activity_spinner_row,R.id.text_id,location);
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


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }
}
