package org.ancode.alivelib.utils;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by andyliu on 16-8-25.
 */
public class HttpHelper {
    private static final String TAG = HttpHelper.class.getSimpleName();
    public static Map<String, HttpURLConnection> urlgetConnections = null;
    public static Map<String, HttpURLConnection> urlpostConnections = null;
    public static final String CHARSET = "UTF-8";

    /**
     * post
     *
     * @param requestUrl
     * @param requestParamsMap
     * @return
     */
    public static String post(String requestUrl, Map<String, String> requestParamsMap, String flag) {
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        StringBuffer responseResult = new StringBuffer();
        StringBuffer params = new StringBuffer();
        HttpURLConnection httpURLConnection = null;
        // 组织请求参数
        Iterator it = requestParamsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry element = (Map.Entry) it.next();
            params.append(element.getKey());
            params.append("=");
            params.append(element.getValue());
            params.append("&");
        }
        if (params.length() > 0) {
            params.deleteCharAt(params.length() - 1);
        }

        try {
            URL realUrl = new URL(requestUrl);
            // 打开和URL之间的连接
            httpURLConnection = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            httpURLConnection.setRequestProperty("accept", "*/*");
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Accept-Charset", CHARSET);
            httpURLConnection.setRequestProperty("Content-Length", String
                    .valueOf(params.length()));
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            if (urlpostConnections == null) {
                urlpostConnections = new HashMap<String, HttpURLConnection>();
            }
            urlpostConnections.put(flag, httpURLConnection);

            // 获取URLConnection对象对应的输出流
            printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // 发送请求参数
            printWriter.write(params.toString());
            // flush输出流的缓冲
            printWriter.flush();
            // 根据ResponseCode判断连接是否成功
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode != 200) {
                Log.e(TAG, "错误 response=" + responseCode);
            } else {
                Log.e(TAG, "发送post请求成功!");
            }
            // 定义BufferedReader输入流来读取URL的ResponseData
            bufferedReader = new BufferedReader(new InputStreamReader(
                    httpURLConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                responseResult.append(line);
            }

        } catch (Exception e) {
            String error = e.getLocalizedMessage();
            if (error.contains("Permission denied")) {
                Log.e(TAG, "发送post请求错误!\n请配置'android.permission.INTERNET'权限");
            } else {
                Log.e(TAG, "发送post请求错误!\n" + error);
            }

        } finally {
            httpURLConnection.disconnect();
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                Log.e(TAG, "关闭http请求失败\n" + ex.getLocalizedMessage());
            }


        }
        Log.v(TAG, "返回数据=" + responseResult.toString());
        urlpostConnections.remove(flag);
        return responseResult.toString();
    }


    /**
     * postJson
     *
     * @param requestUrl
     * @param params
     * @return
     */
    public static String postJson(String requestUrl, String params, String flag) {
        BufferedReader bufferedReader = null;

        DataOutputStream out = null;
        StringBuffer responseResult = new StringBuffer();
        HttpURLConnection httpURLConnection = null;
        BufferedWriter writer = null;
        // 组织请求参数
        try {
            URL realUrl = new URL(requestUrl);
            // 打开和URL之间的连接
            httpURLConnection = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setConnectTimeout(1500);
            httpURLConnection.setReadTimeout(1500);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept-Charset", CHARSET);
//            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(params.getBytes().length));
            httpURLConnection.connect();
            if (urlpostConnections == null) {
                urlpostConnections = new HashMap<String, HttpURLConnection>();
            }
            urlpostConnections.put(flag, httpURLConnection);
            // 获取URLConnection对象对应的输出流
            out = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            Log.v(TAG, "上传的数据为\n" + params);
            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            writer = new BufferedWriter(new OutputStreamWriter(out, CHARSET));
            writer.write(params);
            writer.flush();

            // 根据ResponseCode判断连接是否成功
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode != 200) {
                Log.e(TAG, "错误 response=" + responseCode);
            } else {
                Log.e(TAG, "发送postJson请求成功!");
            }
            // 定义BufferedReader输入流来读取URL的ResponseData
            bufferedReader = new BufferedReader(new InputStreamReader(
                    httpURLConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                responseResult.append(line);
            }

        } catch (Exception e) {
            String error = e.getLocalizedMessage();
            if (!TextUtils.isEmpty(error)) {
                if (error.contains("Permission denied")) {
                    Log.e(TAG, "发送postJson请求错误!\n请配置'android.permission.INTERNET'权限");
                } else {
                    Log.e(TAG, "发送postJson请求错误!\n" + error);
                }
            }


        } finally {
            httpURLConnection.disconnect();
            try {
                if (writer != null) {
                    writer.close();
                }
                if (out != null) {
                    out.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }

            } catch (IOException ex) {
                Log.e(TAG, "关闭http请求失败\n" + ex.getLocalizedMessage());
            }


        }
        Log.v(TAG, "返回数据=" + responseResult.toString());
        urlpostConnections.remove(flag);
        return responseResult.toString();
    }


    /**
     * @param inputStream
     * @param encode
     * @return
     */
    private static String changeString(InputStream inputStream,
                                       String encode) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result = null;
        if (inputStream != null) {
            try {
                while ((len = inputStream.read(data)) != -1) {
                    byteArrayOutputStream.write(data, 0, len);
                }
                result = new String(byteArrayOutputStream.toByteArray(), encode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 向指定URL发送GET方法的请求
     */
    public static String get(String url, String encode, String flag) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            if (urlgetConnections == null) {
                urlgetConnections = new HashMap<String, HttpURLConnection>();
            }
            urlgetConnections.put(flag, conn);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "text/plain; charset=" + encode);
            // 建立实际的连接
            conn.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = conn.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            Log.e(TAG, " 发送GET请求出现异常！\n" + e.getLocalizedMessage());
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        urlgetConnections.remove(flag);
        return result;
    }

    public static void cancelAll() {
        try {
            if (urlgetConnections != null)
                for (Map.Entry<String, HttpURLConnection> entry : urlgetConnections.entrySet()) {
                    entry.getValue().disconnect();
                }

            if (urlpostConnections != null)
                for (Map.Entry<String, HttpURLConnection> entry : urlpostConnections.entrySet()) {
                    entry.getValue().disconnect();
                }
        } catch (Exception e) {
            Log.e(TAG, "urlConnection disConnect error\n" + e.getLocalizedMessage());
        }

    }


}
