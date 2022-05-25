package com.example.socketslearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Connection mConnect = null;
    private EditText usernameInput = null;
    private EditText passwordInput = null;
    private Button mBtnLogin  = null;

    private String LOG_TAG = "SOCKET";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mConnect = Connection.getInstance();

        mBtnLogin = (Button) findViewById(R.id.btn_login);
        usernameInput = (EditText) findViewById(R.id.usernameInput);
        passwordInput = (EditText) findViewById(R.id.passwordnput);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    LoginBtnClick();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void LoginBtnClick() throws Exception {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        String dataToSend = String.format("{" +
                "\"oper\": \"login\", " +
                "\"username\": \"%s\", " +
                "\"password\": \"%s\" " +
                "}", username, password);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

//                        if (text.trim().length() == 0)
//                            text = "Test message";
                    // отправляем сообщение

                    mConnect.sendData(dataToSend.getBytes());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Context context = getApplicationContext();
                            CharSequence text = "Hello toast!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    });

                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }
        }).start();


    }
}