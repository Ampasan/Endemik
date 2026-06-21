package com.example.endemik.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.endemik.R;
import com.example.endemik.adapter.EndemikAdapter;
import com.example.endemik.model.Endemik;
import com.example.endemik.model.Favorit;
import com.example.endemik.repository.EndemikRepository;
import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    //region Variables
    private EndemikAdapter adapter;
    private EndemikRepository repository;
    //endregion

    //region Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        initRepository();
        setupToolbar();
        setupRecyclerView();
        observeFavorites();
    }
    //endregion

    //region Initialization
    private void initRepository() {
        repository = new EndemikRepository(getApplication());
    }

    private void setupToolbar() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.appBarFavorite), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbarFavorite);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        RecyclerView rvFavorite = findViewById(R.id.rvFavorite);
        ViewCompat.setOnApplyWindowInsetsListener(rvFavorite, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), systemBars.bottom);
            return insets;
        });
        rvFavorite.setClipToPadding(false);

        adapter = new EndemikAdapter(endemik -> {
            Intent intent = new Intent(FavoriteActivity.this, DetailActivity.class);
            intent.putExtra("id", endemik.getId());
            startActivity(intent);
        });

        rvFavorite.setLayoutManager(new GridLayoutManager(this, 2));
        rvFavorite.setAdapter(adapter);
    }
    //endregion

    //region Data Observation
    private void observeFavorites() {
        repository.getAllFavorit().observe(this, favorits -> {
            List<Endemik> endemikList = new ArrayList<>();
            for (Favorit f : favorits) {
                endemikList.add(f.toEndemik());
            }
            adapter.setData(endemikList);
        });
    }
    //endregion
}
