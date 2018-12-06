package com.zao.java;

import java.io.IOException;
import java.util.Map;
import okhttp3.*;

public class MyOkHttp {

    public static OkHttpClient client = new OkHttpClient();

    /**
     * post使用的json头
     */
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    /**
     * get接口操作方法     *
     *
     * @param url 接口地址
     * @return
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            System.out.println("GET");
            return response.body().string();
        } else {
            throw new IOException("错误码: " + response);
        }
    }

    /**
     * 使用键值对调用post方法
     * @param url 接口链接
     * @param map 键值对
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> map) throws IOException {

        FormBody.Builder build = new FormBody.Builder();
        for (Map.Entry<String, String> item : map.entrySet()) {
            build.add(item.getKey(), item.getValue());
        }
        RequestBody formBody = build.build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            System.out.println("POST-Key-Value");
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * post方法,使用json
     *
     * @param url 接口链接
     * @param json 请求体
     * @return
     * @throws IOException
     */
    public static String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            System.out.println("POST-JSON");
            return response.body().string();
        } else {
            throw new IOException("错误码:" + response);
        }
    }

    /**
     * put方法,使用map传递键值对
     * @param url 接口链接
     * @param map 键值对
     * @return
     * @throws IOException
     */
    public static String put(String url, Map<String, String> map) throws IOException {

        FormBody.Builder build = new FormBody.Builder();
        for (Map.Entry<String, String> item : map.entrySet()) {
            build.add(item.getKey(), item.getValue());
        }
        RequestBody formBody = build.build();

        Request request = new Request.Builder()
                .url(url)
                .put(formBody)
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            System.out.println("PUT-Key-Value");
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * put方法,使用json
     * @param url 接口链接
     * @param json 键值对
     * @return
     * @throws IOException
     */
    public static String put(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).put(body).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            System.out.println("PUT-JSON");
            return response.body().string();
        } else {
            throw new IOException("错误码:" + response);
        }
    }
}