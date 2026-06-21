package com.example.endemik.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.endemik.R;
import com.example.endemik.repository.EndemikRepository;

public class SplashActivity extends AppCompatActivity {
    //region Variables
    private static final int SPLASH_DELAY = 2500;
    private EndemikRepository repository;
    private boolean isDataFetched = false;
    private boolean isTimerFinished = false;

    private ProgressBar progressBar;
    private LinearLayout successContainer;
    //endregion

    //region Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("theme_pref", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("isDarkMode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progressBar);
        successContainer = findViewById(R.id.successContainer);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = new EndemikRepository(getApplication());

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            isTimerFinished = true;
            checkNavigation();
        }, SPLASH_DELAY);

        repository.fetchDataFromApi(new EndemikRepository.OnDataFetchedListener() {
            @Override
            public void onSuccess() {
                isDataFetched = true;
                runOnUiThread(() -> checkNavigation());
            }

            @Override
            public void onError(String message) {
                isDataFetched = true;
                runOnUiThread(() -> {
                    Toast.makeText(SplashActivity.this, R.string.failed_to_fetch, Toast.LENGTH_SHORT).show();
                    checkNavigation();
                });
            }
        });
    }
    //endregion

    //region Navigation Logic
    private void checkNavigation() {
        if (isDataFetched && isTimerFinished) {
            progressBar.setVisibility(View.INVISIBLE);
            successContainer.setVisibility(View.VISIBLE);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (!isFinishing()) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                }
            }, 1500);
        }
    }
    //endregion
}
