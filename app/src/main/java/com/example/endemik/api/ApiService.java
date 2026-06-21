package com.example.endemik.api;

import com.example.endemik.model.Endemik;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("endemik.json")
    Call<List<Endemik>> getEndemikData();
}
