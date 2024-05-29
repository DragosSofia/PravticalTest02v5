package ro.pub.cs.systems.eim.practicaltest02v5;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread{
    private String address;
    private int port;
    private String key;
    private String value;
    private String operation;
    private EditText weatherForecastTextView;

    public ClientThread(String address, int port, String operation, String key, String value, EditText weatherForecastTextView){
        this.address = address;
        this.port = port;
        this.operation = operation;
        this.key = key;
        this.value = value;
        this.weatherForecastTextView = weatherForecastTextView;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utillities.getReader(socket);
            PrintWriter printWriter = Utillities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }

            if(operation.equals("put")){
                printWriter.println(operation + "," + key + "," + value);
            } else {
                printWriter.println(operation + "," + key);
            }

            printWriter.flush();
            String weatherInformation = bufferedReader.readLine();
            //weatherForecastTextView.setText(weatherInformation);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    weatherForecastTextView.setText(weatherInformation);
                }
            });

        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
        }
    }
}
