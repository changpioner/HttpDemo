package com.nam;

import com.util.Base64Util;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by Namhwik on 2017/11/12.
 */
public class entrance {
    private static final int MAX_ENCRYPT_BLOCK = 117;
    public static final String KEY_ALGORITHM = "RSA";

    public static void main(String[] args) throws Exception {
        String url = "https://tapi.bz365.com/api/smgw.do";
        // 电子保单邮件
        JSONObject bizJson = new JSONObject();
        //bizJson.put("partnerUserId","tongxing");
        bizJson.put("osType","ios");
        bizJson.put("apiVersion","1.0.1");
        bizJson.put("busiType","openapi");
        bizJson.put("apiName","goodsDesc");
        bizJson.put("goodsId","1110227");
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8Bhzx/GAG0+0qug7gYbkIPAnKT074+9OvYNsOCRNXgY5mWQNayxBqczBSWxm3dBJGGsYp16e74xuwsd5J8OyQ5lKlsmqtBlNU2zVD1s5udidur6TrCycqSwJBdv9VGvrBGQ4gWe/ADiaAoU/dbyQt0L/niVNuDgdMxybT88JpYQIDAQAB";
        byte[] bytes = encryptByPublicKey(Base64Util.decodeString(bizJson.toString()),publicKey);
        String res = bcd2Str(bytes);

        System.out.println(res);
        String res2 = signPublic(bizJson.toString(),publicKey);
        System.out.println(res2);

        JSONObject bodyJson = new JSONObject();
        bodyJson.put("partnerId","tongxing");
        //bodyJson.put("channel","tongxing");
        bodyJson.put("apiVersion","1.0.1");
        bodyJson.put("bizData",res2);
        String body = bodyJson.toString();
        StringEntity requestEntity = new StringEntity(body, "UTF-8");
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-type", "application/json");
        post.setHeader("Connection", "close");
        post.setEntity(requestEntity);
        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity);
        System.out.println(result);

    }


    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64Util.decodeString(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 模长
        int key_len = publicKey.getModulus().bitLength() / 8;
        // 加密数据长度 <= 模长-11
        String[] datas = splitString(data, key_len - 11);
        StringBuilder mi = new StringBuilder();
        //如果明文长度大于模长-11则要分组加密
        for (String s : datas) {
            mi.append(bcd2Str(cipher.doFinal(s.getBytes())));
        }
        return mi.toString();
    }

    public static String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i=0; i<x+z; i++) {
            if (i==x+z-1 && y!=0) {
                str = string.substring(i*len, i*len+y);
            }else{
                str = string.substring(i*len, i*len+len);
            }
            strings[i] = str;
        }
        return strings;
    }


    /**
     * BCD转字符串
     */
    public static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    public static String signPublic(String content, String publicKey) {
        try {

            //ssad
            System.out.println("content = " + content);
            //对公钥解
            byte[] keyBytes = org.apache.commons.codec.binary.Base64.decodeBase64(publicKey);
            //取公钥
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(x509EncodedKeySpec);
            //对数据解密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);

            InputStream ins = new ByteArrayInputStream(content.getBytes("UTF-8"));
            ByteArrayOutputStream writer = new ByteArrayOutputStream();
            byte[] buf = new byte[117];
            int bufl;

            while ((bufl = ins.read(buf)) != -1) {
                byte[] block = null;

                if (buf.length == bufl) {
                    block = buf;
                } else {
                    block = new byte[bufl];
                    System.arraycopy(buf, 0, block, 0, bufl);
                }

                writer.write(cipher.doFinal(block));
            }
            return Base64.encodeBase64String(writer.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
