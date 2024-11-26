package com.K_Food_Detector.k_fooddetector.FindFood;
import android.content.Context;
import com.google.gson.stream.JsonReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
public class FindFood {
    private Context context;
    private static FindFood instance;
    private List<Food> foodList = new ArrayList<>();  // 메모리에 저장될 리스트

    public FindFood(Context context) {
        this.context = context;
    }
    public static synchronized FindFood getInstance(Context context) {
        if (instance == null) {
            instance = new FindFood(context);
        }
        return instance;
    }
    public void parseJsonStream() {
        try {
            InputStream is = context.getAssets().open("food.json");
            JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));

            reader.beginArray();  // JSON 배열 시작
            while (reader.hasNext()) {
                reader.beginObject();  // 각 객체의 시작
                String kName = "", eName = "", explain = "";

                while (reader.hasNext()) {
                    String name = reader.nextName();
                    switch (name) {
                        case "PRDLST_NK":
                            kName = reader.nextString();
                            break;
                        case "PRDLST_NM":
                            eName = reader.nextString();
                            break;
                        case "RAWMTRL_NM":
                            explain = reader.nextString();
                            break;
                        default:
                            reader.skipValue();  // 필요 없는 값 건너뛰기
                    }
                }
                reader.endObject();  // 각 객체의 끝

                Food food = new Food(kName, eName, explain);
                foodList.add(food); // 리스트에 추가
                System.out.println("추가된 음식: " + food.toString()); // 추가된 음식 출력
            }
            reader.endArray();  // JSON 배열 끝

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*public void printKNames() {
        for (Food food : foodList) {
            System.out.println(food.getK_name());
        }
    }*/
    public List<Food> getFoodList() {
        return foodList;
    }
    public List<Food> searchFoodByName(String name) {
        List<Food> results = new ArrayList<>();
        for (Food food : foodList) {
            if (food.getK_name().contains(name)) {
                results.add(food);
            }
        }
        return results;
    }

}
