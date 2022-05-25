package com.example.socketslearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    private Button mBtnOpen  = null;
    private Button mBtnSend  = null;
    private Button mBtnLogin  = null;
    private Button mBtnClose = null;
    private EditText mEdit = null;
    public TextView text = null;
    private Connection mConnect = null;

    private String HOST = "10.120.51.22";
    private int PORT = 9876;

    private String LOG_TAG = "SOCKET";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnOpen = (Button) findViewById(R.id.btn_open );
        mBtnSend = (Button) findViewById(R.id.btn_send );
        mBtnLogin = (Button) findViewById(R.id.btn_login_page);
        mBtnClose = (Button) findViewById(R.id.btn_close);
        mEdit = (EditText) findViewById(R.id.edText);
        text = (TextView) findViewById(R.id.textView);

        mBtnSend .setEnabled(false);
        mBtnClose.setEnabled(false);

        mBtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOpenClick();
            }
        });

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendClick();
            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnLoginClick();
            }
        });

        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCloseClick();
            }
        });
    }

    private void OnLoginClick()
    {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    private void onOpenClick()
    {
        mConnect = Connection.getInstance();

        mBtnSend.setEnabled(true);
        mBtnClose.setEnabled(true);
    }

    private void onSendClick()
    {
        String text;
        text = mEdit.getText().toString();

        String dataToSend = String.format("{" +
                "\"oper\": \"send-message\", " +
                "\"id\": \"3\", " +
                "\"chat_id\": \"1\", " +
                "\"text\": \"%s\"" +
                "}", text);

        SendData(dataToSend);
    }

    private void SendData(String data)
    {
        if (mConnect == null) {
            Log.d(LOG_TAG, "Соединение не установлено");
        }  else {
            Log.d(LOG_TAG, "Отправка сообщения");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

//                        if (text.trim().length() == 0)
//                            text = "Test message";
                        // отправляем сообщение

                        mConnect.sendData(data.getBytes());

                    } catch (Exception e) {
                        Log.e(LOG_TAG, e.getMessage());
                    }
                }
            }).start();
        }
    }

    private void onCloseClick()
    {
        // Закрытие соединения
        mConnect.closeConnection();
        // Блокирование кнопок
        mBtnSend .setEnabled(false);
        mBtnClose.setEnabled(false);
        Log.d(LOG_TAG, "Соединение закрыто");
    }
}