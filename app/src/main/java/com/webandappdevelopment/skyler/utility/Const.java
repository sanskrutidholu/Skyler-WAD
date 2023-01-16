package com.webandappdevelopment.skyler.utility;

import android.content.Context;
import android.widget.Toast;

public class Const {

    static void callToast(Context ctx, String msg){
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }
}
