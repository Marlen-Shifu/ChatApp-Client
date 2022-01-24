package com.example.socketslearn;

import android.util.Log;
import android.widget.TextView;

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
    private  Socket  mSocket = null;
    private  String  mHost   = null;
    private  int     mPort   = 0;

    private MainActivity act = null;

    public final int BUFFER_SIZE = 2048;

    public static final String LOG_TAG = "SOCKET";

    public PrintWriter output;

    public BufferedReader stdIn;

    public BufferedReader in;

    public Connection() {}

    public Connection (final String host, final int port, MainActivity act)
    {
        this.mHost = host;
        this.mPort = port;
        this.act = act;
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

            Log.d(LOG_TAG, "before");
            listenData();
            Log.d(LOG_TAG, "after");


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
        Log.d(LOG_TAG, "closestart");
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

                            act.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    act.text.setText(inputLine);

                                }
                            });
                        }

//                closeConnection(); // disconnect server

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