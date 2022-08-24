package com.reactnativecommunity.webview.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.reactnativecommunity.webview.data.localstorage.LocalStorageDao
import com.reactnativecommunity.webview.data.localstorage.LocalStorageEntity
import com.reactnativecommunity.webview.data.database.CoreDataBase.Companion.VERSION

@Database(
  entities = [
    LocalStorageEntity::class
  ],
  views = [],
  version = VERSION,
  exportSchema = true
)
abstract class CoreDataBase : RoomDatabase() {

  companion object {
    const val VERSION = 1

    private const val DATABASE_NAME = "web_local_storage"

    @Volatile
    var instance: CoreDataBase? = null

    fun getInstance(context: Context): CoreDataBase {
      return instance ?: synchronized(this) {
        instance ?: buildDataBase(context).also {
          instance = it
        }
      }
    }

    private fun buildDataBase(context: Context): CoreDataBase {
      val builder = Room.databaseBuilder(context.applicationContext, CoreDataBase::class.java, DATABASE_NAME)
        .addCallback(object : RoomDatabase.Callback() {
          override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
          }

          override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            Log.d("CoreDB", "onOpen")
          }
        })
        .addMigrations(
        )

      return builder.build()
    }
  }

  abstract fun getLocalStorageDao(): LocalStorageDao



}