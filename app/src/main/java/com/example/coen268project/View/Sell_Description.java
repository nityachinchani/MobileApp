package com.example.coen268project.View;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.coen268project.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Sell_Description} factory method to
 * create an instance of this fragment.
 */
public class Sell_Description extends Fragment {
    Button button_post;
    private EditText p_name;
    private EditText p_price;
    private TextView p_description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_sell__description, container, false);

        p_name = view.findViewById(R.id.edit_text_file_name);
        p_price = view.findViewById(R.id.edit_text_file_price);
        p_description = view.findViewById(R.id.Description);
        button_post = view.findViewById(R.id.button_post);

       //Getting Data From Upload_fragment
        Bundle bundle = getArguments();
        final String item = bundle.getString("Item");
        final String Location = bundle.getString("Location_1");
        final String path_1 = bundle.getString("Path");

        final String product_name = p_name.getText().toString();
        final String product_price = p_price.getText().toString();
        final String product_description = p_description.getText().toString();


               button_post.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v)
                   {

                       if (p_name.getText().toString().trim().isEmpty()) {
                           p_name.setError("Name required");
                           p_name.requestFocus();
                           return;
                       }

                       if (p_price.getText().toString().trim().isEmpty()) {
                           p_price.setError("Price required");
                           p_price.requestFocus();
                           return;
                       }

                       Intent intent = new Intent(getActivity(), Main_Fragment_Controller.class);
                       startActivity(intent);
                   }
               });
       return view;
    }

}
