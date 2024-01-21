package com.zhangyao.suburb_selector_mobile_application.utils;

import android.content.Context;
import android.widget.Toast;

public class UIutils {
    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
