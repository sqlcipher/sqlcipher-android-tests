package net.zetetic;

import android.app.Application;

public class ZeteticApplication extends Application {

    private static ZeteticApplication instance;

    public ZeteticApplication(){
        instance = this;
    }

    public static ZeteticApplication getInstance(){
        return instance;
    }
}
