package com.willor.lib_data.data.local.local_db

import androidx.room.*
import com.willor.lib_data.domain.models.SnRLevelsEntity
import com.willor.lib_data.utils.DbConstants


@Dao
interface SnRLevelsTableDAO {
    @Query("SELECT * FROM ${DbConstants.S_N_R_TABLE}")
    fun getAllFromTable(): List<SnRLevelsEntity>?

    @Query("SELECT * FROM ${DbConstants.S_N_R_TABLE} WHERE ticker = :ticker LIMIT 1")
    fun getByTicker(ticker: String): SnRLevelsEntity?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllIntoTable(vararg q: SnRLevelsEntity)

    @Delete
    fun deleteFromTable(q: SnRLevelsEntity)

    @Query("DELETE FROM ${DbConstants.S_N_R_TABLE}")
    fun clearTable()
}