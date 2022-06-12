package com.example.socketslearn.ChatsPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.socketslearn.ChatActivity.ChatActivity;
import com.example.socketslearn.Connection;
import com.example.socketslearn.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatsPageActivity extends AppCompatActivity implements ChatAdapter.OnChatListener {

    ArrayList<Chat> chats = new ArrayList<Chat>();

    public static final String LOG_TAG = "SOCKET";

    private Connection mConnect = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_page);

        mConnect = Connection.getInstance(getApplicationContext());


        // начальная инициализация списка
        try {
            setInitialData();
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        }
        RecyclerView recyclerView = findViewById(R.id.chats_list);
        // создаем адаптер
        ChatAdapter adapter = new ChatAdapter(this, chats, this);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);
    }

    private void setInitialData() throws Exception
    {
        JSONArray chatsData = new JSONArray(getIntent().getStringExtra("chats_data"));

        for (int i = 0; i < chatsData.length(); i++)
        {
            JSONObject chat = chatsData.getJSONObject(i);

            chats.add(new Chat(chat.getInt("id"), chat.getString("title")));
        }

    }

    @Override
    public void onChatClick(int pos) {
        Log.d(LOG_TAG, "onclick");
        String dataToSend = String.format("{" +
                "\"oper\": \"messages\", " +
                "\"chat_id\": \"%s\"" +
                "}", String.valueOf(chats.get(pos).getId()));
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