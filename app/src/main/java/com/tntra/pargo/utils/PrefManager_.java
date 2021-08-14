package com.tntra.pargo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.tntra.pargo.Constants;

public class PrefManager_ {
    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }
}
