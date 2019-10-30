package com.belous.v.clrc.model.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Yeelight {

    private static final int TIME_OUT = 3000;

    public static final String SET_POWER = "set_power";
    public static final String SET_BRIGHT = "set_bright";
    public static final String SET_CT_ABX = "set_ct_abx";

    public static List<Map<String, String>> searchDevices() throws IOException {
        return formatRespData(getUDPResponse());
    }

    private static List<String> getUDPResponse() throws IOException {
        String UDP_HOST = "239.255.255.250";
        int UDP_PORT = 1982;
        String message = "M-SEARCH * HTTP/1.1\r\n" +
                "HOST:239.255.255.250:1982\r\n" +
                "MAN:\"ssdp:discover\"\r\n" +
                "ST:wifi_bulb\r\n";

        DatagramSocket datagramSocket = new DatagramSocket();
        datagramSocket.setSoTimeout(TIME_OUT);
        datagramSocket.send(new DatagramPacket(message.getBytes(), message.getBytes().length, InetAddress.getByName(UDP_HOST), UDP_PORT));

        List<String> respList = new ArrayList<>();
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                byte[] buffer = new byte[65536];
                DatagramPacket datagramPacket = new DatagramPacket(buffer, 65536);
                datagramSocket.receive(datagramPacket);
                respList.add(new String(datagramPacket.getData(), 0, datagramPacket.getLength()));
            }
        } catch (IOException e) {
            return respList;
        } finally {
            datagramSocket.close();
        }
    }

    private static List<Map<String, String>> formatRespData(List<String> respList) {
        List<Map<String, String>> devices = new ArrayList<>();
        List<String> dropKeys = Arrays.asList("Ext", "Server", "Date", "Cache-Control");
        for (String response : respList) {
            Map<String, String> params = new HashMap<>();
            String[] str = response.split("\n");
            for (int i = 1; i < str.length; i++) {
                String[] line = str[i].split(": ");
                if (!dropKeys.contains(line[0])) {
                    params.put(line[0], line[1].trim());
                }
            }
            devices.add(params);
        }
        return devices;
    }

    public static Map<String, String> getParams(String ip, int port) {
        Map<String, String> params = new HashMap<>();

        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), TIME_OUT);
            try {
                socket.setSoTimeout(TIME_OUT);

                String[] paramList = {"power", "bright", "ct", "active_mode", "color_mode", "rgb", "nl_br"};
                StringBuilder stringBuilder = new StringBuilder();
                for (String str : paramList) {
                    stringBuilder.append("\"");
                    stringBuilder.append(str);
                    stringBuilder.append("\",");
                }
                String message = "{ \"id\": 1, \"method\": \"get_prop\", \"params\":[" + stringBuilder.toString() + "]}\r\n";

                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
                bufferedOutputStream.write(message.getBytes());
                bufferedOutputStream.flush();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                JSONObject jsonObject = new JSONObject(bufferedReader.readLine());
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    params.put(paramList[i], jsonArray.getString(i));
                }
                params.put("online", "1");
            } catch (IOException | JSONException | NullPointerException e) {
                params.put("online", "");
            }
            socket.close();
        } catch (IOException e) {
            params.put("online", "");
        }
        return params;
    }

    public static Map<String, String> setParams(String ip, int port, Queue<String> args) {
        Map<String, String> params = new HashMap<>();
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), TIME_OUT);
            try {
                socket.setSoTimeout(TIME_OUT);

                String message = "{\"id\":1,\"method\":\"" + args.poll() + "\",\"params\":" + args.toString() + "}\r\n";
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bufferedWriter.write(message);
                bufferedWriter.flush();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                JSONObject jsonObject = new JSONObject(bufferedReader.readLine());
                if (jsonObject.has("result")) {
                    try {
                        jsonObject = new JSONObject(bufferedReader.readLine()).getJSONObject("params");
                        for (Iterator<String> iterator = jsonObject.keys(); iterator.hasNext(); ) {
                            String key = iterator.next();
                            params.put(key, jsonObject.getString(key));
                        }
                    } catch (SocketTimeoutException e) {
                        params = getParams(ip, port);
                    }
                }
                params.put("online", "1");
            } catch (IOException | JSONException | NullPointerException e) {
                params.put("online", "");
            }
            socket.close();
        } catch (IOException e) {
            params.put("online", "");
        }
        return params;
    }
}
