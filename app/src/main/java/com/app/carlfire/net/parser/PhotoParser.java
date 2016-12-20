package com.app.carlfire.net.parser;

import android.util.Log;

import com.app.carlfire.beans.NewsBean;
import com.app.carlfire.beans.PhotoGirl;
import com.app.commonlib.net.parser.RespParser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carl on 2016-12-09 009.
 */

public class PhotoParser implements RespParser<List<PhotoGirl>> {

    @Override
    public List<PhotoGirl> parseResponse(String result) throws JSONException {
        List<PhotoGirl> beans = new ArrayList<>();
        try{
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(result).getAsJsonObject();
            JsonElement jsonElement = jsonObject.get("results");
            if(jsonElement == null){
                return null;
            }
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                PhotoGirl photo = new Gson().fromJson(jo,PhotoGirl.class);
                beans.add(photo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return beans;
    }
}
