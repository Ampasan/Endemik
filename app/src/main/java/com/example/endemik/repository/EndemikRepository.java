package com.example.endemik.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.endemik.api.ApiClient;
import com.example.endemik.api.ApiService;
import com.example.endemik.database.AppDatabase;
import com.example.endemik.database.EndemikDao;
import com.example.endemik.model.Endemik;
import com.example.endemik.model.Favorit;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EndemikRepository {
    //region Variables
    private final EndemikDao endemikDao;
    private final ApiService apiService;
    private final ExecutorService executorService;
    //endregion

    //region Constructor
    public EndemikRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        endemikDao = db.endemikDao();
        apiService = ApiClient.getApiService();
        executorService = Executors.newSingleThreadExecutor();
    }
    //endregion

    //region API Data Fetching
    public void fetchDataFromApi(OnDataFetchedListener listener) {
        executorService.execute(() -> {
            if (endemikDao.getCount() > 0) {
                listener.onSuccess();
                return;
            }

            apiService.getEndemikData().enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<List<Endemik>> call, Response<List<Endemik>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        executorService.execute(() -> {
                            endemikDao.insertAll(response.body());
                            listener.onSuccess();
                        });
                    } else {
                        listener.onError("Failed to fetch data");
                    }
                }

                @Override
                public void onFailure(Call<List<Endemik>> call, Throwable t) {
                    listener.onError(t.getMessage());
                }
            });
        });
    }
    //endregion

    public LiveData<List<Endemik>> getEndemikByTipeLimited(String tipe) {
        return endemikDao.getEndemikByTipeLimited(tipe);
    }

    public LiveData<List<Endemik>> getInitialSearchData() {
        return endemikDao.getInitialSearchData();
    }

    public LiveData<Endemik> getEndemikById(String id) {
        return endemikDao.getEndemikById(id);
    }

    public LiveData<List<String>> getUniqueAsalByTipe(String tipe) {
        return endemikDao.getUniqueAsalByTipe(tipe);
    }

    public LiveData<List<Endemik>> getEndemikByTipeAndAsal(String tipe, String asal) {
        return endemikDao.getEndemikByTipeAndAsal(tipe, asal);
    }

    public LiveData<List<Endemik>> searchEndemik(String query) {
        return endemikDao.searchEndemik(query);
    }
    //endregion

    public LiveData<List<Favorit>> getAllFavorit() {
        return endemikDao.getAllFavorit();
    }

    public void insertFavorit(Favorit favorit) {
        executorService.execute(() -> endemikDao.insertFavorit(favorit));
    }

    public void deleteFavorit(Favorit favorit) {
        executorService.execute(() -> endemikDao.deleteFavorit(favorit));
    }

    public LiveData<Boolean> isFavorit(String id) {
        return endemikDao.isFavorit(id);
    }
    //endregion

    //region Interfaces
    public interface OnDataFetchedListener {
        void onSuccess();
        void onError(String message);
    }
    //endregion
}
