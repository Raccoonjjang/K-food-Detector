package com.K_Food_Detector.k_fooddetector.FindFood;

public class Food {
    public Food(String kName, String eName, String explain) {
        this.K_name = kName;
        this.E_name = eName;
        this.explain = explain;
    }
    private String K_name;
    private String E_name;
    private String explain;
    public String getK_name(){
        return K_name;
    }
    public String getE_name(){
        return E_name;
    }
    public String getExplain(){
        return explain;
    }
    public void setK_name(){
        this.K_name = K_name;
    }
    public void setE_name(){
        this.E_name = E_name;
    }
    public void setExplain(){
        this.explain = explain;
    }
    public String toString() {
        return "Food{" +
                "kName='" + K_name + '\'' +
                ", eName='" + E_name + '\'' +
                ", explain='" + explain + '\'' +
                '}';
    }
}