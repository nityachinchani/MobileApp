package com.example.coen268project.View;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Firebase.FirebaseChildCallback;
import com.example.coen268project.Model.ItemDao;
import com.example.coen268project.Model.MessagesDao;
import com.example.coen268project.Presentation.Account;
import com.example.coen268project.Presentation.Messages;
import com.example.coen268project.Presentation.Utility;
import com.example.coen268project.R;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
public class OneToOneChatActivity extends AppCompatActivity {
    private ListView listView;
    private EditText et_MessageContent;
    private Messages messages;
    private Button btnSend;
    private String sellerId = "";
    private String buyerId = "";
    private String buyerName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_to_one_chat);
        listView = findViewById(R.id.list_view);
        btnSend = findViewById(R.id.button_chatbox_send);
        et_MessageContent = findViewById(R.id.edittext_chatbox);
        messages = new Messages();
        sellerId = getIntent().getStringExtra("sellerId");
        buyerId = getIntent().getStringExtra("buyerId");
        if(buyerId.equals(""))
        {
            buyerId = Utility.getCurrentUserId();
            buyerName = Utility.getCurrentUserName();
        }

        messages.getAllMessageByChildEvent(sellerId, buyerId, new FirebaseChildCallback() {
            @Override
            public void onChildAdded(Object object) {
                BindItems((MessagesDao)object);
            }

            @Override
            public void onChildChanged(Object object) {

            }

            @Override
            public void onChildRemoved(Object object) {

            }

            @Override
            public void onChildMoved(Object object) {

            }

            @Override
            public void onCancelled(Object object) {

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String messageContent = et_MessageContent.getText().toString();
                et_MessageContent.setText("");
                messages.setBuyerName(sellerId, buyerId, buyerName, new CallBack() {
                    @Override
                    public void onSuccess(Object object) {
                        messages.createMessage(sellerId, buyerId, Utility.getCurrentUserId(), Utility.getCurrentUserName(), messageContent, new CallBack() {
                            @Override
                            public void onSuccess(Object object) {
                            }

                            @Override
                            public void onError(Object object) {
                            }
                        });
                    }

                    @Override
                    public void onError(Object object) {
                    }
                });
            }
        });
    }

    CustomAdapter adapter;
    public void BindItems(MessagesDao messagesDao)
    {
        if(adapter == null) {
            adapter = new CustomAdapter();
        }
        adapter.AddItem(messagesDao);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }

    /**
     * This class extends the BaseAdapter class to provide a Custom adapter for the list view
     * @author nitya
     */
    public class CustomAdapter extends BaseAdapter {
        ArrayList<MessagesDao> messagesDaos = new ArrayList<>();
        public CustomAdapter() {
        }

        public int getCount() {
            return messagesDaos.size();
        }

        public void AddItem(MessagesDao messagesDao)
        {
            this.messagesDaos.add(messagesDao);
        }

        public MessagesDao getItem(int position) {
            return this.messagesDaos.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            TextView txt_name;
            TextView txt_messageContent;
            convertView = inflater.inflate(R.layout.activity_chat_row, parent, false);
            txt_name = convertView.findViewById(R.id.name);
            txt_messageContent = convertView.findViewById(R.id.messageContent);
            txt_name.setText(this.messagesDaos.get(position).getName() +": ");
            txt_messageContent.setText(this.messagesDaos.get(position).getMessageContent());
            return convertView;
        }
    }
}
