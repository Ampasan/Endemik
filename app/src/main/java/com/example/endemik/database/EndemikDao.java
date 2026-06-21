package com.example.endemik.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.endemik.model.Endemik;
import com.example.endemik.model.Favorit;
import java.util.List;

@Dao
public interface EndemikDao {
    // Endemik table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Endemik> endemikList);

    @Query("SELECT * FROM endemik WHERE tipe = :tipe")
    LiveData<List<Endemik>> getEndemikByTipe(String tipe);

    @Query("SELECT * FROM endemik WHERE tipe = :tipe ORDER BY (foto IS NOT NULL AND foto != '') DESC LIMIT 20")
    LiveData<List<Endemik>> getEndemikByTipeLimited(String tipe);

    @Query("SELECT * FROM (SELECT * FROM endemik WHERE tipe = 'Hewan' ORDER BY (foto IS NOT NULL AND foto != '') DESC LIMIT 4) " +
           "UNION ALL " +
           "SELECT * FROM (SELECT * FROM endemik WHERE tipe = 'Tumbuhan' ORDER BY (foto IS NOT NULL AND foto != '') DESC LIMIT 4)")
    LiveData<List<Endemik>> getInitialSearchData();

    @Query("SELECT * FROM endemik WHERE nama LIKE '%' || :query || '%' OR deskripsi LIKE '%' || :query || '%'")
    LiveData<List<Endemik>> searchEndemik(String query);

    @Query("SELECT * FROM endemik WHERE id = :id")
    LiveData<Endemik> getEndemikById(String id);

    @Query("SELECT DISTINCT asal FROM endemik WHERE tipe = :tipe ORDER BY asal ASC")
    LiveData<List<String>> getUniqueAsalByTipe(String tipe);

    @Query("SELECT * FROM endemik WHERE tipe = :tipe AND asal = :asal ORDER BY (foto IS NOT NULL AND foto != '') DESC")
    LiveData<List<Endemik>> getEndemikByTipeAndAsal(String tipe, String asal);

    @Query("SELECT * FROM favorit WHERE id = :id")
    LiveData<Favorit> getFavoritById(String id);

    @Query("SELECT COUNT(*) FROM endemik")
    int getCount();

    // Favorit table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorit(Favorit favorit);

    @Delete
    void deleteFavorit(Favorit favorit);

    @Query("SELECT * FROM favorit")
    LiveData<List<Favorit>> getAllFavorit();

    @Query("SELECT EXISTS(SELECT 1 FROM favorit WHERE id = :id)")
    LiveData<Boolean> isFavorit(String id);
}
