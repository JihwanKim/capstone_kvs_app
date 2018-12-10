package com.example.user.kvs;
//기록 시 열리는 새로운 액티비티에 대한 자바소스
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class FourthActivity extends AppCompatActivity{
    int tog = 0;


    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(new MyGraphicView(this));

    }
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                tog ^= 1;
                if(tog == 0){
                    setContentView(new MyGraphicView(this));
                }
                else{
                    setContentView(new GraphicView(this));
                }
        }
        return true;
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
            paint.setTextSize(50);                              //글짜 표기
            canvas.drawText("운전자의 상태 기록", 10, 50, paint);	//표시명, 시작점의 xy좌표

            //갤럭시 A5 -> 720 * 1280

            paint.setColor(Color.GREEN);                //paint객체의 색상 초록색 지정
            paint.setTextSize(30);                              //글짜 표기
            canvas.drawText("100%", 640, 110, paint);
            canvas.drawLine(0, 120, 720, 120, paint);	//100%표시선 그리기(시작점의 xy좌표, 끝점의 xy좌표)             //글짜 표기
            canvas.drawText("80%", 660, 110+200, paint);
            canvas.drawLine(0, 120+200, 720, 120+200, paint);	//80%표시선 그리기(시작점의 xy좌표, 끝점의 xy좌표)             //글짜 표기
            canvas.drawText("60%", 660, 110+400, paint);
            canvas.drawLine(0, 120+400, 720, 120+400, paint);	//60%표시선 그리기(시작점의 xy좌표, 끝점의 xy좌표)
            canvas.drawText("40%", 660, 110+600, paint);
            canvas.drawLine(0, 120+600, 720, 120+600, paint);	//40%표시선 그리기(시작점의 xy좌표, 끝점의 xy좌표)
            canvas.drawText("20%", 660, 110+800, paint);
            canvas.drawLine(0, 120+800, 720, 120+800, paint);	//20%표시선 그리기(시작점의 xy좌표, 끝점의 xy좌표)

            paint.setColor(Color.BLACK);
            canvas.drawText("0%", 680, 110+1000, paint);
            canvas.drawLine(0, 120+1000, 720, 120+1000, paint);	//0%표시선 그리기(시작점의 xy좌표, 끝점의 xy좌표)

            paint.setStyle(Paint.Style.FILL);		//내부를 색상 채워라(FILL)

            //&&&&&100% 80% 60% 40% 20% 0%-> top = 120, 320, 520, 720, 920 1120으로 설정하면됨.&&&&&&

            paint.setColor(Color.RED);			//paint객체의 색상 다시 빨간색으로 지정
            Rect rect1 = new Rect(50, 120, 50+50, 120+1000); //첫번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect1, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(25);                              //글짜 표기
            canvas.drawText("man", 50, 1160, paint);	//표시명, 시작점의 xy좌표

            paint.setColor(Color.RED);
            Rect rect2 = new Rect(50+110, 120+200, 50+110+50, 120+1000); //두번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect2, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(25);                              //글짜 표기
            canvas.drawText("smiling", 50+110, 1160, paint);	//표시명, 시작점의 xy좌표

            paint.setColor(Color.RED);
            Rect rect3 = new Rect(50+220, 120+400, 50+220+50, 120+1000); //세번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect3, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(25);                              //글짜 표기
            canvas.drawText("eye", 50+220, 1160, paint);	//표시명, 시작점의 xy좌표
            canvas.drawText("open", 50+220, 1180, paint);	//표시명, 시작점의 xy좌표

            paint.setColor(Color.RED);
            Rect rect4 = new Rect(50+330, 120+600, 50+330+50, 120+1000); //네번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect4, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(25);                              //글짜 표기
            canvas.drawText("age", 50+330, 1160, paint);	//표시명, 시작점의 xy좌표

            paint.setColor(Color.RED);
            Rect rect5 = new Rect(50+440, 120+800, 50+440+50, 120+1000); //다섯번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect5, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(25);                              //글짜 표기
            canvas.drawText("xxxx", 50+440, 1160, paint);	//표시명, 시작점의 xy좌표
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
            paint.setTextSize(50);                              //글짜 표기
            canvas.drawText("운전자의 상태 기록", 10, 50, paint);	//표시명, 시작점의 xy좌표

            //갤럭시 A5 -> 720 * 1280

            paint.setColor(Color.GREEN);                //paint객체의 색상 초록색 지정
            paint.setTextSize(30);                              //글짜 표기
            canvas.drawText("100%", 640, 110, paint);
            canvas.drawLine(0, 120, 720, 120, paint);	//100%표시선 그리기(시작점의 xy좌표, 끝점의 xy좌표)             //글짜 표기
            canvas.drawText("80%", 660, 110+200, paint);
            canvas.drawLine(0, 120+200, 720, 120+200, paint);	//80%표시선 그리기(시작점의 xy좌표, 끝점의 xy좌표)             //글짜 표기
            canvas.drawText("60%", 660, 110+400, paint);
            canvas.drawLine(0, 120+400, 720, 120+400, paint);	//60%표시선 그리기(시작점의 xy좌표, 끝점의 xy좌표)
            canvas.drawText("40%", 660, 110+600, paint);
            canvas.drawLine(0, 120+600, 720, 120+600, paint);	//40%표시선 그리기(시작점의 xy좌표, 끝점의 xy좌표)
            canvas.drawText("20%", 660, 110+800, paint);
            canvas.drawLine(0, 120+800, 720, 120+800, paint);	//20%표시선 그리기(시작점의 xy좌표, 끝점의 xy좌표)

            paint.setColor(Color.BLACK);
            canvas.drawText("0%", 680, 110+1000, paint);
            canvas.drawLine(0, 120+1000, 720, 120+1000, paint);	//0%표시선 그리기(시작점의 xy좌표, 끝점의 xy좌표)

            paint.setStyle(Paint.Style.FILL);		//내부를 색상 채워라(FILL)

            //&&&&&100% 80% 60% 40% 20% 0%-> top = 120, 320, 520, 720, 920 1120으로 설정하면됨.&&&&&&

            paint.setColor(Color.RED);			//paint객체의 색상 다시 빨간색으로 지정
            Rect rect1 = new Rect(50, 120+800, 50+50, 120+1000); //첫번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect1, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(25);                              //글짜 표기
            canvas.drawText("man", 50, 1160, paint);	//표시명, 시작점의 xy좌표

            paint.setColor(Color.RED);
            Rect rect2 = new Rect(50+110, 120+600, 50+110+50, 120+1000); //두번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect2, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(25);                              //글짜 표기
            canvas.drawText("smiling", 50+110, 1160, paint);	//표시명, 시작점의 xy좌표

            paint.setColor(Color.RED);
            Rect rect3 = new Rect(50+220, 120+400, 50+220+50, 120+1000); //세번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect3, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(25);                              //글짜 표기
            canvas.drawText("eye", 50+220, 1160, paint);	//표시명, 시작점의 xy좌표
            canvas.drawText("open", 50+220, 1180, paint);	//표시명, 시작점의 xy좌표

            paint.setColor(Color.RED);
            Rect rect4 = new Rect(50+330, 120+200, 50+330+50, 120+1000); //네번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect4, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(25);                              //글짜 표기
            canvas.drawText("age", 50+330, 1160, paint);	//표시명, 시작점의 xy좌표

            paint.setColor(Color.RED);
            Rect rect5 = new Rect(50+440, 120, 50+440+50, 120+1000); //다섯번째 바 (시작점의 xy좌표, 가로간격, 세로간격)
            canvas.drawRect(rect5, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(25);                              //글짜 표기
            canvas.drawText("xxxx", 50+440, 1160, paint);	//표시명, 시작점의 xy좌표
        }
    }

}
