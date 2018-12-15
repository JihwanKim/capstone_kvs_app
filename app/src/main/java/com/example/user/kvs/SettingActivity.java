package com.example.user.kvs;
//사용자설정에 관한 액티비티
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        settingOkButton = findViewById(R.id.button);
        phoneNumberText = findViewById(R.id.editText1);

        settingOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSend();

            }
        });


    }
    private void goSend(){
        String userPhoneNumber = KVSTelephone.getInstance().getPhoneNumber();
        String targetPhoneNumber = phoneNumberText.getText().toString();
        RequestBody requestBody = new FormBody.Builder()
                .add("phone_number", targetPhoneNumber)
                .add("msg", "으악! 살려줘!")
                .build();

        Request request = new Request.Builder()
                .url("http://kvs.j-confiance.io/activity_setting/" + userPhoneNumber)
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Logger.d(response.message());
            }
        });
    }
}
