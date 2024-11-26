package com.K_Food_Detector.k_fooddetector;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.K_Food_Detector.k_fooddetector.FindFood.FindFood;
import com.K_Food_Detector.k_fooddetector.TextDetector.TextRecognitionProcessor;
public class SearchActivity extends AppCompatActivity {
    private EditText searchEditText;
    private Button searchButton;
    private TextRecognitionProcessor tp;
    private FindFood findfood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        findfood = FindFood.getInstance(this);
        // 검색어를 Intent로부터 받아 EditText에 설정
        String searchQuery = getIntent().getStringExtra("search_query");
        if (searchQuery != null) {
            searchEditText.setText(searchQuery);
        }

        TableLayout tableLayout = findViewById(R.id.result_table);

        // 검색 결과를 Intent로부터 받아와서 분리
        String searchResults = getIntent().getStringExtra("search_results");
        if (searchResults != null) {
            displayResults(searchResults, tableLayout);
        }

        // 버튼 클릭 시 EditText의 내용을 Intent로 전달하여 SearchActivity 다시 시작
        searchButton.setOnClickListener(v -> reSearch()) ;

    }
    private void reSearch() {
        String newQuery = searchEditText.getText().toString();
        // TextRecognitionProcessor 인스턴스 생성
        TextRecognitionProcessor textRecognitionProcessor = new TextRecognitionProcessor(findfood, null, SearchActivity.this);

        // 문자열 검색어로 extractTextDetails 실행
        textRecognitionProcessor.extractTextDetails(newQuery);
    }
    private void displayResults(String searchResults, TableLayout tableLayout) {
        tableLayout.removeAllViews(); // 기존 결과 지우기
        String[] rows = searchResults.split("\n\n");

        for (String row : rows) {
            String[] columns = row.split("\n");
            String koreanName = "";
            String englishName = "";

            for (String column : columns) {
                String[] labelAndValue = column.split(": ");
                if (labelAndValue.length == 2) {
                    switch (labelAndValue[0].trim()) {
                        case "Korean Name":
                            koreanName = labelAndValue[1];
                            break;
                        case "English Name":
                            englishName = labelAndValue[1];
                            break;
                    }
                }
            }

            TableRow koreanNameRow = new TableRow(this);
            koreanNameRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
            TextView koreanNameView = new TextView(this);
            koreanNameView.setText("Korean Name: " + koreanName);
            koreanNameView.setPadding(16, 8, 16, 8);
            koreanNameRow.addView(koreanNameView);
            tableLayout.addView(koreanNameRow);

            TableRow englishNameRow = new TableRow(this);
            englishNameRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
            TextView englishNameView = new TextView(this);
            englishNameView.setText("English Name: " + englishName);
            englishNameView.setPadding(16, 8, 16, 8);
            englishNameRow.addView(englishNameView);
            tableLayout.addView(englishNameRow);

            TableRow separatorRow = new TableRow(this);
            TextView separator = new TextView(this);
            separator.setText("-----------------------------");
            separator.setPadding(16, 8, 16, 8);
            separatorRow.addView(separator);
            tableLayout.addView(separatorRow);
        }
    }
}
