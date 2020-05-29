package com.example.coen268project.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coen268project.R;

public class MainAdapter_explore_screen extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private String[] titles;
    private int[] numberImage;

    public MainAdapter_explore_screen(Context c, String[] titles, int[] numberImage){
        context =c;
        this.titles=titles;
        this.numberImage=numberImage;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(layoutInflater==null)
        {
            layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.explore_screen_adapter_element,null);
        }
        ImageView imageView=convertView.findViewById(R.id.image_view);
        TextView textView = convertView.findViewById(R.id.text_view);

        imageView.setImageResource(numberImage[position]);
        textView.setText(titles[position]);

        return convertView;
    }
}
