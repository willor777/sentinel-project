package com.willor.lib_data.data.local

import androidx.room.*
import com.willor.ktstockdata.quote_data.dataobjects.ETFQuote
import com.willor.lib_data.data.local.models.ETFQuoteEntity
import com.willor.lib_data.utils.DbConstants


@Dao
interface ETFQuoteTableDAO {

    @Query("SELECT * FROM ${DbConstants.ETF_QUOTE_TABLE}")
    fun getAllFromTable(): List<ETFQuoteEntity>?

    @Query("SELECT * FROM ${DbConstants.ETF_QUOTE_TABLE} WHERE ticker = :ticker LIMIT 1")
    fun getByTicker(ticker: String): ETFQuoteEntity?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllIntoTable(vararg q: ETFQuoteEntity)

    @Delete
    fun deleteFromTable(q: ETFQuoteEntity)

    @Query("DELETE FROM ${DbConstants.ETF_QUOTE_TABLE}")
    fun clearTable()

}