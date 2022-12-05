package com.example.ecommerceapp.application;

import com.example.ecommerceapp.data_local.ShareDataLocalManager;


public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ShareDataLocalManager.init(getApplicationContext());
    }
}
