package com.example.user.kvs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private Button btn0;    //시작 버튼
    private Button btn6;    //사용자 설정 버튼
    private Button btn3;    //끝내기 버튼
    private ImageView img;  //이미지 뷰
    private static final int PERMISSIONS_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn0 = findViewById(R.id.button);
        btn3 = findViewById(R.id.button3);
        btn6 = findViewById(R.id.button6);
        img = findViewById(R.id.imageView);

        requestPermission();
        //시작 버튼에 대한 리스너
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
        //사용자 설정버튼에 대한 리스너
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent2);
            }
        });

        //끝내기 버튼에 대한 리스너
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }

        });

    }

    private final String[] needPermissions = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};

    private void requestPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, needPermissions[0])
                        && ActivityCompat.shouldShowRequestPermissionRationale(this, needPermissions[1])
                        && ActivityCompat.shouldShowRequestPermissionRationale(this, needPermissions[2])) {
                    Snackbar.make(findViewById(R.id.activity_main), "앱을 실행하기 위해선 권한이 필요합니다..",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this, needPermissions,
                                    PERMISSIONS_REQUEST_CODE);
                        }
                    }).show();
                } else {
                    ActivityCompat.requestPermissions(this, needPermissions,
                            PERMISSIONS_REQUEST_CODE);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == needPermissions.length) {
            boolean permissionResult = true;
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permissionResult = false;
                    break;
                }
            }
            if (permissionResult) {
                KVSTelephone.getInstance(this.getApplicationContext());
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, needPermissions[0])
                        && ActivityCompat.shouldShowRequestPermissionRationale(this, needPermissions[1])
                        && ActivityCompat.shouldShowRequestPermissionRationale(this, needPermissions[2])) {
                    Snackbar.make(findViewById(R.id.activity_main), "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();
                } else {
                    Snackbar.make(findViewById(R.id.activity_main), "설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();
                }
            }
        }
    }
}
