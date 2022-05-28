package com.example.socketslearn.ChatsPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.socketslearn.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatsPageActivity extends AppCompatActivity {

    ArrayList<Chat> chats = new ArrayList<Chat>();

    public static final String LOG_TAG = "SOCKET";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_page);

        // начальная инициализация списка
        try {
            setInitialData();
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        }
        RecyclerView recyclerView = findViewById(R.id.chats_list);
        // создаем адаптер
        ChatAdapter adapter = new ChatAdapter(this, chats);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);
    }

    private void setInitialData() throws Exception
    {
        JSONArray chatsData = new JSONArray(getIntent().getStringExtra("chats_data"));
        Log.d(LOG_TAG, chatsData.toString());

        for (int i = 0; i < chatsData.length(); i++)
        {
            JSONObject chat = chatsData.getJSONObject(i);

            chats.add(new Chat(chat.getString("title")));
        }

    }
}