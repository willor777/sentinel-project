package com.willor.lib_data.data.local

import androidx.room.*
import com.willor.lib_data.data.local.models.ETFQuoteEntity
import com.willor.lib_data.data.local.models.StockQuoteEntity
import com.willor.lib_data.utils.DbConstants


@Dao
interface StockQuoteTableDAO {
    @Query("SELECT * FROM ${DbConstants.STOCK_QUOTE_TABLE}")
    fun getAllFromTable(): List<StockQuoteEntity>?

    @Query("SELECT * FROM ${DbConstants.STOCK_QUOTE_TABLE} WHERE ticker = :ticker LIMIT 1")
    fun getByTicker(ticker: String): StockQuoteEntity?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllIntoTable(vararg q: StockQuoteEntity)

    @Delete
    fun deleteFromTable(q: StockQuoteEntity)

    @Query("DELETE FROM ${DbConstants.STOCK_QUOTE_TABLE}")
    fun clearTable()
}