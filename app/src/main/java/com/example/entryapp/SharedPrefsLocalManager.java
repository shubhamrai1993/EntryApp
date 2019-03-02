package com.example.entryapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class SharedPrefsLocalManager {
    public static void storeEntryRequestId(Context context, String entryRequestId) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("ENTRY_REQUEST_ID", entryRequestId);
        editor.apply();
    }

    public static void storeUserId(Context context, String userId) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("USER_ID", userId);
        editor.apply();
    }
}
