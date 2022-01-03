package com.belous.v.clrc.core.data.net

import org.json.JSONObject
import java.io.*
import java.net.*
import java.util.*
import kotlin.collections.HashSet

object YeelightSource {

    private const val TIME_OUT = 3000
    const val SET_POWER = "set_power"
    const val SET_BRIGHT = "set_bright"
    const val SET_CT_ABX = "set_ct_abx"

    fun searchDevices(): List<Map<String, String>> {
        val responseSet = uDPResponse()
        return formatRespData(responseSet)
    }

    private fun uDPResponse(): HashSet<String> {
        val UDP_HOST = "239.255.255.250"
        val UDP_PORT = 1982
        val message = "M-SEARCH * HTTP/1.1\r\n" +
            "HOST:239.255.255.250:1982\r\n" +
            "MAN:\"ssdp:discover\"\r\n" +
            "ST:wifi_bulb\r\n"

        val datagramSocket = DatagramSocket()
        datagramSocket.soTimeout = TIME_OUT
        datagramSocket.send(
            DatagramPacket(
                message.toByteArray(),
                message.toByteArray().size,
                InetAddress.getByName(UDP_HOST),
                UDP_PORT
            )
        )
        val respList = HashSet<String>()
        try {
            while (true) {
                val buffer = ByteArray(65536)
                val datagramPacket = DatagramPacket(buffer, 65536)
                datagramSocket.receive(datagramPacket)
                respList.add(String(datagramPacket.data, 0, datagramPacket.length))
            }
        } catch (e: IOException) {
            return respList
        } finally {
            datagramSocket.close()
        }
    }

    private fun formatRespData(respList: HashSet<String>): List<Map<String, String>> {
        val devices = ArrayList<Map<String, String>>()
        val dropKeys = listOf("Ext", "Server", "Date", "Cache-Control")
        for (response in respList) {
            val params = HashMap<String, String>()
            response.split("\n".toRegex()).forEach {
                val line = it.split(": ".toRegex())
                if (!dropKeys.contains(line[0]) && line.size > 1) {
                    params[line[0]] = line[1].trim()
                }
            }
            devices.add(params)
        }
        return devices
    }

    fun getParams(ip: String, port: Int): HashMap<String, String> {
        val params = HashMap<String, String>()
        try {
            val socket = Socket()
            socket.connect(InetSocketAddress(ip, port), TIME_OUT)
            try {
                socket.soTimeout = TIME_OUT
                val paramList =
                    arrayOf("power", "bright", "ct", "active_mode", "color_mode", "rgb", "nl_br")
                val stringBuilder = StringBuilder()
                for (str in paramList) {
                    stringBuilder.append("\"")
                    stringBuilder.append(str)
                    stringBuilder.append("\",")
                }
                val message =
                    "{ \"id\": 1, \"method\": \"get_prop\", \"params\":[$stringBuilder]}\r\n"
                val bufferedOutputStream = BufferedOutputStream(socket.getOutputStream())
                bufferedOutputStream.write(message.toByteArray())
                bufferedOutputStream.flush()
                val bufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))
                val jsonObject = JSONObject(bufferedReader.readLine())
                val jsonArray = jsonObject.getJSONArray("result")
                for (i in 0 until jsonArray.length()) {
                    params[paramList[i]] = jsonArray.getString(i)
                }
                params["online"] = "1"
            } catch (e: Exception) {
                params["online"] = ""
            }
            socket.close()
        } catch (e: IOException) {
            params["online"] = ""
        }
        return params
    }

    fun setParams(ip: String, port: Int, args: Queue<String>): HashMap<String, String> {
        var params = HashMap<String, String>()
        try {
            val socket = Socket()
            socket.connect(InetSocketAddress(ip, port), TIME_OUT)
            try {
                socket.soTimeout = TIME_OUT
                val message =
                    "{\"id\":1,\"method\":\"" + args.poll() + "\",\"params\":" + args.toString() + "}\r\n"
                val bufferedWriter = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
                bufferedWriter.write(message)
                bufferedWriter.flush()
                val bufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))
                var jsonObject = JSONObject(bufferedReader.readLine())
                if (jsonObject.has("result")) {
                    try {
                        jsonObject = JSONObject(bufferedReader.readLine()).getJSONObject("params")
                        val iterator = jsonObject.keys()
                        while (iterator.hasNext()) {
                            val key = iterator.next()
                            params[key] = jsonObject.getString(key)
                        }
                    } catch (e: SocketTimeoutException) {
                        params = getParams(ip, port)
                    }
                }
                params["online"] = "1"
            } catch (e: Exception) {
                params["online"] = ""
            }
            socket.close()
        } catch (e: IOException) {
            params["online"] = ""
        }
        return params
    }
}