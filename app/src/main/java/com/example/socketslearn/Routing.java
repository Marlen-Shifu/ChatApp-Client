package com.example.socketslearn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.example.socketslearn.ChatActivity.ChatActivity;
import com.example.socketslearn.ChatsPage.ChatsPageActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.LongFunction;


public class Routing {

    private static Routing routing;

    public static final String LOG_TAG = "SOCKET";

    private static Context mContext;

    public static Routing GetInstance(Context context)
    {
        mContext = context;

        if (routing == null)
            routing = new Routing();

        return routing;
    }

    public void Route(String input) throws Exception
    {
        JSONObject jsonObject = new JSONObject(input);

        if (jsonObject.getBoolean("ok"))
        {
            if (jsonObject.get("oper").toString().equals("login"))
            {
                Login(jsonObject.get("chats").toString());
            }

            else if (jsonObject.get("oper").toString().equals("messages"))
            {
                Messages(jsonObject.get("messages").toString());
            }

            else if (jsonObject.get("oper").toString().equals("new_message"))
            {
                Log.d(LOG_TAG, "NEW_MESSAGE");
                NewMessage(jsonObject.get("message").toString());
            }
        }
    }

    private void Login(String chatsData)
    {
        Intent i = new Intent(mContext, ChatsPageActivity.class);
        i.putExtra("chats_data", chatsData);
        mContext.startActivity(i);
    }

    private void Messages(String messagesData)
    {
        Intent i = new Intent(mContext, ChatActivity.class);
        i.putExtra("messages_data", messagesData);
        mContext.startActivity(i);
    }


    private void NewMessage(String messageData) throws JSONException {
        ChatActivity chat = ChatActivity.GetInstance();
        chat.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    chat.NewMessageRecieved(messageData);
                } catch (JSONException e) {
                    Log.d(LOG_TAG, e.toString());
                }
            }
        });
    }
}
