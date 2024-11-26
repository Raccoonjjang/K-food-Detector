package com.K_Food_Detector.k_fooddetector.TextDetector;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;

import com.K_Food_Detector.k_fooddetector.SearchActivity;
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
  public void extractTextDetails(String query) {
    StringBuilder resultBuilder = new StringBuilder();
    boolean hasResults = false;

    List<com.K_Food_Detector.k_fooddetector.FindFood.Food> results = findfood.searchFoodByName(query);
    if (!results.isEmpty()) {
      hasResults = true;
      for (com.K_Food_Detector.k_fooddetector.FindFood.Food food : results) {
        String KName = food.getK_name();
        String Ename = food.getE_name();
        String resultText = String.format("Korean Name: %s\nEnglish Name: %s\n", KName, Ename);
        resultBuilder.append(resultText).append("\n");
      }
    }

    if (!hasResults) {
      resultBuilder.append("검색 결과가 없습니다.\n");
    }

    Intent intent = new Intent(context, SearchActivity.class);
    intent.putExtra("search_results", resultBuilder.toString());
    intent.putExtra("search_query", query.trim());
    context.startActivity(intent);
  }
  public void extractTextDetails(Text text) {
    List<Text.TextBlock> textBlocks = text.getTextBlocks();
    StringBuilder resultBuilder = new StringBuilder();
    boolean hasResults = false;
    StringBuilder queryBuilder = new StringBuilder();  // 검색어를 저장할 StringBuilder

    for (Text.TextBlock block : textBlocks) {
      String blockText = block.getText();
      queryBuilder.append(blockText).append(" ");  // 검색어로 추가

      List<com.K_Food_Detector.k_fooddetector.FindFood.Food> results = findfood.searchFoodByName(blockText);
      if (!results.isEmpty()) {
        hasResults = true;
        for (com.K_Food_Detector.k_fooddetector.FindFood.Food food : results) {
          String KName = food.getK_name();
          String Ename = food.getE_name();
          String resultText = String.format("Korean Name: %s\nEnglish Name: %s\n", KName, Ename);
          resultBuilder.append(resultText).append("\n");
        }
      }
    }

    if (!hasResults) {
      resultBuilder.append("검색 결과가 없습니다.\n");
    }

    Intent intent = new Intent(context, SearchActivity.class);
    intent.putExtra("search_results", resultBuilder.toString());
    intent.putExtra("search_query", queryBuilder.toString().trim());  // 검색어 전달
    context.startActivity(intent);
  }

}
