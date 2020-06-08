package com.example.coen268project.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Presentation.Messages;
import com.example.coen268project.Presentation.Utility;
import com.example.coen268project.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatsFragment extends Fragment {
    private ListView listView;
    private TextView textView;
    private Messages messages;
    private HashMap<String, String> buyerList = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        listView = view.findViewById(R.id.list_view);
        messages = new Messages();
        messages.getAllBuyers(Utility.getCurrentUserId(), new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if(object == null)
                {
                    Toast.makeText(getContext(), "You do not have buyers you have chatted with", Toast.LENGTH_SHORT).show();
                    return;
                }
                buyerList.putAll((HashMap)object);
                BindItems();
            }

            @Override
            public void onError(Object object) {
            }
        });
        return view;
    }

    public void BindItems()
    {
        final CustomAdapter adapter = new CustomAdapter(buyerList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), OneToOneChatActivity.class);
                intent.putExtra("sellerId", Utility.getCurrentUserId());
                intent.putExtra("buyerId", (CharSequence) adapter.getItem(i));
                startActivity(intent);
            }
        });
    }

    /**
     * This class extends the BaseAdapter class to provide a Custom adapter for the list view
     * @author nitya
     */
    public class CustomAdapter extends BaseAdapter {
        class MyBuyers {
            private String name ="";
            private String uid ="";

            public MyBuyers(String uid, String name)
            {
                this.uid = uid;
                this.name = name;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }


            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }
        }

        ArrayList<MyBuyers> buyers = new ArrayList<>();
        public CustomAdapter(HashMap<String, String> buyerList) {
            Iterator hmIterator = buyerList.entrySet().iterator();
            while (hmIterator.hasNext()) {
                Map.Entry mapElement = (Map.Entry)hmIterator.next();
                MyBuyers myBuyers = new MyBuyers(mapElement.getKey().toString(), mapElement.getValue().toString());
                buyers.add(myBuyers);
            }
        }

        public int getCount() {
            return this.buyers.size();
        }

        public String getItem(int position) {
            return this.buyers.get(position).getUid();
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View activity_row;
            activity_row = inflater.inflate(R.layout.activity_buyer_list, parent, false);
            textView = activity_row.findViewById(R.id.text_id);
            textView.setText(this.buyers.get(position).getName());
            return activity_row;
        }
    }
}