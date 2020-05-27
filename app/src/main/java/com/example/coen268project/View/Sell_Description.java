package com.example.coen268project.View;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.coen268project.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Sell_Description} factory method to
 * create an instance of this fragment.
 */
public class Sell_Description extends Fragment {
    Button button_post;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_sell__description, container, false);

       //Getting Data From Upload_fragment
        Bundle bundle = getArguments();
        final String item = bundle.getString("Item");
        final String Location = bundle.getString("Location_1");
        final String path_1 = bundle.getString("Path");

        button_post = view.findViewById(R.id.button_post);

               button_post.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v)
                   {
                       Intent intent = new Intent(getActivity(), Main_Fragment_Controller.class);
                       startActivity(intent);
                   }
               });
       return view;
    }
}
