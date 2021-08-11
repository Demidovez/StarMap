package com.kalambur.mappy_stars.room

import android.content.Context
import androidx.room.RoomDatabase

import androidx.room.Database
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kalambur.mappy_stars.pojo.Template
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// TODO: exportSchema = false не дает обновлять БД в будущем (у пользователей все удалится)
@Database(entities = [Template::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun templateDao(): TemplateDao

    class TemplateDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { database ->
                scope.launch {
                    database.templateDao().insertDefault(DataGenerator.getTemplates())
                }
            }
        }
    }

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database"
                )
//                .fallbackToDestructiveMigration()
                .addCallback(TemplateDatabaseCallback(scope))
                .build()

                INSTANCE = instance

                return instance
            }
        }
    }
}