package com.phonepe.business.depository.core.localstorage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocalStorageDao {

    @Query("select value from web_local_storage where _id = :key")
    fun getKey(key: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(localStorageEntity: LocalStorageEntity)

    @Query("delete from web_local_storage where _id= :key")
    fun delete(key: String)

    @Query("delete from web_local_storage where 1")
    suspend fun deleteAll()

}