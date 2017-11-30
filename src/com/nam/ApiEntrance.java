package com.nam;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.datanucleus.util.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.nam.entrance.signPublic;

/**
 * Created by Namhwik on 2017/11/12.
 */
public class ApiEntrance {
    public static void main(String[] args) throws JSONException, IOException, NoSuchAlgorithmException {
        String url = "https://tapi.bz365.com/api/smgw.do";
        // 电子保单邮件
        JSONObject bizJson = new JSONObject();
        //bizJson.put("partnerUserId","tongxing");
        bizJson.put("osType","ios");
        bizJson.put("apiVersion","1.0.1");
        bizJson.put("busiType","openapi");
        bizJson.put("apiName","calculatePrice");
        bizJson.put("goodsId","110214");
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8Bhzx/GAG0+0qug7gYbkIPAnKT074+9OvYNsOCRNXgY5mWQNayxBqczBSWxm3dBJGGsYp16e74xuwsd5J8OyQ5lKlsmqtBlNU2zVD1s5udidur6TrCycqSwJBdv9VGvrBGQ4gWe/ADiaAoU/dbyQt0L/niVNuDgdMxybT88JpYQIDAQAB";
        String res = signPublic(bizJson.toString(),publicKey);

        JSONObject bodyJson = new JSONObject();
        bodyJson.put("partnerId","tongxing");
        bodyJson.put("apiVersion","1.0.1");
        bodyJson.put("bizData",res);
        HashMap<String,Object> priceParam = new HashMap<>();



      /*  priceParam.put("sku","110375");
        priceParam.put("policy_age","110526");
        priceParam.put("policy_sex","110551");
        priceParam.put("policy_pay_period","111765");
        priceParam.put("goodsId","110214");
        priceParam.put("buy_account","110704");

        */


        priceParam.put("goodsId", 1110255);
        priceParam.put("sku", 110375);
        priceParam.put("policy_sex",110551);
        priceParam.put("policy_age",110526);
        //priceParam.put("fromTime", "2017年06月09日");
        //priceParam.put("policy_period", 100073);
        priceParam.put("buy_account", 110704);
        priceParam.put("policy_pay_period", 111765);

    /*    priceParam.put("goodsId", 1110215);
        priceParam.put("sku", 1110460);
        priceParam.put("policy_age","1944年12月05日");
        priceParam.put("fromTime", "2017年06月09日");
        priceParam.put("policy_period", 100005);
        priceParam.put("policy_pay_period", 111020);*/



        Map<String, Object> bizData = new HashMap<>();
        bizData.put("priceParam", priceParam);
        bizData.put("apiName", "calculatePrice");

        bodyJson.put("bizData",signPublic(JSON.toJSONString(bizData), publicKey));
        String body = bodyJson.toString();
        StringEntity requestEntity = new StringEntity(body, "UTF-8");
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

    public static String bzSign(String key, Map<String, Object> map) {
        String rstr = "";
        try {
            ArrayList<String> list = new ArrayList<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (!StringUtils.isEmpty(entry.getValue().toString()) && !"null".equals(entry.getValue())) {
                    list.add(entry.getKey() + "=" + entry.getValue() + "&");
                }
            }
            int size = list.size();
            String[] arrayToSort = list.toArray(new String[size]);
            Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < size; i++) {
                sb.append(arrayToSort[i]);
            }
            String result = sb.toString();
            result += "key="+ key;
            //System.out.println("加密串 result=" + result);
            rstr = DigestUtils.md5Hex((result).getBytes("UTF-8")).toUpperCase();
            rstr = DigestUtils.md5Hex((rstr).getBytes("UTF-8")).toUpperCase();
        } catch (Exception e) {
            System.out.println("bzSign:" + e);
        }
        return rstr;
    }
}
