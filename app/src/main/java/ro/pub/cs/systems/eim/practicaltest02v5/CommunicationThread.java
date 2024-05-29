package ro.pub.cs.systems.eim.practicaltest02v5;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;
    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utillities.getReader(socket);
            PrintWriter printWriter = Utillities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type!");
            String info = bufferedReader.readLine();
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Received info: " + info);
            String action = info.split(",")[0];
            String key = info.split(",")[1];
            String value = "";
            if(Objects.equals(action, "put")){
                value = info.split(",")[2];
            }

            String pageSourceCode = "";
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://worldtimeapi.org/api/ip");
            HttpResponse httpGetResponse = httpClient.execute(httpGet);
            HttpEntity httpGetEntity = httpGetResponse.getEntity();
            if (httpGetEntity != null) {
                pageSourceCode = EntityUtils.toString(httpGetEntity);
            }

            JSONObject content = new JSONObject(pageSourceCode);
            String time = content.getString("datetime");

            if(Objects.equals(action, "put")){
                serverThread.data.put(key, new KeyInfo(value, time));
                printWriter.println("putSuccess");
            } else if (Objects.equals(action, "get")){
                if(serverThread.data.containsKey(key)){
                    String timePut = serverThread.data.get(key).getTimestamp();

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O ) {
                        ZonedDateTime dateTime1 = ZonedDateTime.parse(timePut, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                        ZonedDateTime dateTime2 = ZonedDateTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

                        Duration duration = Duration.between(dateTime1, dateTime2);

                        if( duration.abs().getSeconds() < 20){
                            printWriter.println(serverThread.data.get(key).getValue());
                            printWriter.flush();
                        } else {
                            printWriter.println("expired");
                            printWriter.flush();
                        }
                    }
                }else{
                    printWriter.println("Notfound");
                    printWriter.flush();
                }
            }

            Log.e(Constants.TAG, "time response" + pageSourceCode);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
