package com.example.coen268project.View;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.coen268project.R;

public class Sell_fragment extends Fragment {

    private static final String[] titles = new String[]{"Sofa and Dining", "Bed and Wardrobes",
            "Home Decor and Garden", "Kids Furniture", "Other Household Items"};

    private ListView listView;
    private TextView textView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sell, container, false);
        listView = (ListView) view.findViewById(R.id.list_view);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), R.layout.activity_row,R.id.text_id,titles);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(), (CharSequence) listView.getItemAtPosition(i),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), location_fragment.class);
                intent.putExtra("Item", (CharSequence) listView.getItemAtPosition(i+1));
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }
}


