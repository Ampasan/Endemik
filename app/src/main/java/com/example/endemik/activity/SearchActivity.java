package com.example.endemik.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.endemik.R;
import com.example.endemik.adapter.EndemikAdapter;
import com.example.endemik.repository.EndemikRepository;

public class SearchActivity extends AppCompatActivity {
    //region Variables
    private EndemikAdapter adapter;
    private EndemikRepository repository;
    private RecyclerView rvSearch;
    private SearchView searchView;
    //endregion

    //region Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initRepository();
        setupToolbar();
        setupRecyclerView();
        setupSearchView();
        
        loadInitialData();
    }
    //endregion

    //region Initialization
    private void initRepository() {
        repository = new EndemikRepository(getApplication());
    }

    private void setupToolbar() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.appBarSearch), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbarSearch);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        rvSearch = findViewById(R.id.rvSearch);
        ViewCompat.setOnApplyWindowInsetsListener(rvSearch, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), systemBars.bottom);
            return insets;
        });
        rvSearch.setClipToPadding(false);

        adapter = new EndemikAdapter(endemik -> {
            Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
            intent.putExtra("id", endemik.getId());
            startActivity(intent);
        });

        rvSearch.setLayoutManager(new GridLayoutManager(this, 2));
        rvSearch.setAdapter(adapter);
    }

    private void setupSearchView() {
        searchView = findViewById(R.id.searchView);
        ImageView closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        if (closeButton != null) {
            closeButton.setImageDrawable(androidx.core.content.ContextCompat.getDrawable(this, R.drawable.ic_remove));
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.trim().isEmpty()) {
                    performSearch(query);
                } else {
                    loadInitialData();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.trim().isEmpty()) {
                    performSearch(newText);
                } else {
                    loadInitialData();
                }
                return true;
            }
        });
    }
    //endregion

    //region Data Loading
    private void loadInitialData() {
        repository.getInitialSearchData().observe(this, endemiks -> {
            if (endemiks != null) {
                adapter.setData(endemiks);
            }
        });
    }

    private void performSearch(String query) {
        repository.searchEndemik(query).observe(this, endemiks -> {
            if (endemiks != null) {
                adapter.setData(endemiks);
            }
        });
    }
    //endregion
}
