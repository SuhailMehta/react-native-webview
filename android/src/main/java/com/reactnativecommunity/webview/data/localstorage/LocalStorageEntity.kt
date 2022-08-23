package com.phonepe.business.depository.core.localstorage

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "web_local_storage")
data class LocalStorageEntity(

    @PrimaryKey
    @ColumnInfo(name = "_id")
    val _id: String,

    @ColumnInfo(name = "value")
    val value: String
)