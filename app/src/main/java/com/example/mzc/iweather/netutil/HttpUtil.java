package com.example.mzc.iweather.netutil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by MZC on 9/4/2016.
 */
public class HttpUtil {
    public static void sendHttpRequest(final String address, final boolean getWeatherInfo, final HttpCallbackListener listener){
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection connection=null;
                        try{
                         URL url=new URL(address);
                            BufferedReader reader;
                            connection=(HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            if(getWeatherInfo){
                                connection.setRequestProperty("apikey","Replace this string with your own baidu api key");
                                connection.connect();
                                InputStream in = connection.getInputStream();
                                reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
                            }
                            else{
                                connection.setConnectTimeout(8000);
                                connection.setReadTimeout(8000);
                                InputStream in = connection.getInputStream();
                                reader = new BufferedReader(new InputStreamReader(in));
                            }

                            StringBuilder response=new StringBuilder();
                            String line;
                            while((line=reader.readLine())!=null){
                                response.append(line);
                                response.append("\r\n");
                            }
                            if(listener!=null){
                               //callback onFinish()
                                listener.onFinish(response.toString());
                            }
                        }catch (Exception e){
                            if(listener!=null){
                                listener.onError(e);
                            }
                        }
                        finally{
                            if(connection!=null){
                                    connection.disconnect();
                            }
                        }
                    }
                }
        ).start();
    }
}
