package com.willor.lib_data.data.local.local_db

import androidx.room.*
import com.willor.lib_data.domain.models.MajorIndicesDataEntity
import com.willor.lib_data.utils.DbConstants


@Dao
interface MajorIndicesDataTableDAO {
    @Query("SELECT * FROM ${DbConstants.MAJOR_INDICES_TABLE}")
    fun getAllFromTable(): List<MajorIndicesDataEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllIntoTable(vararg q: MajorIndicesDataEntity)

    @Delete
    fun deleteFromTable(q: MajorIndicesDataEntity)

    @Query("DELETE FROM ${DbConstants.MAJOR_INDICES_TABLE}")
    fun clearTable()
}