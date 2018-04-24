package swan.biz.koala.database

import android.arch.persistence.room.Room
import swan.biz.koala.KoalaApplicationImpl

object MzPersistentDatabaseHelper {

    private var baseDatabase: MzPersistentBaseDatabase =
            Room.databaseBuilder(KoalaApplicationImpl.getContext()!!, MzPersistentBaseDatabase::class.java, "/mnt/sdcard/mz.db").build()

    fun TOP(): MzPersistentTopDao {
        return baseDatabase.TOP()
    }
}