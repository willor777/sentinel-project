package com.willor.lib_data.domain

import android.content.Context
import androidx.room.Room
import com.willor.lib_data.data.local.local_db.StockDataDb
import com.willor.lib_data.data.local.local_preferences.DatastorePrefsManager
import com.willor.lib_data.data.remote.StockData
import com.willor.lib_data.domain.abstraction.IRepo
import com.willor.lib_data.domain.abstraction.IStockData
import com.willor.lib_data.repo.RepoImpl
import com.willor.lib_data.utils.DbConstants
import com.willor.sentinel_bots.bots.DoubleStepBot
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DaggerHiltDI {


    @Provides
    fun provideTestString(): String {
        return "TestString from daggerhiltDI.kt"
    }

    @Singleton
    @Provides
    fun provideStockDataApi(): IStockData {
        return StockData()
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): StockDataDb {
        return Room.databaseBuilder(context, StockDataDb::class.java, DbConstants.DB_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun provideRepo(db: StockDataDb, api: IStockData): IRepo {
        return RepoImpl(db, api)
    }

    @Singleton
    @Provides
    fun provideDatastorePreferences(@ApplicationContext context: Context): DatastorePrefsManager{
        return DatastorePrefsManager(context)
    }


    @Singleton
    @Provides
    fun provideDoubleStepBot(api: IRepo): DoubleStepBot{
        return DoubleStepBot(api)
    }

}