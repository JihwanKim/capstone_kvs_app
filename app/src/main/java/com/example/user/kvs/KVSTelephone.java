package com.example.user.kvs;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

public class KVSTelephone {
    private static KVSTelephone INSTANCE;
    private final String phoneNumber;

    public KVSTelephone(Context context) {
        TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            phoneNumber = "01065891249";
            Logger.d("READ_SMS" + String.valueOf(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED));
            Logger.d("READ_PHONE_NUMBERS" + String.valueOf(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED));
            Logger.d("READ_PHONE_STATE" + String.valueOf(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED));
            return;
        }
        phoneNumber = tMgr.getLine1Number();
    }

    public static KVSTelephone getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new KVSTelephone(context);
        }
        return INSTANCE;
    }

    public static KVSTelephone getInstance() {
        return INSTANCE;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
