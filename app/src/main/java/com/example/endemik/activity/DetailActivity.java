package com.example.endemik.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.bumptech.glide.Glide;
import com.example.endemik.R;
import com.example.endemik.model.Endemik;
import com.example.endemik.model.Favorit;
import com.example.endemik.repository.EndemikRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailActivity extends AppCompatActivity {
    //region Variables
    private EndemikRepository repository;
    private Endemik currentEndemik;
    private boolean isFavorite = false;
    //endregion

    //region Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setupWindowInsets();
        initRepository();
        setupToolbar();
        initViews();
    }
    //endregion

    //region Initialization
    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.appBarLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });
    }

    private void initRepository() {
        repository = new EndemikRepository(getApplication());
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initViews() {
        String id = getIntent().getStringExtra("id");
        ImageView imgDetail = findViewById(R.id.imgDetail);
        TextView tvNamaDetail = findViewById(R.id.tvNamaDetail);
        TextView tvNamaLatin = findViewById(R.id.tvNamaLatin);
        TextView tvKlasifikasi = findViewById(R.id.tvKlasifikasi);
        TextView tvAsalSebaran = findViewById(R.id.tvAsalSebaran);
        TextView tvStatus = findViewById(R.id.tvStatus);
        TextView tvDeskripsiDetail = findViewById(R.id.tvDeskripsiDetail);
        Button btnVideo = findViewById(R.id.btnVideo);
        FloatingActionButton fabFavorite = findViewById(R.id.fabFavorite);

        observeEndemik(id, imgDetail, tvNamaDetail, tvNamaLatin, tvKlasifikasi, tvAsalSebaran, tvStatus, tvDeskripsiDetail, btnVideo);
        observeFavorite(id, fabFavorite);
        setupFavoriteClick(fabFavorite);
    }
    //endregion

    //region Data Observation
    private void observeEndemik(String id, ImageView imgDetail, TextView tvNamaDetail, TextView tvNamaLatin, 
                               TextView tvKlasifikasi, TextView tvAsalSebaran, TextView tvStatus, 
                               TextView tvDeskripsiDetail, Button btnVideo) {
        repository.getEndemikById(id).observe(this, endemik -> {
            if (endemik != null) {
                currentEndemik = endemik;
                tvNamaDetail.setText(endemik.getNama());
                tvNamaLatin.setText(endemik.getNamaLatin());
                tvKlasifikasi.setText(String.format("Famili: %s\nGenus: %s", endemik.getFamili(), endemik.getGenus()));
                tvAsalSebaran.setText(String.format("Asal: %s\nSebaran: %s", endemik.getAsal(), endemik.getSebaran()));
                tvStatus.setText(endemik.getStatus());
                tvDeskripsiDetail.setText(endemik.getDeskripsi());

                Glide.with(this).load(endemik.getFoto()).into(imgDetail);

                btnVideo.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(endemik.getVidio()));
                    startActivity(intent);
                });
            }
        });
    }

    private void observeFavorite(String id, FloatingActionButton fabFavorite) {
        repository.isFavorit(id).observe(this, favorite -> {
            isFavorite = favorite != null && favorite;
            fabFavorite.setImageResource(isFavorite ? R.drawable.ic_favorit_filled : R.drawable.ic_favorit);
            fabFavorite.setSupportImageTintList(android.content.res.ColorStateList.valueOf(
                    isFavorite ? android.graphics.Color.RED : android.graphics.Color.GRAY
            ));
        });
    }
    //endregion

    //region Favorite Logic
    private void setupFavoriteClick(FloatingActionButton fabFavorite) {
        fabFavorite.setOnClickListener(v -> {
            if (currentEndemik != null) {
                if (isFavorite) {
                    repository.deleteFavorit(new Favorit(currentEndemik));
                    Toast.makeText(this, R.string.removed_from_favorite, Toast.LENGTH_SHORT).show();
                } else {
                    repository.insertFavorit(new Favorit(currentEndemik));
                    Toast.makeText(this, R.string.added_to_favorite, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //endregion
}
