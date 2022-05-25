package com.example.socketslearn;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;
import android.view.ViewParent;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class Connection
{
    private static Connection connection;

    private  Socket  mSocket = null;
    private  String  mHost   = null;
    private  int     mPort   = 0;

    public final int BUFFER_SIZE = 2048;

    public static final String LOG_TAG = "SOCKET";

    public PrintWriter output;

    public BufferedReader stdIn;

    public BufferedReader in;

    public Connection() {}

    public Connection (final String host, final int port)
    {
        this.mHost = host;
        this.mPort = port;
    }

    public static Connection getInstance()
    {
        if (connection == null) {
            connection = new Connection("10.0.2.2", 5000);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        connection.openConnection();

                        Log.d(LOG_TAG, "Соединение установлено");
                        Log.d(LOG_TAG, "(mConnect != null) = "
                                + (connection != null));

                    } catch (Exception e) {
                        Log.e(LOG_TAG, e.getMessage());
                        connection = null;
                    }
                }
            }).start();
        }

        return connection;
    }

//    public int input() throws IOException {
//        return mSocket;
//    }

    // Метод открытия сокета
    public void openConnection() throws Exception
    {
        // Если сокет уже открыт, то он закрывается
        closeConnection();

        try {
            // Создание сокета
            mSocket = new Socket(mHost, mPort);

            in = new BufferedReader(
                    new InputStreamReader(
                            mSocket.getInputStream(), StandardCharsets.UTF_8));

            stdIn =                                       // 4th statement
                    new BufferedReader(
                            new InputStreamReader(System.in));

            listenData();


        } catch (IOException e) {
            throw new Exception("Невозможно создать сокет: "
                    + e.getMessage());
        }
    }
    /**
     * Метод закрытия сокета
     */
    public void closeConnection()
    {
        if (mSocket != null && !mSocket.isClosed()) {
            try {
                mSocket.close();
                in.close();
                stdIn.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Ошибка при закрытии сокета :"
                        + e.getMessage());
            } finally {
                mSocket = null;
            }
        }
        mSocket = null;
    }
    /**
     * Метод отправки данных
     */
    public void sendData(byte[] data) throws Exception {
        // Проверка открытия сокета
        if (mSocket == null || mSocket.isClosed()) {
            throw new Exception("Ошибка отправки данных. " +
                    "Сокет не создан или закрыт");
        }
        // Отправка данных
        try {
            mSocket.getOutputStream().write(data);
            mSocket.getOutputStream().flush();
        } catch (IOException e) {
            throw new Exception("Ошибка отправки данных : "
                    + e.getMessage());
        }
    }

    public void listenData() throws Exception{

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {

                        if (in.ready()) {
                            String inputLine;

                            inputLine = in.readLine();
                            Log.d(LOG_TAG, "JJS");

                            JSONObject jsonObject = new JSONObject(inputLine);

                            Log.d(LOG_TAG, jsonObject.get("chats").toString());

                        }

                    } catch (Exception e) {
                        Log.e(LOG_TAG, e.getMessage());
                        return;
                    }
                }

            }
        }).start();
    };

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
        closeConnection();
    }
}