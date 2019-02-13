package com.example.barvius.learn2.HTTP;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadThread extends Thread {
    private Context applicationContext = null;
    private Request request = null;
    private String result;
    private String resultError;
    private Thread thisThread;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void run() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            urlConnection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
            urlConnection.setConnectTimeout(5000);

            if (request instanceof GET) {
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
            }
            if (request instanceof POST) {
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                urlConnection.setRequestProperty("Content-Length", "" + ((POST) request).getData().getBytes("UTF-8").length);
                urlConnection.setUseCaches(false);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setInstanceFollowRedirects(false);
                urlConnection.getOutputStream().write(((POST) request).getData().getBytes());
            }
//            resultError = urlConnection.getErrorStream().toString();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                result = buffer.toString();
            } else {
                result = "";
            }
        } catch (Exception e) {
            resultError = e.getMessage();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String GET(String url, String[]... params){
        String response = url;
        for (String[] i: params) {
            if (response.equals(url)){
                response += "?";
            } else {
                response += "&";
            }
            response += i[0];
            if (!i[1].equals("")) {
                response += "=";
                response += i[1];
            }
        }
        request = new GET(response);
        sendRequest();
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String POST(String url, String[]... params){
        String response = "";
        for (String[] i: params) {
            if (response.length() > 0){
                response += "&";
            }
            response += i[0];
            if (!i[1].equals("")) {
                response += "=";
                response += i[1];
            }
        }
        request = new POST(url,response);
        sendRequest();
        return result;
    }

    private void sendRequest(){
        resultError = "";
        thisThread = new Thread(this);

        thisThread.start();
        try {
            thisThread.join();
            if(applicationContext != null && !resultError.equals("")) {
                Toast toast = Toast.makeText(applicationContext,
                        resultError, Toast.LENGTH_LONG);
                toast.show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public LoadThread(){ }

    public LoadThread(Context context){
        applicationContext = context;
    }

}
