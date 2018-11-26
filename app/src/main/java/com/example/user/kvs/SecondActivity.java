package com.example.user.kvs;
//시작버튼 시 열리는 새로운 액티비티에 대한 자바소스
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SecondActivity extends Activity {
    Button btn4;        //시작버튼의 새로운 액티비티에 있는 돌아가기 버튼
    Button btn11;       //실시간 모니터링 시작 버튼
    Button btn12;
    int tog = 0;
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.second_main);
        btn4 = (Button)findViewById(R.id.button4);
        btn11 = (Button)findViewById(R.id.button11);
        btn12 = (Button)findViewById(R.id.button12);

        btn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SecondActivity.this, "실시간 모니터링을 시작합니다.", Toast.LENGTH_SHORT).show();
                tog = 1;
            }
        });

        btn12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SecondActivity.this, "실시간 모니터링을 종료합니다.", Toast.LENGTH_SHORT).show();
                tog = 0;
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tog == 1){   //실시간 모니터링중에 다른 액티비티로 결과값을 내보낼 때는 명시적 인텐트를 사용한다.
                                //명화 예제와 안드로이드 책 P401~ 확인
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    Toast.makeText(SecondActivity.this, "실시간 모니터링중입니다.", Toast.LENGTH_SHORT).show();
                    startActivity(intent);

                }
                else{
                    finish();
                    Toast.makeText(SecondActivity.this, "돌아가기 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
