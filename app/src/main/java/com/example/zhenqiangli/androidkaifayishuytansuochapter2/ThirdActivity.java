package com.example.zhenqiangli.androidkaifayishuytansuochapter2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by zhenqiangli on 8/21/17.
 */

public class ThirdActivity extends AppCompatActivity {
  private static final String TAG = "ThirdActivity";
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate: " + UserManager.userId);
  }
}
