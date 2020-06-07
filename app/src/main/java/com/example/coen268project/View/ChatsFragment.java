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
import com.example.coen268project.Model.ItemDao;
import com.example.coen268project.Presentation.Item;
import com.example.coen268project.Presentation.Messages;
import com.example.coen268project.Presentation.Utility;
import com.example.coen268project.R;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {
    private ListView listView;
    private TextView textView;
    private Messages messages;
    private ArrayList<String> buyerList = new ArrayList<>();

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
                Object[] objectArray = (Object[]) object;
                for (Object buyerElement : objectArray
                ) {
                    buyerList.add(buyerElement.toString());
                }
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
        ArrayList<String> buyers = new ArrayList<>();
        public CustomAdapter(ArrayList<String> buyerList) {
            for (String buyer: buyerList
            ) {
                this.buyers.add(buyer);
            }
        }

        public int getCount() {
            return this.buyers.size();
        }

        public String getItem(int position) {
            return this.buyers.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View activity_row;
            activity_row = inflater.inflate(R.layout.activity_row, parent, false);
            textView = activity_row.findViewById(R.id.text_id);
            textView.setText(this.buyers.get(position));
            return activity_row;
        }
    }
}