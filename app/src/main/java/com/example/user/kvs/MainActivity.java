package com.example.user.kvs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    Button btn0;    //시작 버튼
    Button btn6;    //사용자 설정 버튼
    Button btn2;    //기록 버튼
    Button btn3;    //끝내기 버튼
    ImageView img;  //이미지 뷰


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn0 = (Button)findViewById(R.id.button);
        btn2 = (Button)findViewById(R.id.button2);
        btn3 = (Button)findViewById(R.id.button3);
        btn6 = (Button)findViewById(R.id.button6);
        img = (ImageView)findViewById(R.id.imageView);

        //시작 버튼에 대한 리스너
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
        //기록 버튼에 대한 리스너
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, FourthActivity.class);
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
                //Toast.makeText(MainActivity.this, "종료합니다.", Toast.LENGTH_SHORT).show();
            }

        });

    }
}
