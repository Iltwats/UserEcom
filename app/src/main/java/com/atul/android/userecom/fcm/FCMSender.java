package com.atul.android.userecom.fcm;

import com.atul.android.userecom.R;
import com.atul.android.userecom.constants.Keys;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class FCMSender {

    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String KEY_STRING = Keys.FCMKEY;
    OkHttpClient client = new OkHttpClient();

    public void send(String message, Callback callback){
        RequestBody reqBody = RequestBody.create(message, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(FCM_URL)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", KEY_STRING)
                .post(reqBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

}