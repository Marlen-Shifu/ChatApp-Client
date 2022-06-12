package com.example.socketslearn.ChatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.socketslearn.ChatsPage.Chat;
import com.example.socketslearn.ChatsPage.ChatAdapter;
import com.example.socketslearn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ChatActivity extends AppCompatActivity implements MessageAdapter.OnMessageListener {

    private Button backBtn;

    ArrayList<Message> messages = new ArrayList<Message>();

    private String LOG_TAG = "SOCKET";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackBtnClicked();
            }
        });

        try {
            setInitialData();
        } catch (JSONException e) {
            Log.d(LOG_TAG, e.toString());
        }

        RecyclerView recyclerView = findViewById(R.id.messages);
        // создаем адаптер
        MessageAdapter adapter = new MessageAdapter(this, messages, this, "1");
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);
    }

    private void setInitialData() throws JSONException {
        JSONArray messagesData = new JSONArray(getIntent().getStringExtra("messages_data"));
        Log.d(LOG_TAG, messagesData.toString());

        for (int i = 0; i < messagesData.length(); i++)
        {
            JSONObject message = messagesData.getJSONObject(i);

            Log.d(LOG_TAG, message.toString());

            messages.add(new Message(
                    message.getInt("id"),
                    message.getString("sender_id"),
                    message.getString("sender"),
                    message.getString("text"),
                    message.getString("send_time")));
        }
    }

    private void BackBtnClicked()
    {
        finish();
    }

    @Override
    public void onMessageClick(int pos) {

    }
}