package com.example.endemik.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.endemik.R;
import com.example.endemik.activity.DetailActivity;
import com.example.endemik.adapter.EndemikAdapter;
import com.example.endemik.repository.EndemikRepository;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class HewanFragment extends Fragment {
    //region Variables
    private RecyclerView rvEndemik;
    private EndemikAdapter adapter;
    private EndemikRepository repository;
    private ChipGroup chipGroupAsal;
    private String currentSelectedAsal = "Semua";
    //endregion

    //region Lifecycle
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new EndemikRepository(requireActivity().getApplication());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hewan, container, false);
        rvEndemik = view.findViewById(R.id.rvHewan);
        chipGroupAsal = view.findViewById(R.id.chipGroupAsal);
        setupRecyclerView();
        setupFilters();
        return view;
    }
    //endregion

    //region Filter Setup
    private void setupFilters() {
        repository.getUniqueAsalByTipe("Hewan").observe(getViewLifecycleOwner(), asals -> {
            chipGroupAsal.removeAllViews();
            
            addFilterChip("Semua");
            
            if (asals != null) {
                for (String asal : asals) {
                    addFilterChip(asal);
                }
            }
        });
    }

    private void addFilterChip(String label) {
        Chip chip = new Chip(requireContext());
        chip.setText(label);
        chip.setCheckable(true);
        chip.setClickable(true);
        
        if (label.equals(currentSelectedAsal)) {
            chip.setChecked(true);
        }

        chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (label.equals(currentSelectedAsal)) return;
                currentSelectedAsal = label;
                updateData();
            }
        });

        chipGroupAsal.addView(chip);
    }

    private void updateData() {
        if (currentSelectedAsal.equals("Semua")) {
            repository.getEndemikByTipeLimited("Hewan").observe(getViewLifecycleOwner(), endemiks -> {
                adapter.setData(endemiks);
            });
        } else {
            repository.getEndemikByTipeAndAsal("Hewan", currentSelectedAsal).observe(getViewLifecycleOwner(), endemiks -> {
                adapter.setData(endemiks);
            });
        }
    }
    //endregion

    //region RecyclerView Setup
    private void setupRecyclerView() {
        adapter = new EndemikAdapter(endemik -> {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("id", endemik.getId());
            startActivity(intent);
        });
        
        rvEndemik.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvEndemik.setAdapter(adapter);

        updateData();
    }
    //endregion
}
