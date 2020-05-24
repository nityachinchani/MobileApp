package com.example.coen268project.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coen268project.R;

import java.util.*;

public class Sell extends AppCompatActivity  {


    private static final String[] titles = new String [] { "Sofa and Dining" , "Bed and Wardrobes",
            "Home Decor and Garden", "Kids Furniture","Other Household Items"  };

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        listView = (ListView) findViewById(R.id.list_view);
        final CustomBaseAdapter adapter = new CustomBaseAdapter(this, titles);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Sell.this,location.class);
                intent.putExtra("Item", titles[position]);
                startActivity(intent);
            }
        });
    }

    public class CustomBaseAdapter extends BaseAdapter {
        Context context;
        String[] Title;

        public CustomBaseAdapter(Context context, String[] rowItems) {
            this.context = context;
            this.Title = rowItems;
        }


        @Override
        public int getCount() {
            return Title.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater mInflater = getLayoutInflater();
            view = mInflater.inflate(R.layout.activity_row, null);
            TextView textView = view.findViewById(R.id.text_id);
            textView.setText(this.Title[position]);
            return view;
        }
    }
}