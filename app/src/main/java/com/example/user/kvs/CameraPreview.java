package com.example.user.kvs;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// 카메라에서 가져온 영상을 보여주는 카메라 프리뷰 클래스
public class CameraPreview extends ViewGroup implements SurfaceHolder.Callback {
    private String recogdata;
    private String img_path;
    private final String TAG = "CameraPreview";

    private int mCameraID;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Camera.CameraInfo mCameraInfo;
    private int mDisplayOrientation;
    private List<Camera.Size> mSupportedPreviewSizes;
    private Camera.Size mPreviewSize;
    private boolean isPreview = false;

    private AppCompatActivity mActivity;

    // 토글
    private int seeFrontToggle = 0;
    private int sleepToggle = 0;
    private int sleepToggle2 = 0;

    private Double surprisedconfidence = 0.0;
    private Double calmconfidence = 0.0;
    private Double happyconfidence = 0.0;
    private Double angryconfidence = 0.0;
    private Double sadconfidence = 0.0;


    public CameraPreview(Context context, AppCompatActivity activity, int cameraID, SurfaceView surfaceView) {
        super(context);
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
        // wrapper to a SurfaceView that centers the activity_camera preview instead
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

        // Open an instance of the activity_camera
        try {
            mCamera = Camera.open(mCameraID); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.e(TAG, "Camera " + mCameraID + " is not available: " + e.getMessage());
        }


        // retrieve activity_camera's info.
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraID, cameraInfo);

        mCameraInfo = cameraInfo;
        mDisplayOrientation = mActivity.getWindowManager().getDefaultDisplay().getRotation();

        int orientation = calculatePreviewOrientation(mCameraInfo, mDisplayOrientation);
        mCamera.setDisplayOrientation(orientation);


        mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
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
            Logger.d( "Camera preview started.");
        } catch (IOException e) {
            Logger.d( "Error activity_setting activity_camera preview: " + e.getMessage());
        }

    }


    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        // Release the activity_camera for other applications.
        if (mCamera != null) {
            if (isPreview)
                mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            isPreview = false;
        }

    }


    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
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
            for (Camera.Size size : sizes) {
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
            Logger.d( "Preview surface does not exist");
            return;
        }


        // stop preview before making changes
        try {
            mCamera.stopPreview();
            Logger.d("Preview stopped.");
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
            Logger.d( "Error starting activity_camera preview: " + e.getMessage());
        }

        int orientation = calculatePreviewOrientation(mCameraInfo, mDisplayOrientation);
        mCamera.setDisplayOrientation(orientation);

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            Logger.d( "Camera preview started.");
        } catch (Exception e) {
            Logger.d( "Error starting activity_camera preview: " + e.getMessage());
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
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }


    public void takePicture() {

        try {
            mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
        } catch (Exception e) {

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
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);

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
                File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/camtest");
                if (!path.exists()) {
                    path.mkdirs();
                }

                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outputFile = new File(path, fileName);
                Logger.d(path + "/" + fileName);

                img_path = path + "/" + fileName;
                outStream = new FileOutputStream(outputFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();
                sendData();   //php

                Logger.d( "onPictureTaken - wrote bytes: " + data.length + " to " + outputFile.getAbsolutePath());

                mCamera.startPreview();

                // 갤러리에 반영
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(Uri.fromFile(outputFile));
                getContext().sendBroadcast(mediaScanIntent);
                try {
                    mCamera.setPreviewDisplay(mHolder);
                    mCamera.startPreview();
                    Logger.d( "Camera preview started.");
                } catch (Exception e) {
                    Logger.d( "Error starting activity_camera preview: " + e.getMessage());
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void sendData() {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "origianl_photo.jpg", RequestBody.create(MultipartBody.FORM, new File(img_path)))
                .build();

        Request request = new Request.Builder()
                .url("http://kvs.j-confiance.io/picture?" + KVSTelephone.getInstance().getPhoneNumber())
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                recogdata = response.body().string();
                Logger.d( "onResponse for sendData: " + recogdata);
                doJSONParser();
            }
        });
    }

    private void requestSMS() {

        Request request = new Request.Builder()
                .url("http://kvs.j-confiance.io/sms/" + KVSTelephone.getInstance().getPhoneNumber())
                .get()
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Logger.d( "onResponse for requestSMS: " + recogdata);
            }
        });
    }

    void doJSONParser() {
        StringBuffer sb = new StringBuffer();
        String str = recogdata;

        try {
            JSONArray jDetails = new JSONObject(str).getJSONObject("body").getJSONArray("FaceDetails");
            Logger.d( "결과 " + jDetails.length());
            if (jDetails.length() == 0) {
                new KVSSpeech(this.getContext(), "살아는 계신가요?");
                //추가할 곳
            }
            JSONObject jarray = jDetails.getJSONObject(0);   // JSONArray 생성
            JSONObject eyesopen = jarray.getJSONObject("EyesOpen");
            JSONArray emotion = jarray.getJSONArray("Emotions");
            JSONObject pose = jarray.getJSONObject("Pose");

            //눈 상태
            String eyesopentype = eyesopen.getString("Value");
            Double eyesopenconfidence = eyesopen.getDouble("Confidence");

            //전방주시
            Double yaw = pose.getDouble("Yaw");         //좌우
            Double pitch = pose.getDouble("Pitch");     //위아래

            for (int i = 0; i < emotion.length(); i++) {  //이모션내부 배열에서 놀람, 차분함값 뽑아내기
                JSONObject jObject = emotion.getJSONObject(i);
                String type = jObject.getString("Type");
                if ("SURPRISED".equals(type)) {
                    surprisedconfidence = jObject.getDouble("Confidence");
                } else if ("CALM".equals(type)) {
                    calmconfidence = jObject.getDouble("Confidence");
                } else if ("SAD".equals(type)) {
                    sadconfidence = jObject.getDouble("Confidence");
                } else if ("ANGRY".equals(type)) {
                    angryconfidence = jObject.getDouble("Confidence");
                } else if ("HAPPY".equals(type)) {
                    happyconfidence = jObject.getDouble("Confidence");
                }
            }

            //좌우전방주시태만
            if (yaw > 35.0 || yaw < -35.0) {
                seeFrontToggle++;
                if (seeFrontToggle == 2) {
                    new KVSSpeech(this.getContext(), "전방을 주시해 주십시오.");
                    seeFrontToggle = 0;
                }
            } else {
                seeFrontToggle = 0;
            }

            //아래전방주시태만
            if (pitch < 0) {
                sleepToggle++;
                if (sleepToggle == 2) {
                    new KVSSpeech(this.getContext(), "운전중에 주무시는 건가요? 전방을 주시해야합니다.");
                    sleepToggle = 0;
                }
            } else {
                sleepToggle = 0;
            }

            //보복운전 상태감지
            if (surprisedconfidence >= 20 && calmconfidence <= 50) {
                new KVSSpeech(this.getContext(), "보복운전은 심신건강에 해롭습니다.");
            }

            //졸음 운전 상태
            if ("false".equals(eyesopentype) && eyesopenconfidence >= 99) {
                sleepToggle2++;
                if (sleepToggle2 == 3) {
                    new KVSSpeech(this.getContext(), "졸음운전은 지옥으로 가는 지름길입니다.");
                    sleepToggle2 = 0;
                }
            } else {
                sleepToggle2 = 0;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    } // end doJSONParser()
}
