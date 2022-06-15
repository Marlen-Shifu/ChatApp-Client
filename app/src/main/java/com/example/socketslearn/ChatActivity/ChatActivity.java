package com.example.socketslearn.ChatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.socketslearn.ChatsPage.Chat;
import com.example.socketslearn.ChatsPage.ChatAdapter;
import com.example.socketslearn.Connection;
import com.example.socketslearn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ChatActivity extends AppCompatActivity implements MessageAdapter.OnMessageListener {

    private Button backBtn;

    ArrayList<Message> messages = new ArrayList<Message>();

    private String LOG_TAG = "SOCKET";

    private MessageAdapter adapter;

    private static ChatActivity chatActivity;

    private EditText editText;

    private Connection mConnect;

    private String userId = "1";
    private int chatId;


    public static ChatActivity GetInstance()
    {
        if (chatActivity == null)
            chatActivity = new ChatActivity();

        return chatActivity;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatActivity = this;

        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackBtnClicked();
            }
        });

        editText = findViewById(R.id.text);

        mConnect = Connection.getInstance(GetInstance().getApplicationContext());

        try {
            setInitialData();
        } catch (JSONException e) {
            Log.d(LOG_TAG, e.toString());
        }

        RecyclerView recyclerView = findViewById(R.id.messages);
        // создаем адаптер
        adapter = new MessageAdapter(this, messages, this, "1");
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);

        findViewById(R.id.send_btn).setOnClickListener(view -> {
            SendNewMessage();
        });
    }

    public void NewMessageRecieved(String messageData) throws JSONException {
        JSONObject message = new JSONObject(messageData);

        messages.add(new Message(
                message.getInt("id"),
                message.getString("sender_id"),
                message.getString("sender"),
                message.getString("text"),
                message.getString("send_time")));

        adapter.notifyItemInserted(messages.size() - 1);
    }

    private void setInitialData() throws JSONException {
        JSONArray messagesData = new JSONArray(getIntent().getStringExtra("messages_data"));
        Log.d(LOG_TAG, messagesData.toString());

        chatId = (int) messagesData.getJSONObject(0).get("chat_id");

        for (int i = 0; i < messagesData.length(); i++)
        {
            JSONObject message = messagesData.getJSONObject(i);


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

    private void SendNewMessage(){
        String dataToSend = String.format("{" +
                "\"oper\": \"send_message\", " +
                "\"chat_id\": \"%s\"," +
                "\"sender_id\": \"%s\"," +
                "\"text\": \"%s\"" +
                "}", chatId, userId, editText.getText().toString());

        Log.d(LOG_TAG, dataToSend);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

//                        if (text.trim().length() == 0)
//                            text = "Test message";
                    // отправляем сообщение

                    mConnect.sendData(dataToSend.getBytes());

                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }
        }).start();
    }
}