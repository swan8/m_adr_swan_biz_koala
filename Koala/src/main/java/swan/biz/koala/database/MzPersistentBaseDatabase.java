package swan.biz.koala.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(
        entities = {
                MzPersistentTop.class
        },

        version = 1
)
public abstract class MzPersistentBaseDatabase extends RoomDatabase {

    abstract MzPersistentTopDao TOP();
}
