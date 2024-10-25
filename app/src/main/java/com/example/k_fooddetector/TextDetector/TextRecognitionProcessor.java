package com.example.k_fooddetector.TextDetector;

import android.graphics.Point;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.Text.Element;
import com.google.mlkit.vision.text.Text.Line;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions;
import com.example.k_fooddetector.FindFood.FindFood;
import java.util.List;
import android.content.Context;
public class TextRecognitionProcessor {

  private static final String TAG = "TextRecProcessor";
  private final TextRecognizer textRecognizer;
  private FindFood findfood;
  public TextRecognitionProcessor(FindFood findfood, KoreanTextRecognizerOptions options) {
    // ML Kit 텍스트 인식 클라이언트 초기화
    textRecognizer = TextRecognition.getClient(options != null ? options : new KoreanTextRecognizerOptions.Builder().build());
    this.findfood = findfood;
  }
  public void processImage(InputImage image) {
    // 이미지에서 텍스트 인식 시작
    textRecognizer.process(image)
            .addOnSuccessListener(new OnSuccessListener<Text>() {
              @Override
              public void onSuccess(Text text) {
                Log.d(TAG, "텍스트 인식 성공");
                extractTextDetails(text);
              }
            })
            .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "텍스트 인식 실패: " + e.getMessage());
              }
            });
  }

  private void extractTextDetails(Text text) {
    // 인식된 텍스트 정보 출력
    List<Text.TextBlock> textBlocks = text.getTextBlocks();
    for (Text.TextBlock block : textBlocks) {
      String blockText = block.getText();
      Log.d(TAG, "텍스트 블록: " + blockText);
      // 검색 결과 처리
      List<com.example.k_fooddetector.FindFood.Food> results = findfood.searchFoodByName(blockText);
      if (!results.isEmpty()) {
        for (com.example.k_fooddetector.FindFood.Food food : results) {
          Log.d(TAG, "검색 결과: " + food.getK_name() + " (" + food.getE_name() + ")");
        }
      } else {
        Log.d(TAG, "검색 결과가 없습니다.");
      }
    }
  }

  public void stop() {
    // 리소스 정리
    textRecognizer.close();
  }
}
