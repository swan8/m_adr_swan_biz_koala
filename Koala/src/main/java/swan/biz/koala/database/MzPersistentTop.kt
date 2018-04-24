package swan.biz.koala.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import swan.biz.koala.model.MztUnit
import java.util.*

@Entity(tableName = MzPersistentTop.TABLE_NAME)
data class MzPersistentTop constructor(
        @PrimaryKey(autoGenerate = true)
        var id: Int
) : MztUnit() {

    companion object {

        const val TABLE_NAME: String = "MzPersistentTop"
    }

    constructor() : this(0)

    var createTime: Long = 0L

    init {
        val calendar: Calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0

        createTime = calendar.timeInMillis
    }
}