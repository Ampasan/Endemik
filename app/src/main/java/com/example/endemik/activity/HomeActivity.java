package com.example.endemik.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.example.endemik.R;
import com.example.endemik.fragment.HewanFragment;
import com.example.endemik.fragment.TumbuhanFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class HomeActivity extends AppCompatActivity {

    //region Variables
    private SharedPreferences sharedPreferences;
    //endregion

    //region Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences = getSharedPreferences("theme_pref", MODE_PRIVATE);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bottom_navigation), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, 0, 0, systemBars.bottom);
            return insets;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.appBarLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        ImageButton btnSearch = findViewById(R.id.btnSearch);
        ImageButton btnFavorite = findViewById(R.id.btnFavorite);
        ImageButton btnMore = findViewById(R.id.btnMore);
        ImageButton btnThemeToggle = findViewById(R.id.btnThemeToggle);

        updateThemeIcon(btnThemeToggle);

        btnThemeToggle.setOnClickListener(v -> toggleTheme(btnThemeToggle));
        btnMore.setOnClickListener(v -> showIdentityDialog());

        bottomNav.post(() -> {
            int height = bottomNav.getHeight();
            findViewById(R.id.fragment_container).setPadding(0, 0, 0, height);
        });

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_hewan) {
                switchFragment(new HewanFragment());
                return true;
            } else if (itemId == R.id.nav_tumbuhan) {
                switchFragment(new TumbuhanFragment());
                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            switchFragment(new HewanFragment());
        }

        btnSearch.setOnClickListener(v -> startActivity(new Intent(this, SearchActivity.class)));
        btnFavorite.setOnClickListener(v -> startActivity(new Intent(this, FavoriteActivity.class)));
    }
    //endregion

    //region Theme Management
    private void toggleTheme(ImageButton btn) {
        boolean isDarkMode = sharedPreferences.getBoolean("isDarkMode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            sharedPreferences.edit().putBoolean("isDarkMode", false).apply();
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            sharedPreferences.edit().putBoolean("isDarkMode", true).apply();
        }
        updateThemeIcon(btn);
    }

    private void updateThemeIcon(ImageButton btn) {
        boolean isDarkMode = sharedPreferences.getBoolean("isDarkMode", false);
        if (isDarkMode) {
            btn.setImageResource(R.drawable.ic_light);
        } else {
            btn.setImageResource(R.drawable.ic_dark);
        }
    }
    //endregion

    //region UI Components
    private void showIdentityDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_identity, null);
        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .create();

        dialogView.findViewById(R.id.btnClose).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
    //endregion

    //region Navigation
    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
    //endregion
}
