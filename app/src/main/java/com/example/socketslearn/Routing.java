package com.example.socketslearn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.socketslearn.ChatsPage.ChatsPageActivity;

import org.json.JSONObject;


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
        }

    }

    private void Login(String chatsData)
    {
        Intent i = new Intent(mContext, ChatsPageActivity.class);
        i.putExtra("chats_data", chatsData);
        mContext.startActivity(i);
    }
}
