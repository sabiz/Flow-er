package jp.sabiz.flow_er.flowitem.db

import android.content.ContentValues
import android.content.Context
import androidx.room.Database
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import jp.sabiz.flow_er.flowitem.FlowItem

@Database(entities = [FlowItem::class], version = 1)
abstract class FlowDatabase: RoomDatabase(){
    abstract fun flowItemDao(): FlowItemDao

    companion object {
        private var INSTANCE: FlowDatabase? = null

        fun get(context: Context): FlowDatabase {
            if (INSTANCE != null) {
                return INSTANCE!!
            }
            INSTANCE = Room.databaseBuilder(context, FlowDatabase::class.java, "flow_database")
                .addCallback(InitialDataLoader())
                .build()
            return INSTANCE!!
        }

        private class InitialDataLoader: RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                val values = listOf(80L, 25L, 40L, 25L, 40L)
                values.forEachIndexed { idx, l ->
                    db.execSQL("INSERT INTO ${FlowItem.TABLE_NAME} VALUES "+
                                    "(\"${FlowItem.generateId()}\", ${idx + 1}, ${l})"
                    )
                }
            }
        }
    }
}