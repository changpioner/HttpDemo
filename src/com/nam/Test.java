package com.nam;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Namhwik on 2017/11/18.
 */
public class Test {
    public static void main(String[] args) throws JSONException, IOException {

         TestApi.goodsProps("110214");
        JSONObject res = TestApi.goodsPolicyProps("110214");
        System.out.println(res.toString());
         TestApi.orderCreate();

    }
}
