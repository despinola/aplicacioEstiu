package com.vacaciones.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, DailyActivity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun dailyActivityDao(): DailyActivityDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE daily_activities ADD COLUMN photoUri TEXT NOT NULL DEFAULT ''")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE daily_activities ADD COLUMN tvTime INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE daily_activities ADD COLUMN tabletTime INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE daily_activities ADD COLUMN hasSetTable INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE daily_activities ADD COLUMN hasWashedDishes INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE daily_activities ADD COLUMN hasPreparedBreakfast INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE daily_activities ADD COLUMN hasSunset INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE daily_activities ADD COLUMN hasShootingStars INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE daily_activities ADD COLUMN hasEclipse INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vacaciones_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.userDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(userDao: UserDao) {
            userDao.deleteAll()
            userDao.insert(User(1, "Blai", "👦"))
            userDao.insert(User(2, "Rita", "👧"))
            userDao.insert(User(3, "Miriam", "👩"))
            userDao.insert(User(4, "David", "👨"))
        }
    }
}
