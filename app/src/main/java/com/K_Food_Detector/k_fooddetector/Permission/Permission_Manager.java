// PermissionManager.java
package com.K_Food_Detector.k_fooddetector.Permission;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

public class Permission_Manager {

    // <editor-fold desc="객체생성">
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 201;
    private static final int REQUEST_CAMERA_PERMISSION = 203; // 카메라 권한 상수 추가
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 300;
    private AppCompatActivity activity;
    //</editor-fold>
    public Permission_Manager(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void requestPermissions() {

        // 외부 저장소 쓰기 권한 요청
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
        }

        // 카메라 권한 요청
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
        }
    }
}