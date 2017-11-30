package com.nam;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.nam.entrance.signPublic;

/**
 * Created by Namhwik on 2017/11/18.
 */
public class TestApi {
    private static Map<String, Object> bodyMap = new HashMap<>();
    private static Map<String, Object> bizData = new HashMap<>();
    private static JSONObject bizJSON = new JSONObject();
    private static JSONObject bodyJSON = new JSONObject();

    private static String apiUrl = "https://tapi.bz365.com/api/smgw.do";
    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8Bhzx/GAG0+0qug7gYbkIPAnKT074+9OvYNsOCRNXgY5mWQNayxBqczBSWxm3dBJGGsYp16e74xuwsd5J8OyQ5lKlsmqtBlNU2zVD1s5udidur6TrCycqSwJBdv9VGvrBGQ4gWe/ADiaAoU/dbyQt0L/niVNuDgdMxybT88JpYQIDAQAB";


    public static void goodsDesc(String goodsId) throws JSONException {
        System.out.println("获取商品详情");
        JSONObject bizData =new JSONObject();
        bizData.put("apiName", "goodsDesc");

        bizData.put("goodsId", goodsId);
        bodyMap.put("bizData", signPublic(bizData.toString(), publicKey));
        bodyMap.put("partnerId","tongxing");
        bodyMap.put("apiVersion","1.0.1");
        try {
            String r = postJson(apiUrl, JSON.toJSONString(bodyMap));
            System.out.println("r=" + r);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static String goodsProps(String goodsId) throws JSONException {
        System.out.println("获取商品价格参数");
        String r = "";

        JSONObject bizData = new JSONObject();
        JSONObject bodyMap = new JSONObject();
        bizData.put("apiName", "goodsProps");
        bizData.put("goodsId", goodsId);
        bizData.put("partnerId","tongxing");

        bodyMap.put("bizData", signPublic(bizData.toString(), publicKey));
        bodyMap.put("partnerId","tongxing");

        bodyMap.put("apiVersion","1.0.1");
        try {
            r = postJson(apiUrl, bodyMap.toString());
            System.out.println("r=" + r);
        } catch (Exception e) {
            System.out.println(e);
        }
        return r;

    }
        public static JSONObject goodsPolicyProps(String goodsId) throws IOException, JSONException {
            JSONObject bizJSON = new JSONObject();
            JSONObject bodyJSON = new JSONObject();
            bizJSON.put("apiName", "goodsPolicyProps");
            bizJSON.put("partnerUserId", "trest");
            bizJSON.put("apiVersion","1.0.1");
            bizJSON.put("goodsId", goodsId);
            bodyJSON.put("bizData", signPublic(bizJSON.toString(), publicKey));
            bodyJSON.put("partnerId","tongxing");
            String res = postJson(apiUrl, bodyJSON.toString());
            JSONObject data = new JSONObject(res).getJSONObject("data");
            return data;
        }


    public static void orderCreate() throws IOException, JSONException {
        System.out.println("下单测试");
        long b = System.currentTimeMillis();
        bizData.put("apiName", "syncOrder");
        bizData.put("partnerUserId", "trest");
        bizData.put("apiVersion","1.0.3");


        Map<String, Object> orderInfo = new HashMap<>();
        orderInfo.put("timeStamp", System.currentTimeMillis());
        //投保人信息
        orderInfo.put("tname", "翟特");
        orderInfo.put("tidCard", "370784199306106810");
        orderInfo.put("tmobile", "17611055275");
        //商品信息
        orderInfo.put("goodsId", 110214);
        orderInfo.put("skuId", 110377);
        //priceKey和priceParam两个参数传其一
        orderInfo.put("priceKey", "110377_110551_110500_111767");
        Map<String, Object> priceParam = new HashMap<>();
        priceParam.put("policy_pay_period", 111767);//保障期限
        priceParam.put("policy_sex", 110551);//性别
        priceParam.put("policy_age", 110500);//性别
        priceParam.put("sku", 110377);//性别
        //orderInfo.put("priceParam", priceParam);

        orderInfo.put("fromTime", "20171201000000");
        orderInfo.put("toTime", "2037113035959");
        orderInfo.put("orderType", "1");



        //被保人信息――不同一人
        orderInfo.put("idCard", "370784199306106810");
        orderInfo.put("name", "翟特");
        orderInfo.put("birthday", "19930610");
        //保单动态属性（按商品要求填写）
        List<Map<String, Object>> policyProps = new ArrayList<>();
        Map<String, Object> policy_region = new HashMap<>();
        policy_region.put("propertyName", "policy_region");
        policy_region.put("propertyValue", "policy_region");
        policy_region.put("tagName", "所在地区");
        policyProps.add(policy_region);

        Map<String, Object> home_address = new HashMap<>();
        home_address.put("propertyName", "home_address");
        home_address.put("propertyValue", "河北省三河市皇庄镇四村100号");
        home_address.put("tagName", "home_address");
        policyProps.add(home_address);

        Map<String, Object> policy_bank_card = new HashMap<>();
        policy_bank_card.put("propertyName", "policy_bank_card");
        policy_bank_card.put("propertyValue", "6214830150286572");
        policy_bank_card.put("tagName", "policy_bank_card");
        policyProps.add(policy_bank_card);

        Map<String, Object> policy_bank = new HashMap<>();
        policy_bank.put("propertyName", "policy_bank");
        policy_bank.put("propertyValue", "招商银行");
        policy_bank.put("tagName", "policy_bank");
        policyProps.add(policy_bank);
        Map<String, Object> policy_bankId = new HashMap<>();
        policy_bankId.put("propertyName", "policy_bankId");
        policy_bankId.put("propertyValue", "1009291105");
        policy_bankId.put("tagName", "policy_bankId");
        policyProps.add(policy_bankId);
        orderInfo.put("policyProps", policyProps);


        List<Map<String, Object>> goodsProps = new ArrayList<>();
        Map<String, Object> buy_account = new HashMap<>();
        buy_account.put("propertyName", "buy_account");
        buy_account.put("propertyValue", "2");
        buy_account.put("tagName", "购买份数");
        goodsProps.add(buy_account);
        orderInfo.put("goodsProps", goodsProps);

        bizData.put("orderInfo", orderInfo);
        bodyMap.put("bizData", signPublic(JSON.toJSONString(bizData), publicKey));
        bodyMap.put("partnerId","tongxing");
        bodyMap.put("apiVersion","1.0.3");
        String r = postJson(apiUrl, JSON.toJSONString(bodyMap));
        System.out.println("r=" + r);
    }


    public static String createOrder(Map<String,Object> paramMap) throws JSONException {
        System.out.println("生成大象保单");
        String resJSON = "";
        String url = "https://tapi.bz365.com/api/smgw.do";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8Bhzx/GAG0+0qug7gYbkIPAnKT074+9OvYNsOCRNXgY5mWQNayxBqczBSWxm3dBJGGsYp16e74xuwsd5J8OyQ5lKlsmqtBlNU2zVD1s5udidur6TrCycqSwJBdv9VGvrBGQ4gWe/ADiaAoU/dbyQt0L/niVNuDgdMxybT88JpYQIDAQAB";
        JSONObject bizData = new JSONObject();
        JSONObject bodyJson = new JSONObject();
        Map<String, Object> orderInfo = new HashMap<>();

        orderInfo.put("timeStamp",System.currentTimeMillis());
        orderInfo.put("tname", "崔利娟");
        orderInfo.put("tidCard", "230903196706070827");
        orderInfo.put("tmobile", "13426129936");
        orderInfo.put("name", "刘学明");
        orderInfo.put("idCard", "410202195101121016");
        orderInfo.put("mobile", "13426129936");
        orderInfo.put("fromTime", "20171130000000");
        orderInfo.put("toTime", "20271130000000");
        //商品信息
        orderInfo.put("goodsId", 110214);
        orderInfo.put("skuId", 110375);
        bizData.put("priceParam", paramMap);
        bizData.put("apiName", "syncOrder");
        bizData.put("partnerUserId","tongxing");

        List<Map<String, Object>> policyProps = new ArrayList<>();
        //投保人
        Map<String, Object> pp22 = new HashMap<>();
        pp22.put("propertyName", "policy_email");
        pp22.put("propertyValue", "namhwik@sina.com");
        pp22.put("tagName", "电子邮箱");
        pp22.put("propertyOwner", "2");
        policyProps.add(pp22);
        //被保人
        Map<String, Object> pp21 = new HashMap<>();
        pp21.put("propertyName", "policy_email");
        pp21.put("propertyValue", "namhwik@sina.com");
        pp21.put("tagName", "电子邮箱");
        pp21.put("propertyOwner", "1");
        policyProps.add(pp21);

        //orderInfo.put("policyProps", policyProps);
        bizData.put("orderInfo", orderInfo);
        System.out.println("bizData++++++++++"+bizData.toString());
        bodyJson.put("bizData", signPublic(bizData.toString(), publicKey));
        bodyJson.put("partnerId","tongxing");

        bodyJson.put("apiVersion","1.0.1");

        try {
            resJSON = new JSONObject(postJson(url, bodyJson.toString())).toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return resJSON;
    }
    public static void paytoolchoose() throws JSONException {
        System.out.println("获取三方支付参数");
        bizData.put("apiName", "paytoolchoose");

        bizData.put("timeStamp", System.currentTimeMillis());
        bizData.put("orderId", "201703091355560013");
        bizData.put("payWay", "0");
        bizData.put("returnUrl", "http://m.bz365.com/mobile/");
        bodyMap.put("bizData", signPublic(JSON.toJSONString(bizData), publicKey));
        bodyMap.put("partnerId","tongxing");
        bodyMap.put("apiVersion","1.0.1");
        try {
            String r = postJson(apiUrl, JSON.toJSONString(bodyMap));
            System.out.println("r=" + r);
            JSONObject json = new JSONObject(r);
            JSONObject data =json.getJSONObject("data");
            String payUrl = data.getString("payUrl");
            System.out.println(payUrl);
            System.out.println(URLDecoder.decode(payUrl));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static String goodsPricePram(String goodsId) throws JSONException {
        String r = "";
        System.out.println("获取商品价格");

        Map<String, Object> priceParam = new HashMap<>();
        //goodsId = 1110277
        priceParam.put("goodsId", goodsId);
        priceParam.put("sku", 1110447);
        priceParam.put("policy_age","1984年12月05日");
        priceParam.put("fromTime","2017年09月05日");

        priceParam.put("policy_period", 100005);
//{"apiName":"calculatePrice",
// "priceParam":{"fromTime":"2017年11月22日","policy_age":"1998年02月09日","policy_period":"100005","sku":"1110447","goodsId":"1110209"}}

        bizData.put("priceParam", priceParam);
        bizData.put("apiName", "calculatePrice");

        bodyMap.put("bizData", signPublic(JSON.toJSONString(bizData), publicKey));
        bodyMap.put("partnerId","tongxing");
        bodyMap.put("apiVersion","1.0.1");
        try {
             r = postJson(apiUrl, JSON.toJSONString(bodyMap));

        } catch (Exception e) {
            System.out.println(e);
        }
        return r;
    }

    private static String postJson(String apiUrl,String body) throws IOException, JSONException {
        StringEntity requestEntity = new StringEntity(body, "UTF-8");
        HttpPost post = new HttpPost(apiUrl);
        post.setHeader("Content-type", "application/json");
        post.setHeader("Connection", "close");
        post.setEntity(requestEntity);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(post);
        String result = EntityUtils.toString(response.getEntity());
        JSONObject resultJson = new JSONObject(result);
        httpclient.close();
        return resultJson.toString();
    }





}
