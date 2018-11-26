package com.example.user.kvs;
//사용방법버튼 시 열리는 새로운 액티비티에 대한 자바소스
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class ThirdActivity extends Activity {
    Button btn5;    //사용방법버튼의 새로운 액티비티에 있는 돌아가기(이전화면) 버튼
    Button btn8;    //이전 화면 버튼
    Button btn9;    //다음 화면 버튼
    Button btn10;   ////사용방법버튼의 새로운 액티비티에 있는 돌아가기(다음화면) 버튼
    ViewFlipper vFlipper; //뷰플리퍼
    // 변경

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.third_main);
        btn5 = (Button)findViewById(R.id.button5);
        btn8 = (Button)findViewById(R.id.button8);
        btn9 = (Button)findViewById(R.id.button9);
        btn10 = (Button)findViewById(R.id.button10);
        vFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(ThirdActivity.this, "돌아가기 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vFlipper.showPrevious();
            }
        });

        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vFlipper.showNext();
            }
        });

        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(ThirdActivity.this, "돌아가기 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
