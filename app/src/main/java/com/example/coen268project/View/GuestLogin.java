package com.example.coen268project.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Presentation.Account;
import com.example.coen268project.R;

import java.util.ArrayList;

public class GuestLogin extends AppCompatActivity {
    ListView android_versions;
    Account account;
    ArrayList<String> myAccounts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_login);
        account = new Account(this);
        android_versions = findViewById(R.id.android_versions);
        account.getAllAccounts(new CallBack() {
            @Override
            public void onSuccess(Object object) {
                Object[] objectArray = (Object[]) object;
                for (Object userName : objectArray
                ) {
                    myAccounts.add(userName.toString());
                }

               CallMe();
            }

            @Override
            public void onError(Object object) {
                Toast.makeText(GuestLogin.this, "No records to display", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CallMe() {
        String textString[] = new String[myAccounts.size()];
        for(int j =0;j<myAccounts.size();j++){
            textString[j] = myAccounts.get(j);
        }
        final CustomAdapter adapter = new CustomAdapter(this,  textString);
        android_versions.setAdapter(adapter);
        android_versions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GuestLogin.this, account_details.class);
                intent.putExtra("Android_version", adapter.Title[position]);
                startActivity(intent);
            }
        });
    }

    /**
     * This class extends the BaseAdapter class to provide a Custom adapter for the list view
     * @author nitya
     */
    public class CustomAdapter extends BaseAdapter {
        private Context context;
        private String[] Title;
        private int[] image;

        public CustomAdapter(Context context, String[] name) {
            context = context;
            Title = name;
        }

        public int getCount() {
            return Title.length;
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int position) {
            return position;
        }

        /**
         * Method that fetches the activity row view
         * @param position - position of the row in the activity_row view
         * @param convertView - convertView
         * @param parent - parent
         * @return The selected row view in the activity_row
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View activity_row;
            activity_row = inflater.inflate(R.layout.activity_row, parent, false);
            TextView textView;
            textView = activity_row.findViewById(R.id.textView);
            textView.setText(this.Title[position]);
            return activity_row;
        }
    }
}
