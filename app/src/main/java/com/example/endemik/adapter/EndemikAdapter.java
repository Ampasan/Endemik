package com.example.endemik.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.endemik.R;
import com.example.endemik.model.Endemik;
import java.util.ArrayList;
import java.util.List;

public class EndemikAdapter extends RecyclerView.Adapter<EndemikAdapter.ViewHolder> {
    //region Variables
    private List<Endemik> endemikList = new ArrayList<>();
    private final OnItemClickListener listener;
    //endregion

    //region Interface
    public interface OnItemClickListener {
        void onItemClick(Endemik endemik);
    }
    //endregion

    //region Constructor
    public EndemikAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }
    //endregion

    //region Data Management
    public void setData(List<Endemik> list) {
        this.endemikList = list;
        notifyDataSetChanged();
    }
    //endregion

    //region RecyclerView Methods
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_endemik, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Endemik endemik = endemikList.get(position);
        holder.bind(endemik, listener);
    }

    @Override
    public int getItemCount() {
        return endemikList.size();
    }
    //endregion

    //region ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgEndemik;
        TextView tvNama;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgEndemik = itemView.findViewById(R.id.imgEndemik);
            tvNama = itemView.findViewById(R.id.tvNama);
        }

        public void bind(Endemik endemik, OnItemClickListener listener) {
            tvNama.setText(endemik.getNama());

            Glide.with(itemView.getContext())
                    .load(endemik.getFoto())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(imgEndemik);

            itemView.setOnClickListener(v -> listener.onItemClick(endemik));
        }
    }
    //endregion
}
