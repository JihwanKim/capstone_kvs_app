package com.example.user.kvs;
//사용자설정에 관한 액티비티

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SettingActivity extends AppCompatActivity {
    private Button settingOkButton;
    private EditText phoneNumberText;
    private EditText msgText;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        settingOkButton = findViewById(R.id.button);
        phoneNumberText = findViewById(R.id.request_target_phone_number_text);
        msgText = findViewById(R.id.message);

        settingOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSavePhoneAndMsg();

            }
        });
        handler = new Handler(Looper.getMainLooper());
        getSavedInfo();
    }

    private void requestSavePhoneAndMsg() {
        if (!(android.util.Patterns.PHONE.matcher(phoneNumberText.getText().toString()).matches() && phoneNumberText.getText().toString().length() >= 10 && phoneNumberText.getText().toString().length() <= 11)) {
            Toast.makeText(this.getApplicationContext(), "정확한 핸드폰 번호를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestBody requestBody = new FormBody.Builder()
                .add("phone_number", phoneNumberText.getText().toString())
                .add("msg", msgText.getText().toString())
                .build();

        Request request = new Request.Builder()
                .url("http://kvs.j-confiance.io/setting/" + new KVSTelephone(this.getApplicationContext()).getPhoneNumber())
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) {
                Logger.d(response.message());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "저장하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void getSavedInfo() {
        Request request = new Request.Builder()
                .url("http://kvs.j-confiance.io/setting/" + new KVSTelephone(this.getApplicationContext()).getPhoneNumber())
                .get()
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                try {
                    JSONObject body = new JSONObject(responseBody).getJSONObject("body");
                    phoneNumberText.setText(body.get("request_phone_number").toString());
                    msgText.setText(body.get("msg").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
