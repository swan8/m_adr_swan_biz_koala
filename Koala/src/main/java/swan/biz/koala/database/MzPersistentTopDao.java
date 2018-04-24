package swan.biz.koala.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface MzPersistentTopDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insert(@NonNull List<MzPersistentTop> top);

    @Query("SELECT * FROM " + MzPersistentTop.TABLE_NAME + " WHERE 1 = 1")
    Flowable<List<MzPersistentTop>> getAllPersistentTop();

    @Query("SELECT * FROM " + MzPersistentTop.TABLE_NAME + " WHERE 1 = 1")
    List<MzPersistentTop> requestMzTopList();

    @Query("DELETE FROM " + MzPersistentTop.TABLE_NAME)
    int clearAll();
}
