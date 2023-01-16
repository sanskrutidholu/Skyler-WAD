package com.webandappdevelopment.skyler.utility;

import android.content.Context;
import android.widget.Toast;

public class CallToast {

    public CallToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
