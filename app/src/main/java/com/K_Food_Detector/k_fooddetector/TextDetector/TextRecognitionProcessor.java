package com.K_Food_Detector.k_fooddetector.TextDetector;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions;
import com.K_Food_Detector.k_fooddetector.FindFood.FindFood;
import java.util.List;
import com.K_Food_Detector.k_fooddetector.ResultActivity;
public class TextRecognitionProcessor {

  private Context context;
  private static final String TAG = "TextRecProcessor";
  private final TextRecognizer textRecognizer;
  private FindFood findfood;
  public TextRecognitionProcessor(FindFood findfood, KoreanTextRecognizerOptions options,Context context) {
    // ML Kit 텍스트 인식 클라이언트 초기화
    textRecognizer = TextRecognition.getClient(options != null ? options : new KoreanTextRecognizerOptions.Builder().build());
    this.findfood = findfood;
    this.context = context;
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
    List<Text.TextBlock> textBlocks = text.getTextBlocks();
    StringBuilder resultBuilder = new StringBuilder();  // 검색 결과를 담을 StringBuilder
    boolean hasResults = false;  // 검색 결과가 있는지 여부를 추적

    for (Text.TextBlock block : textBlocks) {
      String blockText = block.getText();

      // 검색 결과 처리
      List<com.K_Food_Detector.k_fooddetector.FindFood.Food> results = findfood.searchFoodByName(blockText);
      if (!results.isEmpty()) {
        hasResults = true;  // 검색 결과가 있음을 표시
        for (com.K_Food_Detector.k_fooddetector.FindFood.Food food : results) {
          String KName = food.getK_name();
          String Ename = food.getE_name();
          String Explain = food.getExplain();
          String resultText = String.format(
                  "Korean Name: %s\nEnglish Name: %s\nIngredients: %s\n",
                  KName, Ename, Explain
          );
          resultBuilder.append(resultText).append("\n");
        }
      }
    }

    // 결과가 없을 경우 메시지 추가
    if (!hasResults) {
      resultBuilder.append("검색 결과가 없습니다.\n");
    }

    // Intent로 ResultActivity에 결과 전달
    Intent intent = new Intent(context, ResultActivity.class);
    intent.putExtra("search_results", resultBuilder.toString());  // 결과 전달
    context.startActivity(intent);
  }

  public void stop() {
    // 리소스 정리
    textRecognizer.close();
  }
}
