package com.erendogan6.unilist.di

import com.erendogan6.unilist.network.ApiService
import com.erendogan6.unilist.repository.UniversityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class) class AppModule {

    private val BASE_URL = "https://storage.googleapis.com/invio-com/usg-challenge/universities-at-turkey/"

    @Provides @Singleton fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides @Singleton fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides @Singleton fun provideUniversityRepository(apiService: ApiService): UniversityRepository {
        return UniversityRepository(apiService)
    }
}
