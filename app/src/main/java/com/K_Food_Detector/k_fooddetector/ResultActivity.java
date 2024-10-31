package com.K_Food_Detector.k_fooddetector;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdView;

public class ResultActivity extends AppCompatActivity {
    private AdView adView; // AdView 추가
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Intent에서 검색 결과 받아오기
        String searchResults = getIntent().getStringExtra("search_results");
        String[] rows = searchResults.split("\n\n");  // 각 제품 정보별로 분리

        // TableLayout 참조
        TableLayout tableLayout = findViewById(R.id.result_table);

        // 각 제품 정보를 세로 방향으로 출력
        for (String row : rows) {
            String[] columns = row.split("\n");  // 이름, 원재료, 영어명으로 분리

            // 세로로 배치할 각 항목 생성 (한글명, 원재료, 영어명)
            for (String column : columns) {
                // 새 행 생성
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                ));

                // Label과 Value를 분리해 TextView로 추가
                String[] labelAndValue = column.split(": ");
                if (labelAndValue.length == 2) {
                    TextView labelView = new TextView(this);
                    labelView.setText(labelAndValue[0] + ": ");
                    labelView.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                    ));
                    labelView.setPadding(8, 8, 8, 8);

                    TextView valueView = new TextView(this);
                    valueView.setText(labelAndValue[1]);
                    valueView.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                    ));
                    valueView.setPadding(8, 8, 8, 8);

                    // 행에 Label과 Value 추가
                    tableRow.addView(labelView);
                    tableRow.addView(valueView);
                }

                // TableRow를 TableLayout에 추가
                tableLayout.addView(tableRow);
            }

            // 각 제품 정보 끝에 구분선을 추가
            TableRow separatorRow = new TableRow(this);
            TextView separator = new TextView(this);
            separator.setText("-----------------------------");
            separator.setPadding(8, 8, 8, 8);
            separatorRow.addView(separator);
            tableLayout.addView(separatorRow);
        }
    }
}
