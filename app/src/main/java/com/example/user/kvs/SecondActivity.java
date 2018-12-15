package com.example.user.kvs;
//시작버튼 시 열리는 새로운 액티비티에 대한 자바소스

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.speech.tts.TextToSpeech;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.hardware.Camera.Size;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class SecondActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
//    private TextToSpeech textToSpeech1;      //음성출력
//    private TextToSpeech textToSpeech2;
    int tog = 0;

    static TimerTask tt;

    private static final String TAG = "android_camera_example";
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int CAMERA_FACING =Camera.CameraInfo.CAMERA_FACING_FRONT; // Camera.CameraInfo.CAMERA_FACING_FRONT

    private SurfaceView surfaceView;
    private CameraPreview mCameraPreview;
    private View mLayout;  // Snackbar 사용하기 위해서는 View가 필요합니다.
    // (참고로 Toast에서는 Context가 필요했습니다.)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 상태바를 안보이도록 합니다.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 화면 켜진 상태를 유지합니다.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.second_main);

        final Button button = findViewById(R.id.button_main_capture);
        final Button button1 = findViewById(R.id.button_main_stop);
        final Button button2 = findViewById(R.id.button_main_exit);
        new KVSSpeech(this,"얼굴인식을 위해 화면에 얼굴을 위치시킨 후 시작버튼을 눌러주십시오.");
//        textToSpeech2 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if (status == TextToSpeech.SUCCESS) { //Speech()
//                    //사용할 언어를 설정
//                    int result = textToSpeech2.setLanguage(Locale.KOREA);
//                    //언어 데이터가 없거나 혹은 언어가 지원하지 않으면...
//                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
//                        Toast.makeText(SecondActivity.this, "이 언어는 지원하지 않습니다.", Toast.LENGTH_SHORT).show();
//                    } else {
//                        button2.setEnabled(true);
//                        //음성 톤
//                        textToSpeech2.setPitch(0.7f);
//                        //읽는 속도
//                        textToSpeech2.setSpeechRate(1.2f);
//                    }
//                }
//            }
//        });

        mLayout = findViewById(R.id.layout_main);
        surfaceView = findViewById(R.id.camera_preview_main);

        // 런타임 퍼미션 완료될때 까지 화면에서 보이지 않게 해야합니다.
        surfaceView.setVisibility(View.GONE);

        final Timer timer = new Timer();        //0.5초 주기로 자동촬영을 위한 타이머메소드 구현

        button.setOnClickListener(new View.OnClickListener() {      //Start 버튼
            @Override
            public void onClick(View v) {
                tog = 1;
                tt = timerTaskMaker();  //버튼입력 시 타이머를 생성해주기. -> cancel() 일어날 경우 타이머가 완전히 지워짐!
                timer.schedule(tt, 0, 500);     //0.5초 주기 타이머실행
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {     //Stop 버튼
            @Override
            public void onClick(View v) {
                tog = 0;
                tt.cancel();            //cancel()을 이용해 타이머를 종료시킨다.
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tog == 1){
                    Speech();
                }
                else{
                    finish();
                }
            }
        });
        //지원하는 디바이스에 카메라가 있다면
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            int writeExternalStoragePermission =
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //이 부분이 카메라 사용권한을 설정하고 외부저장소 갤러리 사용권한이 설정 되었는지 확인 하는 부분
            if (cameraPermission == PackageManager.PERMISSION_GRANTED
                    && writeExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            }
            else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Snackbar.make(mLayout, "이 앱을 실행하려면 카메라와 외부 저장소 접근 권한이 필요합니다.",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            ActivityCompat.requestPermissions( SecondActivity.this, REQUIRED_PERMISSIONS,
                                    PERMISSIONS_REQUEST_CODE);
                        }
                    }).show();
                }
                else {
                    // 2. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                    ActivityCompat.requestPermissions( this, REQUIRED_PERMISSIONS,
                            PERMISSIONS_REQUEST_CODE);
                }
            }
        }
        else {  //우리가 사용하는 디바이스에 카메라가 지원하지 않는 경우 Snackbar를 이용하여 사용자에게 전달
            final Snackbar snackbar = Snackbar.make(mLayout, "디바이스가 카메라를 지원하지 않습니다.",
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("확인", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
    }

    public TimerTask timerTaskMaker(){
        TimerTask tempTask = new TimerTask() {
            @Override
            public void run() {
                mCameraPreview.takePicture();
            }
        };
        return tempTask;
    }

    //음성출력을 위한 스피치메소드
    private void Speech() {
        new KVSSpeech(this,"온전한 종료를 위한 멈춤버튼을 누른 후에 나가기버튼을 눌러주십시오.");
    }

    //얼굴인식을 위한 음성 출력
    @Override
    protected void onStop() {
        super.onStop();
//        if (textToSpeech1 != null) {
//            textToSpeech1.stop();
//            textToSpeech1.shutdown();
//        }
//        if (textToSpeech2 != null) {
//            textToSpeech2.stop();
//            textToSpeech2.shutdown();
//        }
    }

    void startCamera(){

        // Create the Preview view and set it as the content of this Activity.
        mCameraPreview = new CameraPreview(this, this, CAMERA_FACING, surfaceView);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {

        if ( requestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            boolean check_result = true;

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if ( check_result ) {

                startCamera();
            }
            else {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();

                }else {

                    Snackbar.make(mLayout, "설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
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

// 카메라에서 가져온 영상을 보여주는 카메라 프리뷰 클래스
class CameraPreview extends ViewGroup implements SurfaceHolder.Callback {
    private String recogdata;
    private String img_path;
    private final String TAG = "CameraPreview";

    private int mCameraID;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Camera.CameraInfo mCameraInfo;
    private int mDisplayOrientation;
    private List<Size> mSupportedPreviewSizes;
    private Size mPreviewSize;
    private boolean isPreview = false;

    private AppCompatActivity mActivity;

    public CameraPreview(Context context, AppCompatActivity activity, int cameraID, SurfaceView surfaceView) {
        super(context);


        Log.d("@@@", "Preview");



        mActivity = activity;
        mCameraID = cameraID;
        mSurfaceView = surfaceView;


        mSurfaceView.setVisibility(View.VISIBLE);


        // SurfaceHolder.Callback를 등록하여 surface의 생성 및 해제 시점을 감지
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // We purposely disregard child measurements because act as a
        // wrapper to a SurfaceView that centers the camera preview instead
        // of stretching it.
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);

        if (mSupportedPreviewSizes != null) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed && getChildCount() > 0) {
            final View child = getChildAt(0);

            final int width = r - l;
            final int height = b - t;

            int previewWidth = width;
            int previewHeight = height;
            if (mPreviewSize != null) {
                previewWidth = mPreviewSize.width;
                previewHeight = mPreviewSize.height;
            }

            // Center the child SurfaceView within the parent.
            if (width * previewHeight > height * previewWidth) {
                final int scaledChildWidth = previewWidth * height / previewHeight;
                child.layout((width - scaledChildWidth) / 2, 0,
                        (width + scaledChildWidth) / 2, height);
            } else {
                final int scaledChildHeight = previewHeight * width / previewWidth;
                child.layout(0, (height - scaledChildHeight) / 2,
                        width, (height + scaledChildHeight) / 2);
            }
        }
    }



    // Surface가 생성되었을 때 어디에 화면에 프리뷰를 출력할지 알려줘야 한다.
    public void surfaceCreated(SurfaceHolder holder) {

        // Open an instance of the camera
        try {
            mCamera = Camera.open(mCameraID); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.e(TAG, "Camera " + mCameraID + " is not available: " + e.getMessage());
        }


        // retrieve camera's info.
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraID, cameraInfo);

        mCameraInfo = cameraInfo;
        mDisplayOrientation = mActivity.getWindowManager().getDefaultDisplay().getRotation();

        int orientation = calculatePreviewOrientation(mCameraInfo, mDisplayOrientation);
        mCamera.setDisplayOrientation(orientation);



        mSupportedPreviewSizes =  mCamera.getParameters().getSupportedPreviewSizes();
        requestLayout();

        // get Camera parameters
        Camera.Parameters params = mCamera.getParameters();

        List<String> focusModes = params.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            // set the focus mode
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            // set Camera parameters
            mCamera.setParameters(params);
        }


        try {

            mCamera.setPreviewDisplay(holder);

            // Important: Call startPreview() to start updating the preview
            // surface. Preview must be started before you can take a picture.
            mCamera.startPreview();
            isPreview = true;
            Log.d(TAG, "Camera preview started.");
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }

    }



    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        // Release the camera for other applications.
        if (mCamera != null) {
            if (isPreview)
                mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            isPreview = false;
        }

    }


    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }



    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            Log.d(TAG, "Preview surface does not exist");
            return;
        }


        // stop preview before making changes
        try {
            mCamera.stopPreview();
            Log.d(TAG, "Preview stopped.");
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }

        int orientation = calculatePreviewOrientation(mCameraInfo, mDisplayOrientation);
        mCamera.setDisplayOrientation(orientation);

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            Log.d(TAG, "Camera preview started.");
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }

    }



    /**
     * 안드로이드 디바이스 방향에 맞는 카메라 프리뷰를 화면에 보여주기 위해 계산합니다.
     */
    public static int calculatePreviewOrientation(Camera.CameraInfo info, int rotation) {
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        }
        else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }



    public void takePicture(){

        try {
            mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
        }catch(Exception e){

        }
    }


    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {

        }
    };

    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };


    //참고 : http://stackoverflow.com/q/37135675
    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {

            //이미지의 너비와 높이 결정
            int w = camera.getParameters().getPictureSize().width;
            int h = camera.getParameters().getPictureSize().height;
            int orientation = calculatePreviewOrientation(mCameraInfo, mDisplayOrientation);
            //여기서의 orientation의 값은 갤러리에 저장된 사진의 각도를 의미함. 아랫부분 참조

            //byte array를 bitmap으로 변환
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
            //CameraPreview.this.data = data;

            //이미지를 디바이스 방향으로 회전
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation - 180);   //-180를 해줘야 영상이 반대로 보이지 않음.
            bitmap =  Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);

            //bitmap을 byte array로 변환
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
            byte[] currentData = stream.toByteArray();

            //파일로 저장
            new SaveImageTask().execute(currentData);

        }
    };

    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {
        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream = null;

            try {
                File path = new File (Environment.getExternalStorageDirectory().getAbsolutePath() + "/camtest");
                if (!path.exists()) {
                    path.mkdirs();
                }

                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outputFile = new File(path, fileName);
                Log.d("test", path + "/"+ fileName);

                img_path = path +"/" + fileName;
                outStream = new FileOutputStream(outputFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();
                goSend();   //php

                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to " + outputFile.getAbsolutePath());

                mCamera.startPreview();

                // 갤러리에 반영
                Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(Uri.fromFile(outputFile));
                getContext().sendBroadcast(mediaScanIntent);

                try {
                    mCamera.setPreviewDisplay(mHolder);
                    mCamera.startPreview();
                    Log.d(TAG, "Camera preview started.");
                } catch (Exception e) {
                    Log.d(TAG, "Error starting camera preview: " + e.getMessage());
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private void goSend(){
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file","origianl_photo.jpg", RequestBody.create(MultipartBody.FORM, new File(img_path)))
                .build();

        Request request = new Request.Builder()
                .url("http://kvs.j-confiance.io/picture?phone_number=01085414764")
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                recogdata = response.body().string();              //안면의 상태와 감정데이터값을 받는 부분(String값으로 받음)
                Log.d(TAG, "onResponse: " + recogdata);     //String값으로 받는 데이터를 로그를 이용해 확인해봄.
                doJSONParser();                                     //JSON parsing를 위한 메소드
            }
        });
    }
    int tog1 = 0;
    int tog2 = 0;
    int tog3 = 0;

    Double surprisedconfidence = 0.0;
    Double calmconfidence = 0.0;
    Double happyconfidence = 0.0;
    Double angryconfidence = 0.0;
    Double sadconfidence = 0.0;
    void doJSONParser(){
        StringBuffer sb = new StringBuffer();
        String str = recogdata;
        try {
            JSONArray jDetails = new JSONObject(str).getJSONObject("body").getJSONArray("FaceDetails");
            Log.d(TAG, "결과 " + jDetails.length());
            if (jDetails.length() == 0){
                new KVSSpeech(this.getContext(),"살아는 계신가요?");
                //추가할 곳
            }
            JSONObject jarray = jDetails.getJSONObject(0);   // JSONArray 생성
            JSONObject eyesopen = jarray.getJSONObject("EyesOpen");
            JSONArray emotion = jarray.getJSONArray("Emotions");
            JSONObject pose = jarray.getJSONObject("Pose");

            //눈 상태
            String eyesopentype = eyesopen.getString("Value");
            Double eyesopenconfidence = eyesopen.getDouble("Confidence");

            //전방주시(얼굴의 각도에 해당함)
            Double yaw = pose.getDouble("Yaw");         //좌우
            Double pitch = pose.getDouble("Pitch");     //위아래

            for(int i = 0; i < emotion.length(); i++){  //이모션배열의 내부를 순회하면 해당 값 추출
                JSONObject jObject = emotion.getJSONObject(i);
                String type = jObject.getString("Type");
                if("SURPRISED".equals(type)){
                    surprisedconfidence = jObject.getDouble("Confidence");
                }
                else if("CALM".equals(type)){
                    calmconfidence = jObject.getDouble("Confidence");
                }
                else if("SAD".equals(type)){
                    sadconfidence = jObject.getDouble("Confidence");
                }
                else if("ANGRY".equals(type)){
                    angryconfidence = jObject.getDouble("Confidence");
                }
                else if("HAPPY".equals(type)){
                    happyconfidence = jObject.getDouble("Confidence");
                }
            }

            if(yaw > 35.0 || yaw < -35.0){            //좌우전방주시태만
                tog1++;
                if(tog1 == 2){
                    new KVSSpeech(this.getContext(),"전방을 주시해 주십시오.");
                    tog1 = 0;
                }
            }
            else{
                tog1 = 0;
            }
            if(pitch < 0){            //아래전방주시태만
                tog2++;
                if(tog2 == 2){
                    new KVSSpeech(this.getContext(),"운전중에 주무시는 건가요? 전방을 주시해야합니다.");
                    tog2 = 0;
                }
            }
            else{
                tog2 = 0;
            }

            //보복운전 상태감지
            if(surprisedconfidence >= 20 && calmconfidence <= 50){
                new KVSSpeech(this.getContext(),"보복운전은 심신건강에 해롭습니다.");
            }

            //졸음 운전 상태
            if ("false".equals(eyesopentype) &&  eyesopenconfidence >= 99) {
                tog3++;
                if(tog3 == 3){
                    new KVSSpeech(this.getContext(),"졸음운전은 지옥으로 가는 지름길입니다.");
                    tog3 = 0;
                }
            }
            else{
                tog3 = 0;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    } // end doJSONParser()
}
