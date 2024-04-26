package com.erendogan6.unilist.di

import android.content.Context
import androidx.room.Room
import com.erendogan6.unilist.db.UniversityDao
import com.erendogan6.unilist.db.UniversityDatabase
import com.erendogan6.unilist.network.ApiService
import com.erendogan6.unilist.repository.UniversityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class) class AppModule {

    private val url = "https://storage.googleapis.com/invio-com/usg-challenge/universities-at-turkey/"

    @Provides @Singleton fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
        }.build()
    }

    @Provides @Singleton fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(url).client(provideOkHttpClient()).addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides @Singleton fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides @Singleton fun provideUniversityDao(@ApplicationContext context: Context): UniversityDao {
        val db = Room.databaseBuilder(context, UniversityDatabase::class.java, "universityDB").fallbackToDestructiveMigration().build()
        return db.getDao()
    }

    @Provides @Singleton fun provideUniversityRepository(apiService: ApiService, universityDao: UniversityDao): UniversityRepository {
        return UniversityRepository(apiService, universityDao)
    }
}
