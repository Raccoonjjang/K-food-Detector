package com.example.k_fooddetector;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.k_fooddetector.Permission.Permission_Manager;
import com.example.k_fooddetector.TextDetector.TextRecognitionProcessor; // TextRecognitionProcessor import
import com.google.mlkit.vision.common.InputImage;
import com.example.k_fooddetector.FindFood.FindFood;
public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;

    private Permission_Manager permissionManager;
    private TextRecognitionProcessor textRecognitionProcessor;
    private FindFood findfood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findfood = new FindFood(this);
        findfood.parseJsonStream();
        permissionManager = new Permission_Manager(this);
        permissionManager.requestPermissions();

        Button buttonPickImage = findViewById(R.id.btn_image);

        buttonPickImage.setOnClickListener(v -> openGallery());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                    // InputImage로 변환
                    InputImage inputImage = InputImage.fromBitmap(bitmap, 0);

                    // TextRecognitionProcessor 호출
                    textRecognitionProcessor = new TextRecognitionProcessor(findfood, null); // 옵션이 없으면 null 사용
                    textRecognitionProcessor.processImage(inputImage); // 이미지 처리

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
