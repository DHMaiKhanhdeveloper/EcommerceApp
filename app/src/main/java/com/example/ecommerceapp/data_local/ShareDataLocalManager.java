package com.example.ecommerceapp.data_local;

import android.content.Context;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShareDataLocalManager {

    private static final String FIRST_INSTALL = " FIRST_INSTALL";
    private static final String FIRST_STRING_HASH = "FIRST_STRING_HASH";
    private static final String FIRST_OBJECT_USER = "FIRST_OBJECT_USER";
    private static final String FIRST_OBJECT_LIST_USER = "FIRST_OBJECT_LIST_USER";
    private  SharePreferences sharePreferences;
    private static ShareDataLocalManager shareDataLocalManager;

    public static  void init(Context mContext){
        shareDataLocalManager = new ShareDataLocalManager();
        shareDataLocalManager.sharePreferences = new SharePreferences(mContext);
    }
    public ShareDataLocalManager getShareDataLocalManager(){
        if(shareDataLocalManager ==null) {
            return shareDataLocalManager = new ShareDataLocalManager();
        }
      return shareDataLocalManager;
    }
    public static void  putFirstInstall(boolean value){
        shareDataLocalManager.getShareDataLocalManager().sharePreferences.putBoolean(FIRST_INSTALL,value);
    }
    public static boolean getFirstInstall(){
        return shareDataLocalManager.getShareDataLocalManager().sharePreferences.getBoolean(FIRST_INSTALL);
    }

    public static void  putSetHash(Set<String> value){
        shareDataLocalManager.getShareDataLocalManager().sharePreferences.putStringSet(FIRST_STRING_HASH,value);
    }
    public static Set<String> getSetHash(){
        return shareDataLocalManager.getShareDataLocalManager().sharePreferences.getStringSet(FIRST_STRING_HASH);
    }




}
