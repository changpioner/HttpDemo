package com.nam;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.nam.entrance.signPublic;

/**
 * Created by Namhwik on 2017/11/18.
 */
public class GoodsPropertiesEntrance {
    private static Map<String, Object> bodyMap = new HashMap<>();
    private static Map<String, Object> bizData = new HashMap<>();
    public static void main(String[] args) throws JSONException, IOException {
        String url = "https://tapi.bz365.com/api/smgw.do";
        // 电子保单邮件

        bizData.put("apiVersion", "1.0.1");
        bizData.put("partnerId","tongxing");
        bizData.put("osType","ios");
        bizData.put("apiVersion","1.0.1");
        bizData.put("busiType","openapi");
        bizData.put("apiName","goodsProps");
        bizData.put("goodsId","1110227");

        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8Bhzx/GAG0+0qug7gYbkIPAnKT074+9OvYNsOCRNXgY5mWQNayxBqczBSWxm3dBJGGsYp16e74xuwsd5J8OyQ5lKlsmqtBlNU2zVD1s5udidur6TrCycqSwJBdv9VGvrBGQ4gWe/ADiaAoU/dbyQt0L/niVNuDgdMxybT88JpYQIDAQAB";
        bodyMap.put("bizData", signPublic(JSON.toJSONString(bizData), publicKey));
        bodyMap.put("partnerId","tongxing");
        bodyMap.put("apiVersion","1.0.1");
        StringEntity requestEntity = new StringEntity(JSON.toJSONString(bodyMap), "UTF-8");
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-type", "application/json");
        post.setHeader("Connection", "close");
        post.setEntity(requestEntity);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(post);
        String result = EntityUtils.toString(response.getEntity());
        JSONObject resultJson = new JSONObject(result);
        httpclient.close();
        System.out.println(resultJson.toString());
    }
}
