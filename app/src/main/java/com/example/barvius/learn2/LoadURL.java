package com.example.barvius.learn2;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.barvius.learn2.HTTP.LoadThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoadURL {
    private static LoadURL instance;
    LoadThread lt;

    private LoadURL() {
        lt = new LoadThread();
    }

    public static LoadURL getInstance(){
        if(instance == null){
            instance = new LoadURL();
        }
        return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public DBItems getRandomItems() {
        JSONObject tmp = null;
        try {
            tmp = new JSONObject(lt.GET("http://localhost/android-rest/",
                    new String[] { "getRandomItems", ""}));
            return new DBItems(tmp.getInt("id"),tmp.getString("ru"),tmp.getString("en"));
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        return new DBItems();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public List<DBItems> getRandomSet(long setSize, long masterId) {
        List<DBItems> items = new ArrayList<>();
        try {
            JSONArray tmp = new JSONArray(lt.GET("http://localhost/android-rest/",
                    new String[] { "getRandomSet", ""}, new String[]{"masterId",Long.toString(masterId)},
                    new String[]{"setSize",Long.toString(setSize)}));
            for (int i = 0; i < tmp.length(); i++) {
                JSONObject obj = tmp.getJSONObject(i);
                items.add(new DBItems(obj.getInt("id"),obj.getString("ru"),obj.getString("en")));
            }
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        return items;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean moveToArchive(DBItems items) {
        try {
            return new JSONObject(lt.GET("http://localhost/android-rest/",
                    new String[] { "moveToArchive", ""}, new String[] { "dictionaryId", Long.toString(items.getId())})).getBoolean("dictionaryIsAvailable");
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean addItems(DBItems items) {
        lt.POST("http://localhost/android-rest/?addDictionaryItems",
                new String[] { "ru", items.getRu()}, new String[] { "en", items.getEn()});
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean truncateArchive() {
        try {
            return new JSONObject(lt.GET("http://localhost/android-rest/",
                    new String[] { "truncateArchive", ""})).getBoolean("op");
        } catch (JSONException | NullPointerException e) {
            return false;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean dictionaryIsAvailable() {
        try {
            return new JSONObject(lt.GET("http://localhost/android-rest/",
                            new String[] { "dictionaryIsAvailable", ""})).getBoolean("dictionaryIsAvailable");
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean testIsAvailable() {
        try {
            return new JSONObject(lt.GET("http://localhost/android-rest/",
                            new String[] { "testIsAvailable", ""})).getBoolean("testIsAvailable");
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        return false;
    }
}
