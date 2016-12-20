package com.app.carlfire.net.parser;

import android.util.Log;

import com.app.carlfire.beans.NewsBean;
import com.app.commonlib.net.parser.RespParser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class NewsParser implements RespParser<List<NewsBean>> {
    private  String type = "";

    public  NewsParser(String type) {
        this.type = type;
    }

    @Override
    public List<NewsBean> parseResponse(String result)  {
        List<NewsBean> beans = new ArrayList<NewsBean>();
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonObj = parser.parse(result).getAsJsonObject();
            JsonElement jsonElement = jsonObj.get(type);
            if(jsonElement == null) {
                return null;
            }
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                if (jo.has("skipType") && "special".equals(jo.get("skipType").getAsString())) {
                    continue;
                }
                if (jo.has("TAGS") && !jo.has("TAG")) {
                    continue;
                }

                if (!jo.has("imgextra")) {
                    NewsBean news = new Gson().fromJson(jo,NewsBean.class);
                    beans.add(news);
                }
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        return beans;
    }

}
