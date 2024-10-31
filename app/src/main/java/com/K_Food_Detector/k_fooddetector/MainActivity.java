package com.K_Food_Detector.k_fooddetector;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.K_Food_Detector.k_fooddetector.Permission.Permission_Manager;
import com.K_Food_Detector.k_fooddetector.TextDetector.TextRecognitionProcessor; // TextRecognitionProcessor import
import com.google.mlkit.vision.common.InputImage;
import com.K_Food_Detector.k_fooddetector.FindFood.FindFood;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private ImageView imageView;
    private AdView mAdView;
    private Permission_Manager permissionManager;
    private TextRecognitionProcessor textRecognitionProcessor;
    private FindFood findfood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        findfood = new FindFood(this);
        findfood.parseJsonStream();
        permissionManager = new Permission_Manager(this);
        permissionManager.requestPermissions();

        Button btn_image = findViewById(R.id.btn_image);
        btn_image.setOnClickListener(v -> openGallery());
        Button btn_camera = findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(v -> openCamera()) ;
    }
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = null;

            try {
                // 카메라 촬영 결과일 경우
                if (requestCode == CAMERA_REQUEST_CODE && data.getExtras() != null) {
                    bitmap = (Bitmap) data.getExtras().get("data");  // 카메라 결과에서 비트맵 가져오기
                }
                // 갤러리 선택 결과일 경우
                else if (requestCode == PICK_IMAGE_REQUEST) {
                    Uri imageUri = data.getData();
                    if (imageUri != null) {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);  // 갤러리 비트맵 가져오기
                    }
                }

                // 비트맵이 정상적으로 로드된 경우
                if (bitmap != null) {
                    // InputImage로 변환
                    InputImage inputImage = InputImage.fromBitmap(bitmap, 0);

                    // TextRecognitionProcessor 호출
                    textRecognitionProcessor = new TextRecognitionProcessor(findfood, null,this); // 옵션이 없으면 null 사용
                    textRecognitionProcessor.processImage(inputImage); // 이미지 처리
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}