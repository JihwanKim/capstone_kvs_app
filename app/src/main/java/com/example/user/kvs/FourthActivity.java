package com.example.user.kvs;
//기록 시 열리는 새로운 액티비티에 대한 자바소스
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FourthActivity extends AppCompatActivity{
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(new MyGraphicView(this));
    }
    private static class MyGraphicView extends View {   //운전자의 상태기록 그래픽뷰
        public MyGraphicView(Context context) {
            super(context);
        }

        protected void onDraw(Canvas canvas){
            super.onDraw(canvas);
            Paint paint = new Paint();			//paint 객체변수 저장
            paint.setAntiAlias(true);

            paint.setColor(Color.BLACK);
            paint.setTextSize(20);                              //글짜 표기
            canvas.drawText("운전자의 상태 기록", 10, 30, paint);	//표시명, 시작점의 xy좌표

            paint.setColor(Color.GREEN);                //paint객체의 색상 초록색 지정
            paint.setTextSize(10);                              //글짜 표기
            canvas.drawText("100%", 455, 40, paint);
            canvas.drawLine(0, 50, 480, 50, paint);	//100%표시선 그리기(시작점의 xy좌표, 끝점의 xy좌표)             //글짜 표기
            canvas.drawText("75%", 460, 190, paint);
            canvas.drawLine(0, 200, 480, 200, paint);	//75%표시선 그리기(시작점의 xy좌표, 끝점의 xy좌표)             //글짜 표기
            canvas.drawText("50%", 460, 340, paint);
            canvas.drawLine(0, 350, 480, 350, paint);	//50%표시선 그리기(시작점의 xy좌표, 끝점의 xy좌표)
            canvas.drawText("25%", 460, 490, paint);
            canvas.drawLine(0, 500, 480, 500, paint);	//25%표시선 그리기(시작점의 xy좌표, 끝점의 xy좌표)

            paint.setColor(Color.BLACK);
            canvas.drawLine(0, 650, 480, 650, paint);	//0%표시선 그리기(시작점의 xy좌표, 끝점의 xy좌표)

            paint.setStyle(Paint.Style.FILL);		//내부를 색상 채워라(FILL)

            //&&&&&100% 75% 50% 25% 0%-> top = 50, 200, 350, 500, 650으로 설정하면됨.&&&&&&

            paint.setColor(Color.RED);			//paint객체의 색상 다시 빨간색으로 지정
            Rect rect1 = new Rect(15, 50, 15+30, 250+400); //첫번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect1, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(15);                              //글짜 표기
            canvas.drawText("man", 17, 680, paint);	//표시명, 시작점의 xy좌표

            paint.setColor(Color.RED);
            Rect rect2 = new Rect(75, 350, 75+30, 250+400); //두번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect2, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(15);                              //글짜 표기
            canvas.drawText("smiling", 67, 680, paint);	//표시명, 시작점의 xy좌표

            paint.setColor(Color.RED);
            Rect rect3 = new Rect(135, 200, 135+30, 250+400); //세번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect3, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(15);                              //글짜 표기
            canvas.drawText("eye", 140, 680, paint);	//표시명, 시작점의 xy좌표
            canvas.drawText("open", 135, 700, paint);	//표시명, 시작점의 xy좌표

            paint.setColor(Color.RED);
            Rect rect4 = new Rect(195, 50, 195+30, 250+400); //네번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect4, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(15);                              //글짜 표기
            canvas.drawText("age", 200, 680, paint);	//표시명, 시작점의 xy좌표

            paint.setColor(Color.RED);
            Rect rect5 = new Rect(255, 100, 255+30, 250+400); //다섯번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect5, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(15);                              //글짜 표기
            canvas.drawText("xxxx", 255, 680, paint);	//표시명, 시작점의 xy좌표

            paint.setColor(Color.RED);
            Rect rect6 = new Rect(315, 225, 315+30, 250+400); //여섯번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect6, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(15);                              //글짜 표기
            canvas.drawText("xxxx", 315, 680, paint);	//표시명, 시작점의 xy좌표

            paint.setColor(Color.RED);
            Rect rect7 = new Rect(375, 275, 375+30, 250+400); //여섯번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect7, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(15);                              //글짜 표기
            canvas.drawText("xxxx", 375, 680, paint);	//표시명, 시작점의 xy좌표




        }
    }

    private static class GraphicView extends View {     //표정분석 결과값 그래픽 뷰
        public GraphicView(Context context) {
            super(context);
        }

        protected void onDraw(Canvas canvas){
            super.onDraw(canvas);
            Paint paint = new Paint();			//paint 객체변수 저장
            paint.setAntiAlias(true);

            paint.setColor(Color.BLACK);
            paint.setTextSize(20);                              //글짜 표기
            canvas.drawText("표정 분석 결과 기록", 10, 30, paint);	//표시명, 시작점의 xy좌표

            paint.setColor(Color.GREEN);                //paint객체의 색상 초록색 지정
            paint.setTextSize(10);                              //글짜 표기
            canvas.drawText("100%", 455, 40, paint);
            canvas.drawLine(0, 50, 480, 50, paint);	//100%표시선 그리기(시작점의 xy좌표, 끝점의 xy좌표)             //글짜 표기
            canvas.drawText("75%", 460, 190, paint);
            canvas.drawLine(0, 200, 480, 200, paint);	//75%표시선 그리기(시작점의 xy좌표, 끝점의 xy좌표)             //글짜 표기
            canvas.drawText("50%", 460, 340, paint);
            canvas.drawLine(0, 350, 480, 350, paint);	//50%표시선 그리기(시작점의 xy좌표, 끝점의 xy좌표)
            canvas.drawText("25%", 460, 490, paint);
            canvas.drawLine(0, 500, 480, 500, paint);	//25%표시선 그리기(시작점의 xy좌표, 끝점의 xy좌표)

            paint.setColor(Color.BLACK);
            canvas.drawLine(0, 650, 480, 650, paint);	//0%표시선 그리기(시작점의 xy좌표, 끝점의 xy좌표)

            paint.setStyle(Paint.Style.FILL);		//내부를 색상 채워라(FILL)

            paint.setColor(Color.RED);			//paint객체의 색상 다시 빨간색으로 지정
            Rect rect1 = new Rect(15, 400, 15+30, 250+400); //첫번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect1, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(15);                              //글짜 표기
            canvas.drawText("Happy", 10, 680, paint);	//표시명, 시작점의 xy좌표

            paint.setColor(Color.RED);
            Rect rect2 = new Rect(75, 100, 75+30, 250+400); //두번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect2, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(15);                              //글짜 표기
            canvas.drawText("angry", 75, 680, paint);	//표시명, 시작점의 xy좌표

            paint.setColor(Color.RED);
            Rect rect3 = new Rect(135, 50, 135+30, 250+400); //두번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect3, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(15);                              //글짜 표기
            canvas.drawText("urgent", 130, 680, paint);	//표시명, 시작점의 xy좌표

            paint.setColor(Color.RED);
            Rect rect4 = new Rect(195, 225, 195+30, 250+400); //두번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect4, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(15);                              //글짜 표기
            canvas.drawText("tired", 195, 680, paint);	//표시명, 시작점의 xy좌표

            paint.setColor(Color.RED);
            Rect rect5 = new Rect(255, 500, 255+30, 250+400); //다섯번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect5, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(15);                              //글짜 표기
            canvas.drawText("xxxx", 255, 680, paint);	//표시명, 시작점의 xy좌표

            paint.setColor(Color.RED);
            Rect rect6 = new Rect(315, 75, 315+30, 250+400); //여섯번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect6, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(15);                              //글짜 표기
            canvas.drawText("xxxx", 315, 680, paint);	//표시명, 시작점의 xy좌표

            paint.setColor(Color.RED);
            Rect rect7 = new Rect(375, 50, 375+30, 250+400); //여섯번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect7, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(15);                              //글짜 표기
            canvas.drawText("xxxx", 375, 680, paint);	//표시명, 시작점의 xy좌표
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0,1,0, "운전자의 상태 기록");
        menu.add(0,2,0, "표정 분석 결과 기록");
        menu.add(0,3,0, "돌아 가기");
        //SubMenu sMenu = menu.addSubMenu("색상 변경 >>");  //서브메뉴 필요하면 사용할 수 있음.
        //sMenu.add(0,4,0, "빨강");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case 1:
                setContentView(new MyGraphicView(this));
                return true;

            case 2:
                setContentView(new GraphicView(this));
                return true;
            case 3:
                finish();
                Toast.makeText(FourthActivity.this, "돌아가기 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();
                return true;
            //case 4:

            //return true;
        }
        return  super.onOptionsItemSelected(item);
    }
}
